package org.ekstep.genieservices.commons.network;


import java.util.Map;

public class MultipartFormRequestBody implements IRequestBody<Map<String, Object>> {

    private Map<String, Object> body;

    @Override
    public Map<String, Object> getBody() {
        return this.body;
    }

    @Override
    public void setBody(Map<String, Object> body) {
        this.body = body;
    }

    @Override
    public String getContentType() {
        return MIME_TYPE_FORM_MULTIPART;
    }
}
