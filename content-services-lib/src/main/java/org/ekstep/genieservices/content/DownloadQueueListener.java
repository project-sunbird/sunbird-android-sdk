package org.ekstep.genieservices.content;

import org.ekstep.genieservices.IDownloadService;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.bean.DownloadResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.download.DownloadQueueManager;
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
    private IDownloadService mDownloadService;

    private DownloadQueueListener(AppContext appContext, IDownloadService downloadService) {
        this.mAppContext = appContext;
        this.mDownloadService = downloadService;
        register();
    }

    public static void init(AppContext context, IDownloadService downloadService) {
        INSTANCE = new DownloadQueueListener(context, downloadService);
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
        DownloadRequest downloadRequest = downloadQueueManager.getRequest(downloadResponse.getDownloadId());
        downloadResponse.setIdentifier(downloadRequest.getIdentifier());
        downloadResponse.setMimeType(downloadRequest.getMimeType());
        if (downloadResponse.getStatus()) {
            new ContentServiceImpl(mAppContext).importContent(downloadRequest.isChildContent(),downloadResponse.getFilePath());
            mDownloadService.onDownloadComplete(downloadResponse);
        } else if (StringUtil.isNullOrEmpty(downloadResponse.getFilePath())) {
            mDownloadService.onDownloadFailed(downloadResponse);
        } else {
            mDownloadService.onDownloadFailed(downloadResponse);
        }
    }

    @Subscribe
    public void onDownloadProgress(DownloadProgress downloadProgress) throws InterruptedException {
        Logger.i(TAG, "onDownloadProgress : " + downloadProgress.toString());
    }
}
