package org.ekstep.genieservices.eventbus;

import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.util.AsyncExecutor;

/**
 * Created by swayangjit on 26/4/17.
 */

public class EventPublisher {

    public static final String TAG = EventPublisher.class.getSimpleName();

    public static void postTelemetryEvent(final Telemetry telemetry) {
        EventPublisherThreadPool
                .getInstance()
                .execute(new AsyncExecutor.RunnableEx() {
                    @Override
                    public void run() throws Exception {
                        EventBus.getDefault().post(telemetry);
                    }
                });
    }
}