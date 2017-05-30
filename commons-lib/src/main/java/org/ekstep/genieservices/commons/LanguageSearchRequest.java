package org.ekstep.genieservices.commons;

/**
 * Created on 29/5/17.
 * shriharsh
 */

public class LanguageSearchRequest {
    private String requestData;

    public LanguageSearchRequest(String requestData) {
        this.requestData = requestData;
    }

    public String requestData() {
        return this.requestData;
    }
}

