package org.ekstep.genieservices.commons.bean;

/**
 * Created on 7/3/18.
 *
 * @author indraja machani
 */
public class UserProfileSkillsRequest {

    private boolean refreshProfileSkills;

    private UserProfileSkillsRequest(boolean refreshProfileSkills) {
        this.refreshProfileSkills = refreshProfileSkills;
    }

    public boolean isRefreshProfileSkills() {
        return refreshProfileSkills;
    }

    public static class Builder {

        private boolean refreshProfileSkills;

        /**
         * The profile skills refreshed from the server only if this flag is set.
         */
        public Builder refreshProfileSkillsFromServer() {
            this.refreshProfileSkills = true;
            return this;
        }

        public UserProfileSkillsRequest build() {
            return new UserProfileSkillsRequest(refreshProfileSkills);
        }
    }

}
