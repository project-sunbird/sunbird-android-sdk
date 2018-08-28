package org.ekstep.genieservices.commons.bean;

/**
 * Created on 23/7/18.
 * shriharsh
 */
public class GroupRequest {

    private String uid;


    private GroupRequest(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public static class Builder {
        private String uid;

        /**
         * If required to know users from a specific group, groupId to be passed
         *
         * @param uid
         * @return
         */
        public Builder forUser(String uid) {
            this.uid = uid;
            return this;
        }

        public GroupRequest build() {
            return new GroupRequest(uid);
        }

    }
}
