package org.ekstep.genieservices.commons.network;


public interface IRequestBody<T> {

    String MIME_TYPE_JSON = "application/json";
    String MIME_TYPE_FORM = "application/x-www-form-urlencoded";
    String MIME_TYPE_FORM_MULTIPART = "multipart/form-data";

    T getBody();

    void setBody(T t);

    String getContentType();

}
