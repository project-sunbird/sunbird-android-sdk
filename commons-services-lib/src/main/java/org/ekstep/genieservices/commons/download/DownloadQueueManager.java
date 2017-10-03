package org.ekstep.genieservices.commons.download;

import com.google.gson.reflect.TypeToken;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.db.cache.IKeyValueStore;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created on 20/5/17.
 *
 * @author swayangjit
 */
public class DownloadQueueManager {

    private static final String CURRENT_DOWNLOAD = "current_download";

    private IKeyValueStore mKeyValueStore;
    private IDBSession mDbSession;
    private List<DownloadRequest> mDownloadRequestList;
    private List<String> mCurrentDownloads;

    public DownloadQueueManager(IKeyValueStore keyValueStore, IDBSession dbSession) {
        this.mDbSession = dbSession;
        this.mKeyValueStore = keyValueStore;
    }

    private List<DownloadRequest> getAllRequests() {
        if (CollectionUtil.isNullOrEmpty(mDownloadRequestList)) {
            mDownloadRequestList = new ArrayList<>();
            NoSqlModel noSqlModel = NoSqlModel.findByKey(mDbSession, ServiceConstants.DOWNLOAD_QUEUE);
            if (noSqlModel != null) {
                String jsonDownloadRequest = noSqlModel.getValue();
                if (!StringUtil.isNullOrEmpty(jsonDownloadRequest)) {
                    Type type = new TypeToken<List<DownloadRequest>>() {
                    }.getType();
                    List<DownloadRequest> reqList = GsonUtil.getGson().fromJson(jsonDownloadRequest, type);
                    mDownloadRequestList.addAll(reqList != null ? reqList : new ArrayList<DownloadRequest>());
                }
            }
        }
        return mDownloadRequestList;
    }


    public DownloadRequest popDownloadRequest() {
        DownloadRequest request = null;
        List<DownloadRequest> requestList = getAllRequests();
        if (requestList.size() > 0) {
            request = requestList.get(0);
        }
        return request;
    }

    public DownloadRequest getRequestByDownloadId(long downloadId) {
        List<DownloadRequest> requestList = getAllRequests();
        for (DownloadRequest request : requestList) {
            if (request.getDownloadId() == downloadId) {
                return request;
            }
        }
        return null;
    }

    public DownloadRequest getRequestByIdentifier(String identifier) {
        List<DownloadRequest> requestList = getAllRequests();
        for (DownloadRequest request : requestList) {
            if (identifier.equals(request.getIdentifier())) {
                return request;
            }
        }
        return null;
    }

    public void updateDownload(DownloadRequest downloadRequest) {
        List<DownloadRequest> downloadRequestList = getAllRequests();
        int index = downloadRequestList.indexOf(downloadRequest);
        if (index != -1) {
            downloadRequestList.set(index, downloadRequest);
            save(downloadRequestList);
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

    public void removeFromQueue(String identifier) {
        List<DownloadRequest> contentList = getAllRequests();
        for (int i = 0; i < contentList.size(); i++) {
            DownloadRequest downloadingRequest = contentList.get(i);
            if (identifier.equals(downloadingRequest.getIdentifier())) {
                contentList.remove(i);
                save(contentList);
            }
        }
    }


    public void save(Object requestList) {
        Collection collection = (Collection) requestList;
        String jsonContents = GsonUtil.getGson().toJson(requestList);

        if (CollectionUtil.isNullOrEmpty(collection)) {
            NoSqlModel noSqlModel = NoSqlModel.build(mDbSession, ServiceConstants.DOWNLOAD_QUEUE, jsonContents);
            noSqlModel.delete();
        } else {
            NoSqlModel noSqlModel = NoSqlModel.build(mDbSession, ServiceConstants.DOWNLOAD_QUEUE, jsonContents);
            NoSqlModel noSqlModelInDb = NoSqlModel.findByKey(mDbSession, ServiceConstants.DOWNLOAD_QUEUE);
            if (noSqlModelInDb == null) {
                noSqlModel.save();
            } else {
                noSqlModel.update();
            }
        }


    }


    public void addToQueue(DownloadRequest request) {
        List<DownloadRequest> requestList = getAllRequests();
        Set<DownloadRequest> downloadRequestSet = new LinkedHashSet<>(requestList);
        downloadRequestSet.add(request);
        mDownloadRequestList = new ArrayList<>(downloadRequestSet);
        save(downloadRequestSet);
    }

    public List<String> getCurrentDownloads() {
        if (CollectionUtil.isNullOrEmpty(mCurrentDownloads)) {
            mCurrentDownloads = new ArrayList<>();
            NoSqlModel noSqlModel = NoSqlModel.findByKey(mDbSession, CURRENT_DOWNLOAD);
            if (noSqlModel != null) {
                String jsonCurrentDownloads = noSqlModel.getValue();
                if (!StringUtil.isNullOrEmpty(jsonCurrentDownloads)) {
                    Type type = new TypeToken<List<String>>() {
                    }.getType();
                    List<String> reqList = GsonUtil.getGson().fromJson(jsonCurrentDownloads, type);
                    mCurrentDownloads.addAll(reqList != null ? reqList : new ArrayList<String>());
                }
            }
        }

        return mCurrentDownloads;
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
        String currentDownloads = GsonUtil.getGson().toJson(identifiers);
        if (CollectionUtil.isNullOrEmpty(identifiers)) {
            NoSqlModel noSqlModel = NoSqlModel.build(mDbSession, CURRENT_DOWNLOAD, currentDownloads);
            noSqlModel.delete();
        } else {
            NoSqlModel noSqlModelinDb = NoSqlModel.findByKey(mDbSession, CURRENT_DOWNLOAD);
            NoSqlModel noSqlModel = NoSqlModel.build(mDbSession, CURRENT_DOWNLOAD, currentDownloads);
            if (noSqlModelinDb == null) {
                noSqlModel.save();
            } else {
                noSqlModel.update();
            }
        }

    }

    public boolean shouldResume() {
        return mKeyValueStore.getInt(ServiceConstants.PreferenceKey.KEY_DOWNLOAD_STATUS, 0) == 0;
    }
}
