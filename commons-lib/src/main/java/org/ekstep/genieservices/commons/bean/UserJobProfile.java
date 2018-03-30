package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 29/3/18.
 * shriharsh
 */
public class UserJobProfile {

    private String jobName;
    private String orgName;
    private String role;
    private String subject;
    private boolean isCurrentJob;
    private String joiningDate;
    private String endDate;
    private String id;
    private boolean isDeleted;

    public UserJobProfile(String jobName, String orgName, String role, String subject, boolean isCurrentJob, String joiningDate, String endDate, String id, boolean isDeleted) {
        this.jobName = jobName;
        this.orgName = orgName;
        this.role = role;
        this.subject = subject;
        this.isCurrentJob = isCurrentJob;
        this.joiningDate = joiningDate;
        this.endDate = endDate;
        this.id = id;
        this.isDeleted = isDeleted;
    }

    public String getJobName() {
        return jobName;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getRole() {
        return role;
    }

    public String getSubject() {
        return subject;
    }

    public boolean isCurrentJob() {
        return isCurrentJob;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getId() {
        return id;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public static class Builder {
        private String jobName;
        private String orgName;
        private String role;
        private String subject;
        private boolean isCurrentJob;
        private String joiningDate;
        private String endDate;
        private String id;
        private boolean isDeleted;

        public Builder jobName(String jobName) {
            if (StringUtil.isNullOrEmpty(jobName)) {
                throw new IllegalArgumentException("jobName cannot be null or empty.");
            }

            this.jobName = jobName;
            return this;
        }


        public Builder organization(String orgName) {
            if (StringUtil.isNullOrEmpty(orgName)) {
                throw new IllegalArgumentException("orgName cannot be null or empty.");
            }

            this.orgName = orgName;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder subjects(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder isCurrentJob(boolean isCurrentJob) {
            this.isCurrentJob = isCurrentJob;
            return this;
        }

        public Builder joiningDate(String joiningDate) {
            this.joiningDate = joiningDate;
            return this;
        }

        public Builder endDate(String endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder isDeleted(boolean isDeleted) {
            this.isCurrentJob = isDeleted;
            return this;
        }

        public UserJobProfile build() {
            if (StringUtil.isNullOrEmpty(jobName)) {
                throw new IllegalStateException("jobName is required.");
            }

            if (StringUtil.isNullOrEmpty(orgName)) {
                throw new IllegalStateException("orgName is required");
            }

            return new UserJobProfile(jobName, orgName, role, subject, isCurrentJob, joiningDate, endDate, id, isDeleted);
        }


    }

}
