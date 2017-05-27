package org.ekstep.genieservices.commons;

import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.Request;

/**
 * Created by swayangjit on 17/5/17.
 */

public interface IDownloadManager {

    long enqueue(Request request);

    DownloadProgress query(long downloadId);

    void cancel(long downloadId);
}
