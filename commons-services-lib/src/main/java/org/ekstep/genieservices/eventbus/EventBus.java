package org.ekstep.genieservices.eventbus;

import org.greenrobot.eventbus.util.AsyncExecutor;

/**
 * Created on 26/4/17.
 *
 * @author swayangjit
 */
public class EventBus {

    public static final String TAG = EventBus.class.getSimpleName();

    public static void postEvent(final Object eventObject) {
        EventPublisherThreadPool
                .getInstance()
                .execute(new AsyncExecutor.RunnableEx() {
                    @Override
                    public void run() throws Exception {
                        org.greenrobot.eventbus.EventBus.getDefault().post(eventObject);
                    }
                });
    }

    public static void postStickyEvent(final Object eventObject) {
        EventPublisherThreadPool
                .getInstance()
                .execute(new AsyncExecutor.RunnableEx() {
                    @Override
                    public void run() throws Exception {
                        org.greenrobot.eventbus.EventBus.getDefault().postSticky(eventObject);
                    }
                });
    }

    public static void removeStickyEvent(final Object eventObject) {
        EventPublisherThreadPool
                .getInstance()
                .execute(new AsyncExecutor.RunnableEx() {
                    @Override
                    public void run() throws Exception {
                        org.greenrobot.eventbus.EventBus.getDefault().removeStickyEvent(eventObject);
                    }
                });
    }


    public static void registerSubscriber(final Object subscriber) {
        org.greenrobot.eventbus.EventBus.getDefault().register(subscriber);
    }

    public static void unregisterSubscriber(final Object subscriber) {
        org.greenrobot.eventbus.EventBus.getDefault().unregister(subscriber);
    }

    public static boolean isSubscriberRegistered(final Object eventObject) {
        return org.greenrobot.eventbus.EventBus.getDefault().isRegistered(eventObject);
    }

}