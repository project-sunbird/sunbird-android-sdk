package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.List;

/**
 * Created on 5/11/18.
 * shriharsh
 */
public class GetContentStateRequest {

    private String userId;
    private List<String> courseIds;
    private List<String> contentIds;
    private String batchId;
    private boolean returnRefreshedContentStates;

    private GetContentStateRequest(String userId, List<String> courseIds, List<String> contentIds, String batchId, boolean returnRefreshedContentStates) {
        this.userId = userId;
        this.courseIds = courseIds;
        this.contentIds = contentIds;
        this.batchId = batchId;
        this.returnRefreshedContentStates = returnRefreshedContentStates;
    }

    public boolean isReturnRefreshedContentStates() {
        return returnRefreshedContentStates;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getCourseIds() {
        return courseIds;
    }

    public List<String> getContentIds() {
        return contentIds;
    }

    public String getBatchId() {
        return batchId;
    }

    public static class Builder {
        private String userId;
        private List<String> courseIds;
        private List<String> contentIds;
        private String batchId;
        private boolean returnRefreshedContentStates;

        public Builder forUser(String userId) {
            if (StringUtil.isNullOrEmpty(userId)) {
                throw new IllegalArgumentException("userId should not be null or empty.");
            }
            this.userId = userId;
            return this;
        }

        public Builder forCourses(List<String> courseIds) {
            if (CollectionUtil.isNullOrEmpty(courseIds)) {
                throw new IllegalArgumentException("courseIds should not be null or empty.");
            }
            this.courseIds = courseIds;
            return this;
        }

        public Builder forContents(List<String> contentIds) {
            this.contentIds = contentIds;
            return this;
        }

        public Builder forBatch(String batchId) {
            this.batchId = batchId;
            return this;
        }

        /**
         * The content states are refreshed from the server only if this flag is set and returned immediately.
         */
        public Builder returnRefreshedContentStatesFromServer() {
            this.returnRefreshedContentStates = true;
            return this;
        }

        public GetContentStateRequest build() {
            if (StringUtil.isNullOrEmpty(userId)) {
                throw new IllegalStateException("userId required.");
            }

            if (CollectionUtil.isNullOrEmpty(courseIds)) {
                throw new IllegalStateException("courseIds required.");
            }

            return new GetContentStateRequest(userId, courseIds, contentIds, batchId, returnRefreshedContentStates);
        }
    }

}
