package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.bean.DownloadResponse;

/**
 * Created by swayangjit on 17/5/17.
 */

public interface IDownloadService {

    void enqueue(DownloadRequest... downloadRequest);

    void cancel(String identifier);

    DownloadRequest getDownloadRequest(String identifier);

    DownloadRequest getDownloadRequest(long downloadId);

    DownloadProgress getProgress(String identifier);

    void onDownloadComplete(DownloadResponse downloadResponse);

    void onDownloadFailed(DownloadResponse downloadResponse);
}
