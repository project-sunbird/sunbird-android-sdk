package org.ekstep.genieservices.profile.event;

import com.google.gson.Gson;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
import org.ekstep.genieservices.commons.utils.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.NoSubscriberEvent;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.util.ThrowableFailureEvent;

/**
 * Created on 9/5/17.
 *
 * @author swayangjit
 */
public class SummaryListener {
    private static SummaryListener instance = null;
    private final String TAG = SummaryListener.class.getSimpleName();
    private AppContext appContext = null;

    private SummaryListener(AppContext appContext) {
        this.appContext = appContext;
        register();
    }

    public static void init(AppContext context) {
        instance = new SummaryListener(context);
    }

    public static void destroy() {
        instance.unregister();
    }

    private void register() {
        EventBus.getDefault().register(this);
    }

    private void unregister() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onTelemetryEvent(Telemetry telemetryEvent) throws InterruptedException {
        SummaryHandler.handleTelemetryEvent(telemetryEvent, this.appContext);
    }

    @Subscribe
    public void onThrowableFailureEvent(ThrowableFailureEvent event) {
        Logger.e(TAG, "Got ThrowableFailureEvent, throwable: " + event.getThrowable(), event.getThrowable());
    }

    @Subscribe
    public void onNoSubscriberEvent(NoSubscriberEvent event) {
        Logger.e(TAG, "Got NoSubscriberEvent, event: " + new Gson().toJson(event));
    }
}
