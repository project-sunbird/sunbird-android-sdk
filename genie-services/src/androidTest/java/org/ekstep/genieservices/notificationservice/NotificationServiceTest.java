package org.ekstep.genieservices.notificationservice;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.SampleResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Notification;
import org.ekstep.genieservices.commons.bean.NotificationFilterCriteria;
import org.ekstep.genieservices.commons.bean.enums.NotificationStatus;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sneha on 9/8/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class NotificationServiceTest extends GenieServiceTestBase {
    private static final String TAG = NotificationServiceTest.class.getSimpleName();

    @Override
    public void setup() throws IOException {
        super.setup();
        GenieServiceDBHelper.clearNotificationTableDBEntry();
    }

    @Override
    public void tearDown() throws IOException {
        super.tearDown();
    }

    @Test
    public void _11shouldAddNotification() {

        String notificationJson = SampleResponse.getNotification();
        Notification notification = GsonUtil.fromJson(notificationJson, Notification.class);

        GenieResponse<Void> genieResponse = activity.addNotification(notification);
        Assert.assertTrue(genieResponse.getStatus());
    }

    @Test
    public void _12shouldUpdateThenotificationIfSameNotificationisAdded() {

        String notificationJson = SampleResponse.getNotification();
        Notification notification = GsonUtil.fromJson(notificationJson, Notification.class);

        GenieResponse<Void> genieResponse = activity.addNotification(notification);
        Assert.assertTrue(genieResponse.getStatus());
        GenieResponse<Void> genieResponse1 = activity.addNotification(notification);
        Assert.assertTrue(genieResponse1.getStatus());
    }

    @Test
    public void _13shouldValidateAddNotificationForNull() {
        GenieResponse<Void> genieResponse = activity.addNotification(null);
        Assert.assertFalse(genieResponse.getStatus());
        Assert.assertEquals("Failed to add/update  notification", genieResponse.getErrorMessages().get(0));
    }

    @Test
    public void _22shouldUpdateNotification() {

        String notificationJson = SampleResponse.getNotification();
        Notification notification = GsonUtil.fromJson(notificationJson, Notification.class);

        GenieResponse<Void> genieResponse = activity.addNotification(notification);
        Assert.assertTrue(genieResponse.getStatus());

        String updatedNotificationJson = SampleResponse.getUpdatedNotification();
        Notification notification2 = GsonUtil.fromJson(updatedNotificationJson, Notification.class);

        GenieResponse<Notification> updateResponse = activity.updateNotification(notification2);
        Assert.assertTrue(updateResponse.getStatus());

    }

    @Test
    public void _23shouldUpdateAllNotification() {

        String notificationJson = SampleResponse.getNotification();
        Notification notification = GsonUtil.fromJson(notificationJson, Notification.class);

        GenieResponse<Void> genieResponse = activity.addNotification(notification);
        Assert.assertTrue(genieResponse.getStatus());

        String updatedNotificationJson = SampleResponse.getUpdatedNotification();
        Notification notification2 = GsonUtil.fromJson(updatedNotificationJson, Notification.class);
        notification2.setMsgid(-1);
        GenieResponse<Notification> updateResponse = activity.updateNotification(notification2);
        Assert.assertTrue(updateResponse.getStatus());

    }

    @Test
    public void _33shouldDeleteNotification() {

        String notificationJson = SampleResponse.getNotification();
        Notification notification = GsonUtil.fromJson(notificationJson, Notification.class);

        //add notification
        GenieResponse<Void> genieResponse = activity.addNotification(notification);
        Assert.assertTrue(genieResponse.getStatus());

        //delete notification
        GenieResponse deleteResponse = activity.deleteNotification(1);
        Assert.assertTrue(deleteResponse.getStatus());

    }

    @Test
    public void _34shouldValidateDeleteNotification() {
        String notificationJson = SampleResponse.getNotification();
        Notification notification = GsonUtil.fromJson(notificationJson, Notification.class);

        //add notification
        GenieResponse<Void> genieResponse = activity.addNotification(notification);
        Assert.assertTrue(genieResponse.getStatus());

        //delete notification
        GenieResponse deleteResponse = activity.deleteNotification(2);
        Assert.assertFalse(deleteResponse.getStatus());
        Assert.assertEquals("Failed to delete notification", deleteResponse.getErrorMessages().get(0));
    }

    @Test
    public void _44shouldGetAllNotifications() {
        String notificationJson = SampleResponse.getNotification();
        Notification notification = GsonUtil.fromJson(notificationJson, Notification.class);

        //add notification
        GenieResponse<Void> genieResponse = activity.addNotification(notification);
        Assert.assertTrue(genieResponse.getStatus());

        NotificationFilterCriteria.Builder filterCriteria = new NotificationFilterCriteria.Builder()
                .notificationStatus(NotificationStatus.ALL);

//        get all notification
        GenieResponse<List<Notification>> getAllResponse = activity.getAllNotifications(filterCriteria.build());
        Assert.assertTrue(getAllResponse.getStatus());

        Assert.assertEquals(1, getAllResponse.getResult().get(0).getMsgid());
    }

    @Test
    public void _45shouldGetZeroNotificationsIfNoNotificationsAvailable() {

        NotificationFilterCriteria.Builder filterCriteria = new NotificationFilterCriteria.Builder()
                .notificationStatus(NotificationStatus.ALL);

//        get all notification
        GenieResponse<List<Notification>> getAllResponse = activity.getAllNotifications(filterCriteria.build());
        Assert.assertTrue(getAllResponse.getStatus());

        Assert.assertEquals(0, getAllResponse.getResult().size());
    }

    @Test
    public void _45shouldGetNotificationsDependingUponStatus() {

        NotificationFilterCriteria.Builder filterCriteria = new NotificationFilterCriteria.Builder()
                .notificationStatus(NotificationStatus.READ);

//        get all notification
        GenieResponse<List<Notification>> getAllResponse = activity.getAllNotifications(filterCriteria.build());
        Assert.assertTrue(getAllResponse.getStatus());

        Assert.assertEquals(0, getAllResponse.getResult().size());

        NotificationFilterCriteria.Builder filterCriteria1 = new NotificationFilterCriteria.Builder()
                .notificationStatus(NotificationStatus.UNREAD);

//        get all notification
        GenieResponse<List<Notification>> getAllResponse1 = activity.getAllNotifications(filterCriteria1.build());
        Assert.assertTrue(getAllResponse1.getStatus());

        Assert.assertEquals(0, getAllResponse1.getResult().size());
    }
}
