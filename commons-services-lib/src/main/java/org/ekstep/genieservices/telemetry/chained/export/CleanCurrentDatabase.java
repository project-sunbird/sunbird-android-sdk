package org.ekstep.genieservices.telemetry.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.TelemetryExportResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.telemetry.bean.ExportTelemetryContext;
import org.ekstep.genieservices.telemetry.model.ProcessedEventsModel;

/**
 * Created on 6/10/2017.
 *
 * @author anil
 */
public class CleanCurrentDatabase implements IChainable<TelemetryExportResponse, ExportTelemetryContext> {

    private static final String TAG = CleanCurrentDatabase.class.getSimpleName();
    private IChainable<TelemetryExportResponse, ExportTelemetryContext> nextLink;

    @Override
    public GenieResponse<TelemetryExportResponse> execute(AppContext appContext, ExportTelemetryContext exportContext) {

        ProcessedEventsModel processedEventsModel = ProcessedEventsModel.build(appContext.getDBSession());
        processedEventsModel.deleteAll();

        if (nextLink != null) {
            return nextLink.execute(appContext, exportContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Export telemetry failed", TAG);
        }
    }

    @Override
    public IChainable<TelemetryExportResponse, ExportTelemetryContext> then(IChainable<TelemetryExportResponse, ExportTelemetryContext> link) {
        nextLink = link;
        return link;
    }
}
