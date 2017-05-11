package org.ekstep.genieservices.telemetry.eventbus;

import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.util.AsyncExecutor;

/**
 * Created by swayangjit on 26/4/17.
 */

public class TelemetryEventPublisher {

    public static final String TAG = TelemetryEventPublisher.class.getSimpleName();

    public static void postTelemetryEvent(final Telemetry telemetry) {
        TelemetryEventPublishThreadPool
                .getInstance()
                .execute(new AsyncExecutor.RunnableEx() {
                    @Override
                    public void run() throws Exception {
                        EventBus.getDefault().post(telemetry);
                    }
                });
    }
}