package org.ekstep.genieservices.commons.bean;

/**
 * Created on 18/7/18.
 * shriharsh
 */
public class ProfileRequest {

    private boolean local;
    private boolean server;
    private String groupId;

    private ProfileRequest(boolean local, boolean server, String groupId) {
        this.local = local;
        this.server = server;
        this.groupId = groupId;
    }

    public boolean isLocal() {
        return local;
    }

    public boolean isServer() {
        return server;
    }

    public String getGroupId() {
        return groupId;
    }


    public static class Builder {
        private boolean local = false;
        private boolean server = false;
        private String groupId;

        /**
         * Call when local users are needed
         *
         * @return
         */
        public Builder localUsers() {
            this.local = true;
            return this;
        }


        /**
         * Call when server users are needed
         *
         * @return
         */
        public Builder serverUsers() {
            this.server = true;
            return this;
        }


        /**
         * If required to know users from a specific group, groupId to be passed
         *
         * @param groupId
         * @return
         */
        public Builder forGroup(String groupId) {
            this.groupId = groupId;
            return this;
        }


        public ProfileRequest build() {
            if (!this.local && !this.server) {
                throw new IllegalStateException("Select at least any one of the type of users");
            }

            return new ProfileRequest(local, server, groupId);
        }

    }


}
