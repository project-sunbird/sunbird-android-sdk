package org.ekstep.genieservices.commons.bean;

import java.util.List;

/**
 * Created on 5/29/2017.
 *
 * @author anil
 */
public class ContentListingResult {

    private String id;
    private String responseMessageId;
    private List<Section> sections;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResponseMessageId() {
        return responseMessageId;
    }

    public void setResponseMessageId(String responseMessageId) {
        this.responseMessageId = responseMessageId;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
