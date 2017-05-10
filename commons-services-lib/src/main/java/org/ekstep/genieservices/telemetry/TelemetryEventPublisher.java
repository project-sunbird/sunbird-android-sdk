package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.commons.bean.telemetry.TelemetryEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.util.AsyncExecutor;

import java.util.Collections;
import java.util.Map;

/**
 * Created by swayangjit on 26/4/17.
 */

public class TelemetryEventPublisher {

    public static final String TAG = TelemetryEventPublisher.class.getSimpleName();

    public static void postTelemetryEvent(final Map<String, Object> eventMap) {
        TelemetryEventPublishThreadPool
                .getInstance()
                .execute(new AsyncExecutor.RunnableEx() {
                    @Override
                    public void run() throws Exception {
                        TelemetryEvent event = TelemetryEvent.buildFromMap(Collections.unmodifiableMap(eventMap));
                        EventBus.getDefault().post(event);
                    }
                });
    }
}