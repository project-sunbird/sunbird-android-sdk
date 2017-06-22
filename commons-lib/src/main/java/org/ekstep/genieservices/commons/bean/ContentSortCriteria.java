package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.SearchType;
import org.ekstep.genieservices.commons.bean.enums.SortOrder;

import java.io.Serializable;
import java.util.List;

/**
 * Sort criteria
 *
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
