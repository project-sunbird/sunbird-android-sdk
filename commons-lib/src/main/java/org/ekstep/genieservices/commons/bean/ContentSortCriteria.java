package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.SortOrder;

import java.io.Serializable;

/**
 * Sort criteria which uses sort attribute as String from [relevance, name, popularity, lastPublishedOn] and sort order will be ascending or descending.
 */
public class ContentSortCriteria implements Serializable {

    private String sortAttribute;
    private SortOrder sortOrder;

    public ContentSortCriteria(String sortAttribute, SortOrder sortOrder) {
        this.sortAttribute = sortAttribute;
        this.sortOrder = sortOrder;
    }

    public String getSortAttribute() {
        return sortAttribute;
    }

    public void setSortAttribute(String sortAttribute) {
        this.sortAttribute = sortAttribute;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }
}
