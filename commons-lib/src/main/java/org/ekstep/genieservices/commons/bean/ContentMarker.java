package org.ekstep.genieservices.commons.bean;

import java.util.Map;

public class ContentMarker {

    private String contentId;
    private String uid;
    private Map<String, Object> extraInfoMap;
    private int marker;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Map<String, Object> getExtraInfoMap() {
        return extraInfoMap;
    }

    public void setExtraInfoMap(Map<String, Object> extraInfoMap) {
        this.extraInfoMap = extraInfoMap;
    }

    public int getMarker() {
        return marker;
    }

    public void setMarker(int marker) {
        this.marker = marker;
    }
}
