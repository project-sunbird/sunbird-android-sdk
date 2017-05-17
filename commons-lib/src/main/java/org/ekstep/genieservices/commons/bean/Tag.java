package org.ekstep.genieservices.commons.bean;

/**
 * Created by swayangjit on 12/5/17.
 */

public class Tag {

    private String name;
    private String description;
    private String startDate;
    private String endDate;

    public Tag(String name, String description, String startDate, String endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
