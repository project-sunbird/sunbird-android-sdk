package org.ekstep.genieservices.commons.download;

import org.ekstep.genieservices.IDownloadService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IDownloadManager;
import org.ekstep.genieservices.commons.bean.CorrelationData;
import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.DownloadRequest;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.enums.InteractionType;
import org.ekstep.genieservices.commons.bean.telemetry.GEInteract;
import org.ekstep.genieservices.eventbus.EventPublisher;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

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

    private AppContext mAppContext;
    private DownloadQueueManager mDownloadQueueManager;
    private IDownloadManager mDownloadManager;
    private ScheduledExecutorService mExecutor;

    public DownloadServiceImpl(AppContext appContext) {
        this.mAppContext = appContext;
        this.mDownloadQueueManager = new DownloadQueueManager(mAppContext.getKeyValueStore());
        this.mDownloadManager = mAppContext.getDownloadManager();
        // TODO: 14/6/17  Figure it out later
//        resumeDownloads();
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
        //Assumption is that only 1 download happens at a time. This method will need refactoring whenever this assumption changes
        List<String> currentDownloads = mDownloadQueueManager.getCurrentDownloads();
        if (currentDownloads.size() == 0) {
            DownloadRequest request = mDownloadQueueManager.popDownloadrequest();
            if (request != null) {
                long downloadId = mDownloadManager.enqueue(request);
                TelemetryLogger.log(buildGEInteractEvent(InteractionType.TOUCH, ServiceConstants.Telemetry.CONTENT_DOWNLOAD_INITIATE, request.getCorrelationData(), request.getIdentifier()));
                mDownloadQueueManager.updateDownload(request.getIdentifier(), downloadId);
                mDownloadQueueManager.addToCurrentDownloadQueue(request.getIdentifier());
                startTrackingProgress(request.getIdentifier(), downloadId);
            }

        } else {
            DownloadRequest request = mDownloadQueueManager.getRequestByIdentifier(currentDownloads.get(0));
            if (request != null) {
                //make this more intelligent to detect if a download is not really working out. For now I am just checking if the request is not recognised by the download manager or if it is 100% done
                DownloadProgress progress = mDownloadManager.getProgress(request.getDownloadId());
                if (progress.getStatus() == IDownloadManager.UNKNOWN || progress.getStatus() == IDownloadManager.FAILED || progress.getStatus() == IDownloadManager.COMPLETED) {
                    //clear and restart the download. this means the ondownloadComplete did not fire for some reason
                    mDownloadManager.cancel(request.getDownloadId());
                    mDownloadQueueManager.removeFromCurrentDownloadQueue(request.getIdentifier());
                    mDownloadQueueManager.updateDownload(request.getIdentifier(), -1);
                    resumeDownloads();
                }
            } else {
                mDownloadQueueManager.removeFromCurrentDownloadQueue(request.getIdentifier());
            }
        }
    }

    private GEInteract buildGEInteractEvent(InteractionType type, String subType, List<CorrelationData> correlationDataList, String contendId) {
        GEInteract geInteract = new GEInteract.Builder(new GameData(mAppContext.getParams().getGid(), mAppContext.getParams().getVersionName()))
                .interActionType(type)
                .stageId(ServiceConstants.Telemetry.CONTENT_DETAIL)
                .subType(subType)
                .id(contendId)
                .correlationData(correlationDataList)
                .build();
        return geInteract;
    }


    private void startTrackingProgress(final String identifier, final long downloadId) {
        mExecutor = Executors.newScheduledThreadPool(1);
        mExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                DownloadProgress downloadProgress = mDownloadManager.getProgress(downloadId);
                downloadProgress.setIdentifier(identifier);
                if (downloadProgress.getStatus() == IDownloadManager.UNKNOWN || downloadProgress.getStatus() == IDownloadManager.FAILED) {
                    mExecutor.shutdown();
                } else if (downloadProgress.getStatus() == IDownloadManager.COMPLETED) {
                    EventPublisher.postDownloadProgress(downloadProgress);
                    mExecutor.shutdown();
                } else if (downloadProgress.getStatus() == IDownloadManager.NOT_STARTED) {
                    //do nothing
                } else if (downloadProgress.getStatus() == IDownloadManager.STARTED) {
                    EventPublisher.postDownloadProgress(downloadProgress);
                }

            }
        }, 2, 10, TimeUnit.SECONDS);
    }

    @Override
    public void cancel(String identifier) {
        DownloadRequest request = mDownloadQueueManager.getRequestByIdentifier(identifier);
        if (request != null) {
            if (request.getDownloadId() != -1) {
                mExecutor.shutdown();
                mDownloadManager.cancel(request.getDownloadId());
                mDownloadQueueManager.removeFromCurrentDownloadQueue(identifier);
            }
            mDownloadQueueManager.removeFromQueue(identifier);
        }
        resumeDownloads();
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
        DownloadRequest request = mDownloadQueueManager.getRequestByIdentifier(identifier);
        DownloadProgress progress = new DownloadProgress(-1);
        progress.setIdentifier(identifier);
        if (request != null) {
            if (request.getDownloadId() == -1) {
                progress.setStatus(0);
            } else {
                progress = mDownloadManager.getProgress(request.getDownloadId());
                if (progress.getStatus() == IDownloadManager.COMPLETED) {
                    progress.setDownloadPath(mDownloadManager.getDownloadPath(request.getDownloadId()));
                }
            }
        }
        return progress;
    }

    @Override
    public void onDownloadComplete(String identifier) {
        DownloadRequest request = mDownloadQueueManager.getRequestByIdentifier(identifier);
        TelemetryLogger.log(buildGEInteractEvent(InteractionType.OTHER, ServiceConstants.Telemetry.CONTENT_DOWNLOAD_SUCCESS, request.getCorrelationData(), request.getIdentifier()));
        mDownloadQueueManager.removeFromQueue(identifier);
        mDownloadQueueManager.removeFromCurrentDownloadQueue(identifier);
    }

    @Override
    public void onDownloadFailed(String identiifer) {
        mDownloadQueueManager.removeFromQueue(identiifer);
    }
}
