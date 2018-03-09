package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.List;

/**
 * Created on 6/3/18.
 *
 * @author anil
 */
public class Batch {

    private String identifier;
    private String id;
    private List<String> createdFor;
    private Object courseAdditionalInfo;
    private String endDate;
    private String description;
    private Object participant;
    private String updatedDate;
    private String createdDate;
    private List<String> mentors;
    private String name;
    private String enrollmentType;
    private String courseId;
    private String startDate;
    private String hashTagId;
    private int status;
    private String courseCreator;
    private String createdBy;
    private String creatorName;

    public String getIdentifier() {
        return identifier;
    }

    public String getId() {
        return id;
    }

    public List<String> getCreatedFor() {
        return createdFor;
    }

    public Object getCourseAdditionalInfo() {
        return courseAdditionalInfo;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getDescription() {
        return description;
    }

    public Object getParticipant() {
        return participant;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public List<String> getMentors() {
        return mentors;
    }

    public String getName() {
        return name;
    }

    public String getEnrollmentType() {
        return enrollmentType;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getHashTagId() {
        return hashTagId;
    }

    public int getStatus() {
        return status;
    }

    public String getCourseCreator() {
        return courseCreator;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatorName() {
        return creatorName;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
