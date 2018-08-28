package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

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
                throw new IllegalArgumentException("userId should not be null or empty.");
            }
            this.userId = userId;
            return this;
        }

        public EndorseOrAddSkillRequest.Builder addSkills(String[] skills) {
            if (CollectionUtil.isEmpty(skills)) {
                throw new IllegalArgumentException("skills should not be null or empty.");
            }
            this.skills = skills;
            return this;
        }

        public EndorseOrAddSkillRequest build() {
            if (StringUtil.isNullOrEmpty(userId)) {
                throw new IllegalStateException("userId required.");
            }
            if (CollectionUtil.isEmpty(skills)) {
                throw new IllegalStateException("skills required.");
            }

            return new EndorseOrAddSkillRequest(userId, skills);
        }
    }

}
