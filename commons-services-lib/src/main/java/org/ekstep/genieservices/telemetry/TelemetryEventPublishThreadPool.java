package org.ekstep.genieservices.telemetry;

import org.greenrobot.eventbus.util.AsyncExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by swayangjit on 26/4/17.
 */

public class TelemetryEventPublishThreadPool {

    public static final int MAX_THREADS = 30;
    private static volatile AsyncExecutor asyncExecutor;

    private TelemetryEventPublishThreadPool() {
    }

    public static AsyncExecutor getInstance() {
        if (asyncExecutor == null) {
            synchronized (TelemetryEventPublishThreadPool.class) {
                if (asyncExecutor == null) {
                    AsyncExecutor.Builder builder = AsyncExecutor.builder();
                    ExecutorService threadPool =
                            Executors.newFixedThreadPool(MAX_THREADS);
                    builder.threadPool(threadPool);
                    asyncExecutor = builder.build();
                }
            }
        }
        return asyncExecutor;
    }
}
