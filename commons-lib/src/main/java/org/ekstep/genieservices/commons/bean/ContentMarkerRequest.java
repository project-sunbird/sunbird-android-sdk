package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.MarkerType;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.Map;

public class ContentMarkerRequest {

    private String contentId;
    private String uid;
    private String data;
    private Map<String, Object> extraInfoMap;
    private int marker;
    private boolean isMarked;

    private ContentMarkerRequest(String contentId, String uid, String data,
                                 Map<String, Object> extraInfoMap, int marker, boolean isMarked) {
        this.contentId = contentId;
        this.uid = uid;
        this.data = data;
        this.extraInfoMap = extraInfoMap;
        this.marker = marker;
        this.isMarked = isMarked;
    }

    public String getContentId() {
        return contentId;
    }

    public String getUid() {
        return uid;
    }

    public String getData() {
        return data;
    }

    public Map<String, Object> getExtraInfoMap() {
        return extraInfoMap;
    }

    public int getMarker() {
        return marker;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public static class Builder {
        private String contentId;
        private String uid;
        private String data;
        private Map<String, Object> extraInfoMap;
        private int marker;
        private boolean isMarked;

        public Builder forContent(String contentId) {
            if (StringUtil.isNullOrEmpty(contentId)) {
                throw new IllegalArgumentException("contentId should not be null or empty.");
            }
            this.contentId = contentId;
            return this;
        }

        public Builder forUser(String uid) {
            if (StringUtil.isNullOrEmpty(uid)) {
                throw new IllegalArgumentException("uid should not be null or empty.");
            }
            this.uid = uid;
            return this;
        }

        public Builder forData(String data) {
            if (StringUtil.isNullOrEmpty(data)) {
                throw new IllegalArgumentException("data should not be null or empty.");
            }
            this.data = data;
            return this;
        }

        public Builder extraInfo(Map<String, Object> extraInfoMap) {
            this.extraInfoMap = extraInfoMap;
            return this;
        }

        public Builder setMarker(MarkerType marker) {
            this.marker = marker.getValue();
            this.isMarked = true;
            return this;
        }

        public Builder unsetMarker(MarkerType marker) {
            this.marker = marker.getValue();
            return this;
        }

        public ContentMarkerRequest build() {
            if (StringUtil.isNullOrEmpty(contentId)) {
                throw new IllegalStateException("contentId required.");
            }
            if (StringUtil.isNullOrEmpty(uid)) {
                throw new IllegalStateException("uid required.");
            }
            if (StringUtil.isNullOrEmpty(data)) {
                throw new IllegalStateException("data required.");
            }

            return new ContentMarkerRequest(contentId, uid, data, extraInfoMap, marker, isMarked);
        }
    }
}
