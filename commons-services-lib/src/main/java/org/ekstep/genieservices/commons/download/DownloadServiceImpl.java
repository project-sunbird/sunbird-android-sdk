package org.ekstep.genieservices.commons.download;

import org.ekstep.genieservices.IDownloadService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IDownloadManager;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.CorrelationData;
import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.bean.enums.ContentImportStatus;
import org.ekstep.genieservices.commons.bean.enums.InteractionType;
import org.ekstep.genieservices.commons.bean.telemetry.GEInteract;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.eventbus.EventBus;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created on 17/5/17.
 *
 * @author swayangjit
 */
public class DownloadServiceImpl implements IDownloadService {

    private DownloadQueueManager mDownloadQueueManager;
    private IDownloadManager mDownloadManager;
    private ScheduledExecutorService mExecutor;

    public DownloadServiceImpl(AppContext appContext) {
        this.mDownloadQueueManager = new DownloadQueueManager(appContext.getKeyValueStore(), appContext.getDBSession());
        this.mDownloadManager = appContext.getDownloadManager();
    }

    @Override
    public void enqueue(DownloadRequest... downloadRequest) {
        if (downloadRequest.length > 0) {
            for (DownloadRequest request : downloadRequest) {
                mDownloadQueueManager.addToQueue(request);
            }
        }
        resumeDownloads();
    }

    @Override
    public void resumeDownloads() {
        if (mDownloadQueueManager.shouldResume()) {
            resume();
        }

    }

    @Override
    public void cancel(String identifier) {
        DownloadRequest request = mDownloadQueueManager.getRequestByIdentifier(identifier);
        if (request != null) {
            TelemetryLogger.log(buildGEInteractEvent(InteractionType.TOUCH, ServiceConstants.Telemetry.CONTENT_DOWNLOAD_CANCEL, request.getCorrelationData(), request.getIdentifier()));
            if (request.getDownloadId() != -1) {
                if (mExecutor != null) {
                    mExecutor.shutdown();
                }
            }
            resetDownload(request.getDownloadId(), true, false);
        }
        resumeDownloads();
    }

    @Override
    public void removeDownloadedFile(long downloadId) {
        resetDownload(downloadId, true, true);
    }

    @Override
    public DownloadRequest getDownloadRequest(String identifier) {
        return mDownloadQueueManager.getRequestByIdentifier(identifier);
    }

    @Override
    public DownloadRequest getDownloadRequest(long downloadId) {
        return mDownloadQueueManager.getRequestByDownloadId(downloadId);
    }

    @Override
    public DownloadProgress getProgress(String identifier) {
        DownloadProgress progress = new DownloadProgress(-1);
        progress.setIdentifier(identifier);

        DownloadRequest request = mDownloadQueueManager.getRequestByIdentifier(identifier);
        if (request != null) {
            if (request.getDownloadId() == -1) {
                progress.setStatus(0);
            } else {
                progress = mDownloadManager.getProgress(request.getDownloadId());
            }
        }
        return progress;
    }

    @Override
    public void onDownloadComplete(String identifier) {
        EventBus.postEvent(new ContentImportResponse(identifier, ContentImportStatus.DOWNLOAD_COMPLETED));
        DownloadRequest request = mDownloadQueueManager.getRequestByIdentifier(identifier);
        TelemetryLogger.log(buildGEInteractEvent(InteractionType.OTHER, ServiceConstants.Telemetry.CONTENT_DOWNLOAD_SUCCESS, request.getCorrelationData(), request.getIdentifier()));
    }

    @Override
    public void onDownloadFailed(String identifier) {
        EventBus.postEvent(new ContentImportResponse(identifier, ContentImportStatus.DOWNLOAD_FAILED));
        DownloadRequest request = mDownloadQueueManager.getRequestByIdentifier(identifier);
        resetDownload(request.getDownloadId(), true, true);
        mDownloadQueueManager.removeFromQueue(identifier);
    }

    private GEInteract buildGEInteractEvent(InteractionType type, String subType, List<CorrelationData> correlationDataList, String contendId) {
        GEInteract.Builder geInteract = new GEInteract.Builder();
        geInteract.interActionType(type)
                .stageId(ServiceConstants.Telemetry.CONTENT_DETAIL)
                .subType(subType)
                .id(contendId)
                .correlationData(correlationDataList);
        return geInteract.build();
    }


    /**
     * Assumption is that only 1 download happens at a time. This method will need refactoring whenever this assumption changes
     */
    private synchronized void resume() {
        List<String> currentDownloads = mDownloadQueueManager.getCurrentDownloads();
        if (CollectionUtil.isNullOrEmpty(currentDownloads)) {
            DownloadRequest request = mDownloadQueueManager.popDownloadRequest();
            if (request != null) {
                TelemetryLogger.log(buildGEInteractEvent(InteractionType.TOUCH, ServiceConstants.Telemetry.CONTENT_DOWNLOAD_INITIATE, request.getCorrelationData(), request.getIdentifier()));
                long downloadId = mDownloadManager.enqueue(request);
                request.setDownloadId(downloadId);
                mDownloadQueueManager.updateDownload(request);
                mDownloadQueueManager.addToCurrentDownloadQueue(request.getIdentifier());
                startTrackingProgress(request);
            }
        } else {
            DownloadRequest request = mDownloadQueueManager.getRequestByIdentifier(currentDownloads.get(0));
            if (request != null) {
                //make this more intelligent to detect if a download is not really working out.
                // For now I am just checking if the request is not recognised by the download manager or if it is 100% done.
                DownloadProgress progress = mDownloadManager.getProgress(request.getDownloadId());
                if (progress.getStatus() == IDownloadManager.UNKNOWN || progress.getStatus() == IDownloadManager.FAILED) {
                    //clear and restart the download. this means the onDownloadComplete did not fire for some reason.
                    resetDownload(request.getDownloadId(), false, true);
                } else if (progress.getStatus() == IDownloadManager.COMPLETED) {
                    // reset download if its download time is more than 1hr.
                    if (!StringUtil.isNullOrEmpty(request.getDownloadedFilePath())) {
                        long lastModified = new File(request.getDownloadedFilePath()).lastModified();
                        if (DateUtil.getTimeDifferenceInHours(lastModified, DateUtil.getEpochTime()) > 1) {
                            resetDownload(request.getDownloadId(), false, true);
                        }
                    }
                }
            }
        }
    }

    private void startTrackingProgress(final DownloadRequest request) {
        mExecutor = Executors.newScheduledThreadPool(1);
        mExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                DownloadProgress downloadProgress = mDownloadManager.getProgress(request.getDownloadId());
                downloadProgress.setIdentifier(request.getIdentifier());
                if (downloadProgress.getStatus() == IDownloadManager.UNKNOWN || downloadProgress.getStatus() == IDownloadManager.FAILED) {
                    mExecutor.shutdown();
                } else if (downloadProgress.getStatus() == IDownloadManager.COMPLETED) {
                    EventBus.postEvent(downloadProgress);
                    mExecutor.shutdown();
                } else if (downloadProgress.getStatus() == IDownloadManager.NOT_STARTED) {
                    //do nothing
                } else if (downloadProgress.getStatus() == IDownloadManager.STARTED) {
                    EventBus.postEvent(downloadProgress);
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void resetDownload(long downloadId, boolean removeFromQueue, boolean resumeDownload) {
        DownloadRequest downloadRequest = mDownloadQueueManager.getRequestByDownloadId(downloadId);
        if (downloadRequest != null) {
            // Delete the downloaded file.
            if (!StringUtil.isNullOrEmpty(downloadRequest.getDownloadedFilePath())) {
                FileUtil.rm(new File(downloadRequest.getDownloadedFilePath()));
            }

            if (removeFromQueue) {
                mDownloadQueueManager.removeFromQueue(downloadRequest.getIdentifier());
            } else {
                downloadRequest.setDownloadId(-1);
                mDownloadQueueManager.updateDownload(downloadRequest);
            }
            mDownloadQueueManager.removeFromCurrentDownloadQueue(downloadRequest.getIdentifier());
        }

        mDownloadManager.cancel(downloadId);

        if (resumeDownload) {
            resumeDownloads();
        }
    }
}
