package org.ekstep.genieservices;

import android.content.Context;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
import org.ekstep.genieservices.eventbus.EventBus;
import org.greenrobot.eventbus.NoSubscriberEvent;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Sneha on 6/27/2017.
 */

public class GenieSdkEventListner {

    private static org.ekstep.genieservices.GenieSdkEventListner instance = null;
    private static int randomNumber;
    private static Context context = null;
    private static AppContext appContext = null;
    private final String TAG = org.ekstep.genieservices.GenieSdkEventListner.class.getSimpleName();

    private GenieSdkEventListner(Context context) {
        this.context = context;
        register();
    }

    public static void init(AppContext appcontext) {
        appContext = appcontext;
        instance = new org.ekstep.genieservices.GenieSdkEventListner(context);
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
