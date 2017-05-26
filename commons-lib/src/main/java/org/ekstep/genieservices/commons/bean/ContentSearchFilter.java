package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 5/26/2017.
 *
 * @author anil
 */
public class ContentSearchFilter implements Serializable {

    private String name;
    private List<FilterValue> values;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FilterValue> getValues() {
        return values;
    }

    public void setValues(List<FilterValue> values) {
        this.values = values;
    }
}
