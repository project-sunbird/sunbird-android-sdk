package org.ekstep.genieservices.commons.download;

import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.db.cache.IKeyValueStore;
import org.ekstep.genieservices.commons.db.model.KeyValueStoreModel;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created on 20/5/17.
 *
 * @author swayangjit
 */
public class DownloadQueueManagerNew {

    private static final String DOWNLOAD_QUEUE = "download_queue";
    private static final String CURRENT_DOWNLOAD = "current_download";

    private IKeyValueStore mKeyValueStore;
    private IDBSession mDbSession;
    private Set<DownloadRequest> mDownloadRequestList;

    public DownloadQueueManagerNew(IKeyValueStore keyValueStore) {
        this.mKeyValueStore = keyValueStore;
    }

    public DownloadQueueManagerNew(IDBSession dbSession) {
        this.mDbSession = dbSession;
    }

    private Set<DownloadRequest> getAllRequests() {
        if (mDownloadRequestList == null) {
            mDownloadRequestList = new LinkedHashSet<>();
            KeyValueStoreModel keyStoreModel = KeyValueStoreModel.findByKey(mDbSession, DOWNLOAD_QUEUE);
            if (keyStoreModel != null) {
                String jsonContents = keyStoreModel.getValue();
                if (!StringUtil.isNullOrEmpty(jsonContents)) {
                    DownloadRequest[] contentItems = GsonUtil.fromJson(jsonContents, DownloadRequest[].class);
                    mDownloadRequestList.addAll(Arrays.asList(contentItems));
                }
            }
        }
        return mDownloadRequestList;
    }

//    private List<DownloadRequest> getAllRequests() {
//        List<DownloadRequest> requestList = new ArrayList<>();
//        String jsonContents = mKeyValueStore.getString(DOWNLOAD_QUEUE, null);
//        if (!StringUtil.isNullOrEmpty(jsonContents)) {
//            DownloadRequest[] contentItems = GsonUtil.fromJson(jsonContents, DownloadRequest[].class);
//            requestList.addAll(Arrays.asList(contentItems));
//        }
//        return requestList;
//    }

    public DownloadRequest popDownloadRequest() {
        DownloadRequest request = null;
        Set<DownloadRequest> requestList = getAllRequests();
        if (!CollectionUtil.isNullOrEmpty(requestList)) {
            request = requestList.iterator().next();
        }
        return request;
    }

    public DownloadRequest getRequestByDownloadId(long downloadId) {
        Set<DownloadRequest> requestList = getAllRequests();
        if (!CollectionUtil.isNullOrEmpty(requestList)) {
            Iterator<DownloadRequest> setIterator = requestList.iterator();
            while (setIterator.hasNext()) {
                DownloadRequest downloadRequest = setIterator.next();
                if (downloadRequest.getDownloadId() == downloadId) {
                    return downloadRequest;
                }
            }
        }
        return null;
    }

    public DownloadRequest getRequestByIdentifier(String identifier) {
        Set<DownloadRequest> requestList = getAllRequests();
        if (!CollectionUtil.isNullOrEmpty(requestList)) {
            Iterator<DownloadRequest> setIterator = requestList.iterator();
            while (setIterator.hasNext()) {
                DownloadRequest downloadRequest = setIterator.next();
                if (identifier.equals(downloadRequest.getIdentifier())) {
                    return downloadRequest;
                }
            }
        }
        return null;
    }

    // TODO: 10/9/17  Need to improve on set to list conversion
    public void updateDownload(DownloadRequest downloadRequest) {
        Set<DownloadRequest> downloadRequestSet = getAllRequests();
        List<DownloadRequest> downloadRequestList = new ArrayList<>(downloadRequestSet);
        int index = downloadRequestList.indexOf(downloadRequest);
        if (index != -1) {
            downloadRequestList.set(index, downloadRequest);
            save(new LinkedHashSet<>(downloadRequestList));
        }
    }

    public void removeFromQueue(long downloadId) {
        Set<DownloadRequest> requestList = getAllRequests();
        if (!CollectionUtil.isNullOrEmpty(requestList)) {
            Iterator<DownloadRequest> setIterator = requestList.iterator();
            while (setIterator.hasNext()) {
                DownloadRequest downloadRequest = setIterator.next();
                if (downloadRequest.getDownloadId() == downloadId) {
                    setIterator.remove();
                    save(requestList);
                }
            }
        }
    }

    public void removeFromQueue(String identifier) {
        Set<DownloadRequest> requestList = getAllRequests();
        if (!CollectionUtil.isNullOrEmpty(requestList)) {
            Iterator<DownloadRequest> setIterator = requestList.iterator();
            while (setIterator.hasNext()) {
                DownloadRequest downloadRequest = setIterator.next();
                if (identifier.equals(downloadRequest.getIdentifier())) {
                    setIterator.remove();
                    save(requestList);
                }
            }
        }
    }


    public void save(Set<DownloadRequest> requestList) {
        String jsonContents = GsonUtil.getGson().toJson(requestList);
        KeyValueStoreModel.build(mDbSession, DOWNLOAD_QUEUE, jsonContents);
    }


    public void addToQueue(DownloadRequest request) {
        Set<DownloadRequest> requestList = getAllRequests();
        requestList.add(request);
        save(requestList);
    }

    public List<String> getCurrentDownloads() {
        List<String> downloadingList = new ArrayList<>();
        String jsonContents = mKeyValueStore.getString(CURRENT_DOWNLOAD, null);
        if (!StringUtil.isNullOrEmpty(jsonContents)) {
            String[] items = GsonUtil.fromJson(jsonContents, String[].class);
            downloadingList.addAll(Arrays.asList(items));
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
        for (String currentItem : currentlyDownloading) {
            if (identifier.equals(currentItem)) {
                currentlyDownloading.remove(currentItem);
                saveCurrentDownloads(currentlyDownloading);
            }
        }
    }

    private void saveCurrentDownloads(List<String> identifiers) {
        String jsonContents = GsonUtil.getGson().toJson(identifiers);
        mKeyValueStore.putString(CURRENT_DOWNLOAD, jsonContents);
    }

}
