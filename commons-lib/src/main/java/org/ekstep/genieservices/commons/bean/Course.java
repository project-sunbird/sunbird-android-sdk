package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.GsonUtil;

/**
 * Created on 6/3/18.
 *
 * @author anil
 */
public class Course {

    private String dateTime;
    private String identifier;
    private int lastReadContentStatus;
    private String enrolledDate;
    private String addedBy;
    private String delta;
    private String contentId;
    private boolean active;
    private String description;
    private String courseLogoUrl;
    private String batchId;
    private String userId;
    private String courseName;
    private int leafNodesCount;
    private int progress;
    private String id;
    private String tocUrl;
    private String lastReadContentId;
    private String courseId;
    private int status;

    public String getDateTime() {
        return dateTime;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getLastReadContentStatus() {
        return lastReadContentStatus;
    }

    public String getEnrolledDate() {
        return enrolledDate;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public String getDelta() {
        return delta;
    }

    public String getContentId() {
        return contentId;
    }

    public boolean isActive() {
        return active;
    }

    public String getDescription() {
        return description;
    }

    public String getCourseLogoUrl() {
        return courseLogoUrl;
    }

    public String getBatchId() {
        return batchId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getLeafNodesCount() {
        return leafNodesCount;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getId() {
        return id;
    }

    public String getTocUrl() {
        return tocUrl;
    }

    public String getLastReadContentId() {
        return lastReadContentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
