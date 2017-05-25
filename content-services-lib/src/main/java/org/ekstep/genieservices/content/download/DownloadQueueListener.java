package org.ekstep.genieservices.content.download;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.DownloadResponse;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created on 20/5/17.
 *
 * @author swayangjit
 */
public class DownloadQueueListener {

    private static final String TAG = DownloadQueueListener.class.getSimpleName();

    private static DownloadQueueListener INSTANCE = null;
    private AppContext mAppContext = null;

    private DownloadQueueListener(AppContext appContext) {
        this.mAppContext = appContext;
        register();
    }

    public static void init(AppContext context) {
        INSTANCE = new DownloadQueueListener(context);
    }

    public static void destroy() {
        INSTANCE.unregister();
    }

    private void register() {
        EventBus.getDefault().register(this);
    }

    private void unregister() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDownloadResponse(DownloadResponse downloadResponse) throws InterruptedException {
        Logger.i(TAG, "onDownloadResponse : " + downloadResponse.toString());

        DownloadQueueManager downloadQueueManager = new DownloadQueueManager(mAppContext.getKeyValueStore());
        DownloadService downloadService = new DownloadService(mAppContext);
        if (StringUtil.isNullOrEmpty(downloadResponse.getFilePath())) {  // Download failed
            downloadQueueManager.remove(downloadResponse.getDownloadId());
            downloadService.startQueue();
        } else {    // Download success
            // TODO: 5/22/2017 - Start import and after successful import remove from queue and start next download. Start the Android service to make it async.
            // Start import - new ContentServiceImpl(appContext).importContent(false,downloadResponse.getFilePath());
            // Remove from queue after successful import - downloadQueueManager.remove(downloadResponse.getDownloadId());
            // Start downloading next content after successful import - downloadService.startQueue();
        }
    }

    @Subscribe
    public void onDownloadProgress(DownloadProgress downloadProgress) throws InterruptedException {
        Logger.i(TAG, "onDownloadProgress : " + downloadProgress.toString());
    }
}
