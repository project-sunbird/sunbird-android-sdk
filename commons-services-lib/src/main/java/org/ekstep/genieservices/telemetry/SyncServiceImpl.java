package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ISyncService;
import org.ekstep.genieservices.ITelemetryService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.IParams;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.SyncStat;
import org.ekstep.genieservices.commons.bean.TelemetryStat;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.telemetry.model.ProcessedEventModel;
import org.ekstep.genieservices.telemetry.network.DeviceRegisterAPI;
import org.ekstep.genieservices.telemetry.network.TelemetrySyncAPI;
import org.ekstep.genieservices.telemetry.processors.EventProcessorFactory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This is the implementation of the interface {@link ISyncService}
 */
public class SyncServiceImpl extends BaseService implements ISyncService {

    private static final String TAG = TelemetryServiceImpl.class.getSimpleName();
    private static final int REGISTER_API_SUCCESS_TTL = 24 * DateUtil.MILLISECONDS_IN_AN_HOUR;
    private static final int REGISTER_API_FAILURE_TTL = 1 * DateUtil.MILLISECONDS_IN_AN_HOUR;
    private ITelemetryService mTelemetryService;

    public SyncServiceImpl(AppContext appContext, ITelemetryService telemetryService) {
        super(appContext);
        this.mTelemetryService = telemetryService;
    }

    @Override
    public GenieResponse<SyncStat> sync() {
        String methodName = "sync@SyncServiceImpl";
        HashMap params = new HashMap();
        params.put("mode", TelemetryLogger.getNetworkMode(mAppContext.getConnectionInfo()));
        params.put("logLevel", "2");
        long lastSyncedTime = mAppContext.getKeyValueStore().getLong(ServiceConstants.PreferenceKey.LAST_SYNCED_TIME_STAMP_DEVICE_REGISTER, 0);
        if (lastSyncedTime <= DateUtil.getEpochTime()) {
            registerDevice();
        }
        TelemetryStat telemetryStat = mTelemetryService.getTelemetryStat().getResult();
        if (!mAppContext.getConnectionInfo().isConnected() && telemetryStat.getUnSyncedEventCount() < 300) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.THRESHOLD_LIMIT_NOT_REACHED, ServiceConstants.ErrorMessage.THRESHOLD_LIMIT_NOT_REACHED, TAG);
        }
        EventProcessorFactory.processEvents(mAppContext);

        int numberOfSync = 0;
        int numberOfEventsProcessed = 0;
        long totalByteSize = 0;

        ProcessedEventModel processedEvent = ProcessedEventModel.find(mAppContext.getDBSession());

        while (!isEmpty(processedEvent)) {
            totalByteSize = totalByteSize + processedEvent.getData().length;

            GenieResponse response = new TelemetrySyncAPI(mAppContext, processedEvent.getData()).post();
            if (!response.getStatus()) {
                String message = getMessage(numberOfSync, numberOfEventsProcessed);
                response.setMessage(message);
                TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, ServiceConstants.ErrorMessage.UNABLE_TO_SYNC);
                return response;
            }

            numberOfSync++;
            numberOfEventsProcessed += processedEvent.getNumberOfEvents();

            processedEvent.delete();
            processedEvent = ProcessedEventModel.find(mAppContext.getDBSession());
        }

        String fileSize = calculateByteCountInKB(totalByteSize);
        long syncTime = DateUtil.getEpochTime();
        mAppContext.getKeyValueStore().putLong(ServiceConstants.PreferenceKey.LAST_SYNC_TIME, syncTime);

        GenieResponse<SyncStat> response = GenieResponseBuilder.getSuccessResponse(getMessage(numberOfSync, numberOfEventsProcessed));
        SyncStat syncStat = new SyncStat(numberOfEventsProcessed, syncTime, fileSize);
        response.setResult(syncStat);
        TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
        return response;
    }

    private String calculateByteCountInKB(long bytes) {
        try {
            int unit = 1024;
            return String.format(Locale.US, "%.2f", bytes / Math.pow(unit, 1));
        } catch (Exception e) {
            return "0.00";
        }
    }

    private String getMessage(int numberOfSync, int numberOfEventsProcessed) {
        return String.format(Locale.US, "%d events synced in %d sync", numberOfEventsProcessed, numberOfSync);
    }

    private boolean isEmpty(ProcessedEventModel processedEvent) {
        return processedEvent.getNumberOfEvents() == 0 || processedEvent.getData() == null || processedEvent.getData().length == 0;
    }

    private Map<String, Object> getDeviceRegisterRequest() {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("dspec", mAppContext.getDeviceInfo().getDeviceDetails());
        requestMap.put("channel", mAppContext.getParams().getString(IParams.Key.CHANNEL_ID));
        return requestMap;
    }

    private void registerDevice() {
        DeviceRegisterAPI deviceRegisterAPI = new DeviceRegisterAPI(mAppContext, getDeviceRegisterRequest(), mAppContext.getDeviceInfo().getDeviceID());
        GenieResponse response = deviceRegisterAPI.post();
        if (response != null) {
            mAppContext.getKeyValueStore().putLong(ServiceConstants.PreferenceKey.LAST_SYNCED_TIME_STAMP_DEVICE_REGISTER,
                    DateUtil.getEpochTime() + (response.getStatus() ? REGISTER_API_SUCCESS_TTL : REGISTER_API_FAILURE_TTL));
        }

    }


}
