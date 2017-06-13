package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.ITelemetryService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportRequest;
import org.ekstep.genieservices.commons.bean.TelemetryStat;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
import org.ekstep.genieservices.importexport.FileImporter;

/**
 * This class provides all the required APIs to perform necessary operations related to Telemetry on a separate thread
 */
public class TelemetryService {

    private ITelemetryService telemetryService;
    private FileImporter fileImporter;

    public TelemetryService(GenieService genieService) {
        this.telemetryService = genieService.getTelemetryService();
        this.fileImporter = genieService.getFileImporter();
    }

    /**
     * This api will save the telemetry details passed to it as String.
     * <p>
     * <p> On successful saving the telemetry, the response will return status as TRUE and with "Event Saved Successfully" message.
     * <p>
     * <p>On failing to save the telemetry details, the response will return status as FALSE and the error be the following:
     * <p>PROCESSING_ERROR
     *
     * @param eventString
     * @param responseHandler
     */
    public void saveTelemetry(final String eventString, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return telemetryService.saveTelemetry(eventString);
            }
        });
    }

    /**
     * This api will save the telemetry details passed to it as {@link Telemetry}.
     * <p>
     * <p> On successful saving the telemetry, the response will return status as TRUE and with "Event Saved Successfully" message.
     * <p>
     * <p>On failing to save the telemetry details, the response will return status as FALSE and the error be the following:
     * <p>PROCESSING_ERROR
     *
     * @param event
     * @param responseHandler
     */
    public void saveTelemetry(final Telemetry event, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return telemetryService.saveTelemetry(event);
            }
        });
    }

    /**
     * This api will give the telemetry stats about unsynced events and last sync time in {@link TelemetryStat}
     * <p>
     * <p>Response status always be True, with {@link TelemetryStat} set in the result.
     *
     * @param responseHandler
     */
    public void getTelemetryStat(IResponseHandler<TelemetryStat> responseHandler) {
        new AsyncHandler<TelemetryStat>(responseHandler).execute(new IPerformable<TelemetryStat>() {
            @Override
            public GenieResponse<TelemetryStat> perform() {
                return telemetryService.getTelemetryStat();
            }
        });
    }

    public void importTelemetry(final ImportRequest importRequest, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return fileImporter.importTelemetry(importRequest, telemetryService);
            }
        });
    }

}
