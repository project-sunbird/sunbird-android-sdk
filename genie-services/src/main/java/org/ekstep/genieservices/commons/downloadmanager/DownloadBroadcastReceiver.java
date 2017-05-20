package org.ekstep.genieservices.commons.downloadmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.bean.DownloadResponse;
import org.ekstep.genieservices.eventbus.EventPublisher;
import org.ekstep.genieservices.util.DownloadQueueManager;
import org.ekstep.genieservices.utils.DownloadFileUtil;

/**
 * Created by swayangjit on 17/5/17.
 */

public class DownloadBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long receivedId = intent.getLongExtra(android.app.DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        DownloadRequest downloadRequest = new DownloadQueueManager(GenieService.getService().getKeyStore()).getRequest(receivedId);
        if (downloadRequest != null) {
            String filePath = DownloadFileUtil.getDownloadedFilePath(context, intent);
            EventPublisher.postDownloadResponse(new DownloadResponse(receivedId, downloadRequest.getIdentifier(), filePath));
        }
    }
}
