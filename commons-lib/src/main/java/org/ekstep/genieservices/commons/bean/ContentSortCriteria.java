package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.SortOrder;

import java.io.Serializable;

/**
 * Sort criteria which uses sort attribute as String from [relevance, name, popularity, lastPublishedOn, sizeOnDevice, lastUsedOn, localLastUpdatedOn] and sort order will be ascending or descending.
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContentSortCriteria)) return false;

        ContentSortCriteria that = (ContentSortCriteria) o;

        return sortAttribute != null ? sortAttribute.equals(that.sortAttribute) : that.sortAttribute == null;
    }

    @Override
    public int hashCode() {
        return sortAttribute != null ? sortAttribute.hashCode() : 0;
    }
}
