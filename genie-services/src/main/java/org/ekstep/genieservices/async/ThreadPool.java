package org.ekstep.genieservices.async;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Souvik on 16/06/17.
 */

public final class ThreadPool {

    private static final int RESULT_MESG = 0x23233;

    private static ThreadPool instance;
    private static Lock lock = new ReentrantLock();

    private ThreadPoolExecutor threadPoolExecutor;

    private ThreadPool() {
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
    }

    public static final ThreadPool getInstance() {
        if(instance == null) {
            lock.lock();
            try {
                if (instance == null)
                    instance = new ThreadPool();
                return instance;
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public void execute(final IPerformable performable,
                        final IResponseHandler responseHandler) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final GenieResponse response = performable.perform();
                if (Logger.isDebugEnabled()) {
                    Logger.d("GenieAsyncResponse", GsonUtil.toJson(response));
                }
                Handler handler = new MainThreadHandler(){
                    @Override
                    public void handleMessage(Message msg) {
                        if (response.getStatus()) {
                            responseHandler.onSuccess(response);
                        } else {
                            responseHandler.onError(response);
                        }
                    }
                };
                Message message = handler.obtainMessage(RESULT_MESG);
                message.sendToTarget();
            }
        };

        threadPoolExecutor.execute(runnable);
    }

    class MainThreadHandler extends Handler {

        MainThreadHandler() {
            super(Looper.getMainLooper());
        }

    }



}