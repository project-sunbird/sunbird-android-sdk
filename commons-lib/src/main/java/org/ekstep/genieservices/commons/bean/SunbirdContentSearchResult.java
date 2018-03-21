package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This class holds the id, responseMessageId, filter as {@link List<ContentSearchFilter>}, request as {@link Map<String, Object>} and contentDataList as {@link List<ContentData>}
 * after searching for a particular content.
 */
public class SunbirdContentSearchResult implements Serializable {

    private String id;
    private String responseMessageId;
    private SunbirdContentSearchCriteria filterCriteria;
    private Map<String, Object> request;
    private List<ContentData> contentDataList;
    private List<ContentData> collectionDataList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResponseMessageId() {
        return responseMessageId;
    }

    public void setResponseMessageId(String responseMessageId) {
        this.responseMessageId = responseMessageId;
    }

    public SunbirdContentSearchCriteria getFilterCriteria() {
        return filterCriteria;
    }

    public void setFilterCriteria(SunbirdContentSearchCriteria filterCriteria) {
        this.filterCriteria = filterCriteria;
    }

    public Map<String, Object> getRequest() {
        return request;
    }

    public void setRequest(Map<String, Object> request) {
        this.request = request;
    }

    public List<ContentData> getContentDataList() {
        return contentDataList;
    }

    public void setContentDataList(List<ContentData> contentDataList) {
        this.contentDataList = contentDataList;
    }

    public List<ContentData> getCollectionDataList() {
        return collectionDataList;
    }

    public void setCollectionDataList(List<ContentData> collectionDataList) {
        this.collectionDataList = collectionDataList;
    }
}
