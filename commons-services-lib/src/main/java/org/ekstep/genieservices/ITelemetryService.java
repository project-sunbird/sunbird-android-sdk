package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.TelemetryExportRequest;
import org.ekstep.genieservices.commons.bean.TelemetryExportResponse;
import org.ekstep.genieservices.commons.bean.TelemetryImportRequest;
import org.ekstep.genieservices.commons.bean.TelemetryStat;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;

/**
 * This is the interface with all the required APIs to perform necessary operations related to Telemetry
 */

public interface ITelemetryService {

    /**
     * This api will save the telemetry details passed to it as String.
     * <p>
     * <p> On successful saving the telemetry, the response will return status as TRUE and with "Event Saved Successfully" message.
     * <p>
     * <p>On failing to save the telemetry details, the response will return status as FALSE and the error be the following:
     * <p>PROCESSING_ERROR
     *
     * @param eventString - {@link String}
     * @return {@link GenieResponse<Void>}
     */
    GenieResponse<Void> saveTelemetry(String eventString);

    /**
     * This api will save the telemetry details passed to it as {@link Telemetry}.
     * <p>
     * <p> On successful saving the telemetry, the response will return status as TRUE and with "Event Saved Successfully" message.
     * <p>
     * <p>On failing to save the telemetry details, the response will return status as FALSE and the error be the following:
     * <p>PROCESSING_ERROR
     *
     * @param event - {@link Telemetry}
     * @return {@link GenieResponse<Void>}
     */
    GenieResponse<Void> saveTelemetry(Telemetry event);

    /**
     * This api will give the telemetry stats about unsynced events and last sync time in {@link TelemetryStat}
     * <p>
     * <p>Response status always be True, with {@link TelemetryStat} set in the result.
     *
     * @return {@link GenieResponse<TelemetryStat>}
     */
    GenieResponse<TelemetryStat> getTelemetryStat();

    /**
     * This API is used to import telemetry.
     *
     * @param telemetryImportRequest - {@link TelemetryImportRequest}
     * @return {@link GenieResponse<Void>}
     */
    GenieResponse<Void> importTelemetry(TelemetryImportRequest telemetryImportRequest);

    /**
     * This API is used to export telemetry.
     *
     * @param telemetryExportRequest - {@link TelemetryExportRequest}
     * @return {@link GenieResponse<TelemetryExportResponse>}
     */
    GenieResponse<TelemetryExportResponse> exportTelemetry(TelemetryExportRequest telemetryExportRequest);

}
