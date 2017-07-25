package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.DownloadRequest;

/**
 * This is the interface with all the required APIs for performing download content.
 */
public interface IDownloadService {

    void enqueue(DownloadRequest... downloadRequest);

    void resumeDownloads();

    void cancel(String identifier);

    void removeDownloadedFile(long downloadId);

    DownloadRequest getDownloadRequest(String identifier);

    DownloadRequest getDownloadRequest(long downloadId);

    DownloadProgress getProgress(String identifier);

    void onDownloadComplete(String identifier);

    void onDownloadFailed(String identifier);
}
