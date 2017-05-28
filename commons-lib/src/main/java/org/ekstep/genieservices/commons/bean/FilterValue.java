package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;

/**
 * Created on 5/26/2017.
 *
 * @author anil
 */
public class FilterValue implements Serializable {

    private String name;
    private int count;
    private boolean apply;

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
}
