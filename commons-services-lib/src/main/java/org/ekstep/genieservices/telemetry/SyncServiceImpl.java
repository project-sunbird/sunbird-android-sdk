package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ISyncService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.SyncStat;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.telemetry.model.ProcessedEventModel;
import org.ekstep.genieservices.telemetry.network.TelemetrySyncAPI;
import org.ekstep.genieservices.telemetry.processors.EventProcessorFactory;

import java.util.HashMap;
import java.util.Locale;

/**
 * This is the implementation of the interface {@link ISyncService}
 */
public class SyncServiceImpl extends BaseService implements ISyncService {

    private static final String TAG = TelemetryServiceImpl.class.getSimpleName();

    public SyncServiceImpl(AppContext appContext) {
        super(appContext);
    }

    @Override
    public GenieResponse<SyncStat> sync() {
        String methodName = "sync@SyncServiceImpl";
        HashMap params = new HashMap();
        params.put("mode", TelemetryLogger.getNetworkMode(mAppContext.getConnectionInfo()));
        params.put("logLevel", "2");
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

}
