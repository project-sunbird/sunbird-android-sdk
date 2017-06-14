package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.DownloadRequest;

/**
 * Created by swayangjit on 17/5/17.
 */

public interface IDownloadService {

    void enqueue(DownloadRequest... downloadRequest);

    void resumeDownloads();

    void cancel(String identifier);

    DownloadRequest getDownloadRequest(String identifier);

    DownloadRequest getDownloadRequest(long downloadId);

    DownloadProgress getProgress(String identifier);

    void onDownloadComplete(String identiifer);

    void onDownloadFailed(String identiifer);
}
