package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created on 6/12/2017.
 *
 * @author Indraja Machani
 */
public class Notification implements Serializable {

    private double mMsgId;
    private long mExpiryTime;
    private String mDisplayTime;
    private Date receivedAt;
    private String notificationJson;

    public double getmMsgId() {
        return mMsgId;
    }

    public void setmMsgId(double mMsgId) {
        this.mMsgId = mMsgId;
    }

    public long getmExpiryTime() {
        return mExpiryTime;
    }

    public void setmExpiryTime(long mExpiryTime) {
        this.mExpiryTime = mExpiryTime;
    }

    public String getmDisplayTime() {
        return mDisplayTime;
    }

    public void setmDisplayTime(String mDisplayTime) {
        this.mDisplayTime = mDisplayTime;
    }

    public Date getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Date receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String getNotificationJson() {
        return notificationJson;
    }

    public void setNotificationJson(String notificationJson) {
        this.notificationJson = notificationJson;
    }
}
