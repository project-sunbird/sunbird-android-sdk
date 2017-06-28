package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;
import java.util.List;

/**
 * This class holds the name of the filter and the its list of {@link FilterValue} in values.
 *
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
