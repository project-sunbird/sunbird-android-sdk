package org.ekstep.genieservices.commons.bean;

import java.util.HashMap;
import java.util.List;

/**
 * Created on 23/5/17.
 * shriharsh
 */

public class RelatedContentRequest {
    private String uid;
    private List<HashMap<String, Object>> contentIdentifiers;

    public RelatedContentRequest(String uid, List<HashMap<String, Object>> contentIdentifiers) {
        this.uid = uid;
        this.contentIdentifiers = contentIdentifiers;
    }

    public String uid() {
        return uid;
    }

    public List<HashMap<String, Object>> contentIdentifiers() {
        return contentIdentifiers;
    }
}

