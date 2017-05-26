package org.ekstep.genieservices.commons.download;

import org.ekstep.genieservices.IDownloadService;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.bean.DownloadResponse;
import org.ekstep.genieservices.commons.bean.Request;

import java.util.List;

/**
 * Created on 17/5/17.
 *
 * @author swayangjit
 */
public class DownloadService implements IDownloadService {

    private AppContext mAppContext;
    private DownloadQueueManager mDownloadQueueManager = null;

    public DownloadService(AppContext appContext) {
        this.mAppContext = appContext;
        this.mDownloadQueueManager = new DownloadQueueManager(mAppContext.getKeyValueStore());
    }

    @Override
    public void enqueue(DownloadRequest... downloadRequest) {
        if (downloadRequest.length > 0) {
            for (DownloadRequest request : downloadRequest) {
                mDownloadQueueManager.save(request);
            }
        }
        List<DownloadRequest> downloadRequestList = mDownloadQueueManager.findAll();
        if (downloadRequestList.size() > 0 && downloadRequestList.get(0).getDownloadId() == -1) {
            start();
        }
    }

    @Override
    public void start() {
        List<DownloadRequest> downloadRequestList = mDownloadQueueManager.findAll();
        if (downloadRequestList.size() > 0) {
            DownloadRequest downloadRequest = downloadRequestList.get(0);
            long downloadId = mAppContext.getDownloadManager().enqueue(new Request(downloadRequest.getDownloadUrl(), downloadRequest.getIdentifier(), downloadRequest.getMimeType()));
            mDownloadQueueManager.update(downloadRequest.getIdentifier(), downloadId);
        }
    }

    @Override
    public void onDownloadComplete(DownloadResponse downloadResponse) {
        start();
    }

    @Override
    public void onDownloadFailed(DownloadResponse downloadResponse) {
        mDownloadQueueManager.remove(downloadResponse.getDownloadId());
        start();
    }
}
