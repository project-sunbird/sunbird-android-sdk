package org.ekstep.genieservices.commons.bean;

import java.util.List;

/**
 * Created on 14/4/17.
 *
 * @author shriharsh
 */
public class MasterData {

    private String label;
    private String value;
    private String language;
    private String description;
    private List<MasterDataValues> values;

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public String getLanguage() {
        return language;
    }

    public String getDescription() {
        return description;
    }

    public List<MasterDataValues> getValues() {
        return values;
    }
}
