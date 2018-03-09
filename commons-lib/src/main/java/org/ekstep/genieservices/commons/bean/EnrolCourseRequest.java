package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 6/3/18.
 *
 * @author anil
 */
public class EnrolCourseRequest {

    private String userId;
    private String courseId;
    private String contentId;
    private String batchId;

    private EnrolCourseRequest(String userId, String courseId, String contentId, String batchId) {
        this.userId = userId;
        this.courseId = courseId;
        this.contentId = contentId;
        this.batchId = batchId;
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

    public static class Builder {
        private String userId;
        private String courseId;
        private String contentId;
        private String batchId;

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

        public EnrolCourseRequest build() {
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

            return new EnrolCourseRequest(userId, courseId, contentId, batchId);
        }
    }
}
