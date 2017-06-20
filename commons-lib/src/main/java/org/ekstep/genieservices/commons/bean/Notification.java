package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created on 6/12/2017.
 *
 * @author Indraja Machani
 */
public class Notification implements Serializable {

    private long expiryTime;
    private String displayTime;
    private Date receivedAt;
    private String notificationJson;
    private String status;

    private int msgid;
    private String title;
    private String msg;
    private int relativetime;   // Time in hours

    private String icon;
    private String time;        // dd-mm-yyyy:hh.mm.ss  //time at which Genie should show the notification
    private int validity;       // In minutes
    private int actionid;       // Value from pre-defined enumeration of actions
    private ActionData actiondata;
    private String dispbehavior;
    private int isRead;

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
        return msgid;
    }

    public void setMsgId(int msgid) {
        this.msgid = msgid;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getRelativetime() {
        return relativetime;
    }

    public void setRelativetime(int relativetime) {
        this.relativetime = relativetime;
    }

    public int getActionid() {
        return actionid;
    }

    public void setActionid(int actionid) {
        this.actionid = actionid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getValidity() {
        return validity;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }

    public ActionData getActiondata() {
        return actiondata;
    }

    public void setActiondata(ActionData actiondata) {
        this.actiondata = actiondata;
    }

    public String getDispbehavior() {
        return dispbehavior;
    }

    public void setDispbehavior(String dispbehavior) {
        this.dispbehavior = dispbehavior;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

}
