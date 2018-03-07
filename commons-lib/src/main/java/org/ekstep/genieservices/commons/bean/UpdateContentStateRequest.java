package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 6/3/18.
 *
 * @author anil
 */
public class UpdateContentStateRequest {

    private String userId;
    private String courseId;
    private String contentId;
    private String batchId;
    private int status;
    private int progress;
    private String lastAccessTime;

    private UpdateContentStateRequest(String userId, String courseId, String contentId,
                                      String batchId, int status, int progress) {
        this.userId = userId;
        this.courseId = courseId;
        this.contentId = contentId;
        this.batchId = batchId;
        this.status = status;
        this.progress = progress;
    }

    public String getUserId() {
        return userId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getContentId() {
        return contentId;
    }

    public String getBatchId() {
        return batchId;
    }

    public int getStatus() {
        return status;
    }

    public int getProgress() {
        return progress;
    }

    public static class Builder {
        private String userId;
        private String courseId;
        private String contentId;
        private String batchId;
        private int status;
        private int progress;

        public Builder forUser(String userId) {
            if (StringUtil.isNullOrEmpty(userId)) {
                throw new IllegalArgumentException("userId should not be null or empty.");
            }
            this.userId = userId;
            return this;
        }

        public Builder forCourse(String courseId) {
            if (StringUtil.isNullOrEmpty(courseId)) {
                throw new IllegalArgumentException("courseId should not be null or empty.");
            }
            this.courseId = courseId;
            return this;
        }

        public Builder forContent(String contentId) {
            if (StringUtil.isNullOrEmpty(contentId)) {
                throw new IllegalArgumentException("contentId should not be null or empty.");
            }
            this.contentId = contentId;
            return this;
        }

        public Builder forBatch(String batchId) {
            if (StringUtil.isNullOrEmpty(batchId)) {
                throw new IllegalArgumentException("batchId should not be null or empty.");
            }
            this.batchId = batchId;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder progress(int progress) {
            this.progress = progress;
            return this;
        }

        public UpdateContentStateRequest build() {
            if (StringUtil.isNullOrEmpty(userId)) {
                throw new IllegalStateException("userId required.");
            }

            if (StringUtil.isNullOrEmpty(courseId)) {
                throw new IllegalStateException("courseId required.");
            }

            if (StringUtil.isNullOrEmpty(contentId)) {
                throw new IllegalStateException("contentId required.");
            }

            if (StringUtil.isNullOrEmpty(batchId)) {
                throw new IllegalStateException("batchId required.");
            }

            return new UpdateContentStateRequest(userId, courseId, contentId, batchId,
                    status, progress);
        }
    }
}
