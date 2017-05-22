package org.ekstep.genieservices.util;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.db.cache.IKeyValueStore;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 20/5/17.
 *
 * @author swayangjit
 */
public class DownloadQueueManager {
    private IKeyValueStore mKeyValueStore;

    public DownloadQueueManager(IKeyValueStore keyValueStore) {
        this.mKeyValueStore = keyValueStore;
    }

    public List<DownloadRequest> findAll() {
        List<DownloadRequest> requestList;
        String jsonContents = mKeyValueStore.getString(ServiceConstants.PreferenceKey.DOWNLOAD_QUEUE, null);
        if (!StringUtil.isNullOrEmpty(jsonContents)) {
            DownloadRequest[] contentItems = GsonUtil.fromJson(jsonContents, DownloadRequest[].class);
            requestList = Arrays.asList(contentItems);
            requestList = new ArrayList<>(requestList);
        } else {
            requestList = new ArrayList<>();
        }
        return requestList;
    }

    public DownloadRequest getRequest(long downloadId) {
        List<DownloadRequest> requestList = findAll();

        if (requestList != null) {
            for (int i = 0; i < requestList.size(); i++) {
                DownloadRequest downloadingRequest = requestList.get(i);
                if (downloadingRequest.getDownloadId() == downloadId) {
                    return downloadingRequest;
                }
            }
        }
        return null;
    }

    public void update(String identifier, long downloadId) {
        List<DownloadRequest> contentList = findAll();

        if (contentList != null) {
            for (int i = 0; i < contentList.size(); i++) {
                DownloadRequest downloadRequest = contentList.get(i);
                if (downloadRequest.getIdentifier().equalsIgnoreCase(identifier)) {
                    downloadRequest.setDownloadId(downloadId);
                    save(contentList);
                }
            }
        }
    }

    public void remove(long downloadId) {
        List<DownloadRequest> contentList = findAll();

        if (contentList != null) {
            for (int i = 0; i < contentList.size(); i++) {
                DownloadRequest downloadingRequest = contentList.get(i);
                if (downloadingRequest.getDownloadId() == downloadId) {
                    contentList.remove(i);
                    save(contentList);
                }
            }
        }
    }

    public void save(List<DownloadRequest> requestList) {
        String jsonContents = GsonUtil.getGson().toJson(requestList);
        mKeyValueStore.putString(ServiceConstants.PreferenceKey.DOWNLOAD_QUEUE, jsonContents);
    }

    public void save(DownloadRequest request) {
        List<DownloadRequest> requestList = findAll();
        requestList.add(request);
        save(requestList);
    }
}
