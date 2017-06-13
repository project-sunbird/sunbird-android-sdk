package org.ekstep.genieservices.commons;

import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.bean.Request;

/**
 * Created by swayangjit on 17/5/17.
 */

public interface IDownloadManager {

    final int UNKNOWN = -1;
    final int NOT_STARTED = 0;
    final int STARTED = 1;
    final int COMPLETED = 2;
    final int FAILED = 3;

    long enqueue(DownloadRequest request);

    DownloadProgress getProgress(long downloadId);

    void cancel(long downloadId);

    String getDownloadPath(long downloadId);
}
