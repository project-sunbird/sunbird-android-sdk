package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.List;

/**
 * Created on 19/7/18.
 * shriharsh
 */
public class AddUpdateGroupsRequest {

    private String uid;
    private List<String> groupIdList;

    private AddUpdateGroupsRequest(String uid, List<String> groupIdList) {
        this.uid = uid;
        this.groupIdList = groupIdList;
    }

    public String getUid() {
        return uid;
    }

    public List<String> getGroupIdList() {
        return groupIdList;
    }

    public static class Builder {
        private String uid;
        private List<String> groupIdList;

        /**
         * For which profile groups to be added
         *
         * @param uid
         * @return
         */
        public Builder forProfile(String uid) {
            if (StringUtil.isNullOrEmpty(uid)) {
                throw new IllegalArgumentException("uid required.");
            }
            this.uid = uid;
            return this;
        }


        /**
         * Which group to be added
         *
         * @param groupIdList
         * @return
         */
        public Builder addUpdateGroups(List<String> groupIdList) {
            this.groupIdList = groupIdList;
            return this;
        }

        public AddUpdateGroupsRequest build() {
            if (StringUtil.isNullOrEmpty(uid)) {
                throw new IllegalStateException("uid required.");
            }
            return new AddUpdateGroupsRequest(uid, groupIdList);
        }


    }


}
