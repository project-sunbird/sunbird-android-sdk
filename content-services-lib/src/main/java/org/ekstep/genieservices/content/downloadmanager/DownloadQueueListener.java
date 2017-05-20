package org.ekstep.genieservices.content.downloadmanager;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.DownloadResponse;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.content.ContentServiceImpl;
import org.ekstep.genieservices.telemetry.event.TelemetryListener;
import org.ekstep.genieservices.util.DownloadQueueManager;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by swayangjit on 20/5/17.
 */

public class DownloadQueueListener {
    private static DownloadQueueListener instance = null;
    private final String TAG = DownloadQueueListener.class.getSimpleName();
    private AppContext mAppContext = null;

    private DownloadQueueListener(AppContext appContext) {
        this.mAppContext = appContext;
        register();
    }

    public static void init(AppContext context) {
        instance = new DownloadQueueListener(context);
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
    public void onDownloadResponse(DownloadResponse downloadResponse) throws InterruptedException {
        Logger.i("onDownloadResponse",downloadResponse.toString());
        new DownloadQueueManager(mAppContext.getKeyValueStore()).remove(downloadResponse.getDownloadId());
        new DownloadService(mAppContext).startQueue();
//        new ContentServiceImpl(appContext).importContent(false,downloadResponse.getFilePath());
    }
    @Subscribe
    public void onDownloadPrgress(DownloadProgress downloadProgress) throws InterruptedException {
        Logger.i("onDownloadResponse",downloadProgress.toString());
    }
}
