package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
import org.ekstep.genieservices.eventbus.EventBus;
import org.greenrobot.eventbus.NoSubscriberEvent;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created on 6/27/2017.
 *
 * @author Sneha
 */
public class GenieSdkEventListener {

    private static GenieSdkEventListener instance = null;

    private GenieSdkEventListener() {
        register();
    }

    public static void init() {
        instance = new GenieSdkEventListener();
    }

    public static void destroy() {
        instance.unregister();
    }

    private void register() {
        EventBus.registerSubscriber(this);
    }

    private void unregister() {
        EventBus.unregisterSubscriber(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onContentImportResponse(ContentImportResponse contentImportResponse) throws InterruptedException {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownloadProgress(DownloadProgress downloadProgress) throws InterruptedException {
    }

    @Subscribe
    public void onTelemetryEvent(Telemetry telemetryEvent) throws InterruptedException {
    }

    @Subscribe
    public void onNoSubscriberEvent(NoSubscriberEvent event) {
    }

}
