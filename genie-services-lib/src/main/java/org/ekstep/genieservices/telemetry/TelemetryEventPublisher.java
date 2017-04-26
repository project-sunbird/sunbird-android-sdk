package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.commons.AppContext;
import org.greenrobot.eventbus.util.AsyncExecutor;

import java.util.Map;

/**
 * Created by swayangjit on 26/4/17.
 */

public class TelemetryEventPublisher {

    public static final String TAG = TelemetryEventPublisher.class.getSimpleName();

    public static void postTelemetryEvent(final AppContext appContext, final Map<String, Object> eventMap) {
        TelemetryEventPublishThreadPool
                .getInstance()
                .execute(new AsyncExecutor.RunnableEx() {
                    @Override
                    public void run() throws Exception {
//                        TelemetryEvent event = TelemetryEvent.buildFromMap(unmodifiableMap(eventMap));
//                        Log.i(EventBus.TAG, "Posting event. " + event.eid());
//                        EventBus.getDefault().post(event);
                    }
                });
    }
}
