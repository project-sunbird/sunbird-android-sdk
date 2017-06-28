package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;
import java.util.List;

/**
 * This class holds responseMessageId, apiId, sectionName, list of {@link ContentData} and {@link ContentSearchCriteria}.
 *
 */
public class ContentListingSection implements Serializable {

    private String responseMessageId;
    private String apiId;
    private String sectionName;
    private List<ContentData> contentDataList;
    private ContentSearchCriteria contentSearchCriteria;

    public String getResponseMessageId() {
        return responseMessageId;
    }

    public void setResponseMessageId(String responseMessageId) {
        this.responseMessageId = responseMessageId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public List<ContentData> getContentDataList() {
        return contentDataList;
    }

    public void setContentDataList(List<ContentData> contentDataList) {
        this.contentDataList = contentDataList;
    }

    public ContentSearchCriteria getContentSearchCriteria() {
        return contentSearchCriteria;
    }

    public void setContentSearchCriteria(ContentSearchCriteria contentSearchCriteria) {
        this.contentSearchCriteria = contentSearchCriteria;
    }
}
