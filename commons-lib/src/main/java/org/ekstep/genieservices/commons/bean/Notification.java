package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created on 6/12/2017.
 *
 * @author Indraja Machani
 */
public class Notification implements Serializable {

    private double msgId;
    private long expiryTime;
    private String displayTime;
    private Date receivedAt;
    private String notificationJson;
    private String status;

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

    public double getMsgId() {
        return msgId;
    }

    public void setMsgId(double msgId) {
        this.msgId = msgId;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(String displayTime) {
        this.displayTime = displayTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
