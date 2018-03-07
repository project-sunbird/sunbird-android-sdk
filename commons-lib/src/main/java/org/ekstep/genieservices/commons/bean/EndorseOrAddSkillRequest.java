package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.List;

/**
 * Created on 7/3/18.
 *
 * @author indraja machani
 */
public class EndorseOrAddSkillRequest {

    private String userId;
    private String[] skills;

    private EndorseOrAddSkillRequest(String userId, String[] skills) {
        this.userId = userId;
        this.skills = skills;
    }

    public String getUserId() {
        return userId;
    }

    public String[] getSkills() {
        return skills;
    }

    public static class Builder {

        private String userId;
        private String[] skills;

        public EndorseOrAddSkillRequest.Builder forUser(String userId) {
            if (StringUtil.isNullOrEmpty(userId)) {
                throw new IllegalArgumentException("userId required.");
            }
            this.userId = userId;
            return this;
        }

        public EndorseOrAddSkillRequest.Builder AddSkills(String[] skills) {
            this.skills = skills;
            return this;
        }

        public EndorseOrAddSkillRequest build() {
            return new EndorseOrAddSkillRequest(userId, skills);
        }
    }

}
