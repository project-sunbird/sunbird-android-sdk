package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 18/7/18.
 * shriharsh
 */
public class GetProfileRequest {

    private boolean local;
    private boolean server;
    private String uid;
    private boolean latestCreatedProfile;

    private GetProfileRequest(boolean local, boolean server, String uid, boolean latestCreatedProfile) {
        this.local = local;
        this.server = server;
        this.uid = uid;
        this.latestCreatedProfile = latestCreatedProfile;
    }

    public boolean isLatestCreatedProfile() {
        return latestCreatedProfile;
    }

    public boolean isLocal() {
        return local;
    }

    public boolean isServer() {
        return server;
    }

    public String getUid() {
        return uid;
    }


    public static class Builder {
        private boolean local = false;
        private boolean server = false;
        private String uid;
        private boolean latestCreatedProfile = false;


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
         * For a specific user
         *
         * @param uid
         * @return
         */
        public Builder forUser(String uid) {
            this.uid = uid;
            return this;
        }

        /**
         * For the latest created profile
         *
         * @return
         */
        public Builder latestCreatedProfile(){
            this.latestCreatedProfile = true;
            return this;
        }


        public GetProfileRequest build() {
            if(!latestCreatedProfile && StringUtil.isNullOrEmpty(uid)){
                throw new IllegalStateException("You must request either latest created profile or any specific user");
            }

            if(!StringUtil.isNullOrEmpty(uid) && latestCreatedProfile){
                throw new IllegalStateException("You can request to get the latest created profile or any specific user, but not both at the same time.");
            }

            return new GetProfileRequest(local, server, uid, latestCreatedProfile);
        }

    }


}
