package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.List;

/**
 * Created on 13/3/18.
 *
 * @author anil
 */
public class Category {

    private String identifier;
    private String code;
    private String name;
    private String description;
    private Integer index;
    private String status;
    private List<Term> terms;

    public String getIdentifier() {
        return identifier;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getIndex() {
        return index;
    }

    public String getStatus() {
        return status;
    }

    public List<Term> getTerms() {
        return terms;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
