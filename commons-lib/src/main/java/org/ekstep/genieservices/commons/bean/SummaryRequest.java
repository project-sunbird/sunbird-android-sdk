package org.ekstep.genieservices.commons.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * This class accepts uids, contentId and hierarchyInfo when requesting for learner summary details.
 *
 */
public class SummaryRequest {

    private List<String> uids;
    private String contentId;
    private String hierarchyData;

    private SummaryRequest(List<String> uids, String contentId, String hierarchyData) {
        this.uids = uids;
        this.contentId = contentId;
        this.hierarchyData = hierarchyData;
    }

    public List<String> getUids() {
        return uids;
    }

    public String getContentId() {
        return contentId;
    }

    public String getHierarchyData() {
        return hierarchyData;
    }

    public static class Builder {
        private List<String> uids;
        private String contentId;
        private String hierarchyData;

        public Builder() {
            this.uids = new ArrayList<>();
        }

        /**
         * Content id for which you want the summary.
         */
        public Builder contentId(String contentId) {
            this.contentId = contentId;
            return this;
        }

        /**
         * User id for whom you want the summary.
         */
        public Builder uid(String uid) {
            this.uids.add(uid);
            return this;
        }

        /**
         * User id for whom you want the summary.
         */
        public Builder forUsers(List<String> uids) {
            this.uids.addAll(uids);
            return this;
        }

        /**
         * Hierarchy data of the content of which you want the summary.
         */
        public Builder hierarchyData(String hierarchyData) {
            this.hierarchyData = hierarchyData;
            return this;
        }

        public SummaryRequest build() {
            return new SummaryRequest(uids, contentId, hierarchyData);
        }
    }

}
