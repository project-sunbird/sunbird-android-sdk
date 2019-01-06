package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.List;

public class ContentStateResponse {

    private List<ContentState> contentList;

    public List<ContentState> getContentList() {
        return contentList;
    }

    public void setContentList(List<ContentState> contentList) {
        this.contentList = contentList;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
