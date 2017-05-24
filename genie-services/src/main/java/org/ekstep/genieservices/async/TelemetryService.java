package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.ITelemetryService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.TelemetryStat;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;

public class TelemetryService {
    private ITelemetryService telemetryService;

    public TelemetryService(GenieService genieService) {
        this.telemetryService = genieService.getTelemetryService();
    }

    public void saveTelemetry(final String eventString, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return telemetryService.saveTelemetry(eventString);
            }
        });
    }

    public void saveTelemetry(final Telemetry event, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return telemetryService.saveTelemetry(event);
            }
        });
    }

    public void getTelemetryStat(IResponseHandler<TelemetryStat> responseHandler) {
        new AsyncHandler<TelemetryStat>(responseHandler).execute(new IPerformable<TelemetryStat>() {
            @Override
            public GenieResponse<TelemetryStat> perform() {
                return telemetryService.getTelemetryStat();
            }
        });
    }

}
