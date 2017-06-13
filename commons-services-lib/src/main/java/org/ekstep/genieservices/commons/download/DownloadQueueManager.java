package org.ekstep.genieservices.commons.download;

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

    private static final String DOWNLOAD_QUEUE = "download_queue";
    private static final String CURRENT_DOWNLOAD = "current_download";

    private IKeyValueStore mKeyValueStore;

    public DownloadQueueManager(IKeyValueStore keyValueStore) {
        this.mKeyValueStore = keyValueStore;
    }

    public List<DownloadRequest> getAllRequests() {
        List<DownloadRequest> requestList;
        String jsonContents = mKeyValueStore.getString(DOWNLOAD_QUEUE, null);
        if (!StringUtil.isNullOrEmpty(jsonContents)) {
            DownloadRequest[] contentItems = GsonUtil.fromJson(jsonContents, DownloadRequest[].class);
            requestList = Arrays.asList(contentItems);
        } else {
            requestList = new ArrayList<>();
        }
        return requestList;
    }

    public DownloadRequest popDownloadrequest() {
        DownloadRequest request = null;
        List<DownloadRequest> requestList = getAllRequests();
        if(requestList.size() > 0) {
            request = requestList.get(0);
        }
        return request;
    }

    public DownloadRequest getRequestByDownloadId(long downloadId) {
        List<DownloadRequest> requestList = getAllRequests();
        for (DownloadRequest request: requestList) {
            if (request.getDownloadId() == downloadId) {
                return request;
            }
        }
        return null;
    }

    public DownloadRequest getRequestByIdentifier(String identifier) {
        List<DownloadRequest> requestList = getAllRequests();
        for (DownloadRequest request: requestList) {
            if (identifier.equals(request.getIdentifier())) {
                return request;
            }
        }
        return null;
    }

    public void updateDownload(String identifier, long downloadId) {
        List<DownloadRequest> contentList = getAllRequests();
        for (DownloadRequest request: contentList) {
            if (identifier.equals(request.getIdentifier())) {
                request.setDownloadId(downloadId);
                save(contentList);
            }
        }
    }

    public void removeFromQueue(long downloadId) {
        List<DownloadRequest> contentList = getAllRequests();
        for (int i = 0; i < contentList.size(); i++) {
            DownloadRequest downloadingRequest = contentList.get(i);
            if (downloadingRequest.getDownloadId() == downloadId) {
                contentList.remove(i);
                save(contentList);
            }
        }
    }

    public void removeFromQueue(String identififer) {
        List<DownloadRequest> contentList = getAllRequests();
        for (int i = 0; i < contentList.size(); i++) {
            DownloadRequest downloadingRequest = contentList.get(i);
            if (identififer.equals(downloadingRequest.getIdentifier())) {
                contentList.remove(i);
                save(contentList);
            }
        }
    }

    public void save(List<DownloadRequest> requestList) {
        String jsonContents = GsonUtil.getGson().toJson(requestList);
        mKeyValueStore.putString(DOWNLOAD_QUEUE, jsonContents);
    }

    public void addToQueue(DownloadRequest request) {
        List<DownloadRequest> requestList = getAllRequests();
        requestList.add(request);
        save(requestList);
    }

    public List<String> getCurrentDownloads() {
        List<String> downloadingList;
        String jsonContents = mKeyValueStore.getString(CURRENT_DOWNLOAD, null);
        if (!StringUtil.isNullOrEmpty(jsonContents)) {
            String[] items = GsonUtil.fromJson(jsonContents, String[].class);
            downloadingList = Arrays.asList(items);
        } else {
            downloadingList = new ArrayList<>();
        }
        return downloadingList;
    }

    public void addToCurrentDownloadQueue(String identifier) {
        List<String> currentlyDownloading = getCurrentDownloads();
        currentlyDownloading.add(identifier);
        saveCurrentDownloads(currentlyDownloading);
    }

    public void removeFromCurrentDownloadQueue(String identifier) {
        List<String> currentlyDownloading = getCurrentDownloads();
        for (String currentItem: currentlyDownloading) {
            if (identifier.equals(currentItem)) {
                currentlyDownloading.remove(currentItem);
                saveCurrentDownloads(currentlyDownloading);
            }
        }
    }

    public void saveCurrentDownloads(List<String> identifiers) {
        String jsonContents = GsonUtil.getGson().toJson(identifiers);
        mKeyValueStore.putString(CURRENT_DOWNLOAD, jsonContents);
    }

}
