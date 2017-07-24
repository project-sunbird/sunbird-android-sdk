package org.ekstep.genieservices.commons;

import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.DownloadRequest;

/**
 * Created on 17/5/17.
 *
 * @author swayangjit
 */
public interface IDownloadManager {

    int UNKNOWN = -1;
    int NOT_STARTED = 0;
    int STARTED = 1;
    int COMPLETED = 2;
    int FAILED = 3;

    void enqueue(DownloadRequest request);

    DownloadProgress getProgress(long downloadId);

    void cancel(long downloadId);
}
