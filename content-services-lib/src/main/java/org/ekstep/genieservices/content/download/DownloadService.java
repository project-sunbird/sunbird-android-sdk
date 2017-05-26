package org.ekstep.genieservices.content.download;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.bean.Request;

import java.util.List;

/**
 * Created on 17/5/17.
 *
 * @author swayangjit
 */
public class DownloadService {

    private AppContext mAppContext;
    private DownloadQueueManager mDownloadQueueManager = null;

    public DownloadService(AppContext appContext) {
        this.mAppContext = appContext;
        this.mDownloadQueueManager = new DownloadQueueManager(mAppContext.getKeyValueStore());
    }

    public void enQueue(DownloadRequest... downloadRequest) {
        if (downloadRequest.length > 0) {
            for (DownloadRequest request : downloadRequest) {
                mDownloadQueueManager.save(request);
            }
        }

        startQueue();
    }

    public void startQueue() {
        List<DownloadRequest> downloadRequestList = mDownloadQueueManager.findAll();
        if (downloadRequestList.size() > 0) {
            DownloadRequest downloadRequest = downloadRequestList.get(0);
            long downloadId = mAppContext.getDownloadManager().enqueue(new Request(downloadRequest.getDownloadUrl(), downloadRequest.getIdentifier(), downloadRequest.getMimeType()));
            mDownloadQueueManager.update(downloadRequest.getIdentifier(), downloadId);

        }
    }
}
