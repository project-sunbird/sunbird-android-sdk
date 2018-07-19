package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.List;

/**
 * Created on 19/7/18.
 * shriharsh
 */
public class AddUpdateProfilesRequest {

    private String groupId;
    private List<String> uidList;

    private AddUpdateProfilesRequest(String groupId, List<String> uidList) {
        this.groupId = groupId;
        this.uidList = uidList;
    }

    public String getGroupId() {
        return groupId;
    }

    public List<String> getUidList() {
        return uidList;
    }

    public static class Builder {
        private String groupId;
        private List<String> uidList;

        /**
         * For which group users to be added
         *
         * @param groupId
         * @return
         */
        public Builder forGroup(String groupId) {
            if (StringUtil.isNullOrEmpty(groupId)) {
                throw new IllegalArgumentException("groupId required.");
            }
            this.groupId = groupId;
            return this;
        }


        /**
         * For which group users to be added
         *
         * @param uidList
         * @return
         */
        public Builder addUpdateUsers(List<String> uidList) {
            this.uidList = uidList;
            return this;
        }

        public AddUpdateProfilesRequest build() {
            if (StringUtil.isNullOrEmpty(groupId)) {
                throw new IllegalStateException("groupId required.");
            }
            return new AddUpdateProfilesRequest(groupId, uidList);
        }


    }


}
