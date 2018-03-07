package org.ekstep.genieservices.commons.network;


import java.util.Map;

public class FormRequestBody implements IRequestBody<Map<String, String>> {

    private Map<String, String> body;

    @Override
    public Map<String, String> getBody() {
        return this.body;
    }

    @Override
    public void setBody(Map<String, String> body) {
        this.body = body;
    }

    @Override
    public String getContentType() {
        return MIME_TYPE_FORM;
    }
}
