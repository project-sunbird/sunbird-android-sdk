package org.ekstep.genieservices.eventbus;

import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.DownloadProgress;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.util.AsyncExecutor;

/**
 * Created on 26/4/17.
 *
 * @author swayangjit
 */
public class EventPublisher {

    public static final String TAG = EventPublisher.class.getSimpleName();

    public static void postTelemetryEvent(final Telemetry telemetry) {
        EventPublisherThreadPool
                .getInstance()
                .execute(new AsyncExecutor.RunnableEx() {
                    @Override
                    public void run() throws Exception {
                        EventBus.getDefault().post(telemetry);
                    }
                });
    }

    public static void postDownloadProgress(final DownloadProgress downloadProgress) {
        EventPublisherThreadPool
                .getInstance()
                .execute(new AsyncExecutor.RunnableEx() {
                    @Override
                    public void run() throws Exception {
                        EventBus.getDefault().post(downloadProgress);
                    }
                });
    }

    public static void postContentImportSuccessful(final ContentImportResponse contentImportResponse) {
        EventPublisherThreadPool
                .getInstance()
                .execute(new AsyncExecutor.RunnableEx() {
                    @Override
                    public void run() throws Exception {
                        EventBus.getDefault().post(contentImportResponse);
                    }
                });
    }
}