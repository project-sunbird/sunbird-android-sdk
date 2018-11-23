package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;

/**
 * This class holds the name of the filter, count and apply boolean if it has to be applied while searching for a content.
 *
 */
public class FilterValue implements Serializable {

    private String name;
    private int count;
    private boolean apply;
    private String translations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isApply() {
        return apply;
    }

    public void setApply(boolean apply) {
        this.apply = apply;
    }

    public String getTranslations() {
        return translations;
    }

    public void setTranslations(String translations) {
        this.translations = translations;
    }
}
