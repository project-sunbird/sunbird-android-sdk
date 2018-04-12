package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 29/3/18.
 * shriharsh
 */
public class UserJobProfile {

    private String id;
    private String jobName;
    private String orgName;
    private String role;
    private String[] subject;
    private String joiningDate; //YYYY-MM-DD
    private String endDate;     //YYYY-MM-DD
    private boolean isCurrentJob;
    private boolean isDeleted;

    private UserJobProfile(String id, String jobName, String orgName, String role, String[] subject,
                           String joiningDate, String endDate, boolean isCurrentJob, boolean isDeleted) {
        this.id = id;
        this.jobName = jobName;
        this.orgName = orgName;
        this.role = role;
        this.subject = subject;
        this.joiningDate = joiningDate;
        this.endDate = endDate;
        this.isCurrentJob = isCurrentJob;
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

    public String[] getSubject() {
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

        private String id;
        private String jobName;
        private String orgName;
        private String role;
        private String[] subject;
        private String joiningDate;
        private String endDate;
        private boolean isCurrentJob;
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

        public Builder subjects(String[] subject) {
            this.subject = subject;
            return this;
        }

        public Builder currentJob() {
            this.isCurrentJob = true;
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

        public Builder deletedJobProfile() {
            this.isDeleted = true;
            return this;
        }

        public UserJobProfile build() {
            if (StringUtil.isNullOrEmpty(jobName)) {
                throw new IllegalStateException("jobName is required.");
            }

            if (StringUtil.isNullOrEmpty(orgName)) {
                throw new IllegalStateException("orgName is required");
            }

            return new UserJobProfile(id, jobName, orgName, role, subject, joiningDate, endDate, isCurrentJob, isDeleted);
        }
    }

}
