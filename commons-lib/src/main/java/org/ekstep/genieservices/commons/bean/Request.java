package org.ekstep.genieservices.commons.bean;

/**
 * Created on 17/5/17.
 *
 * @author swayangjit
 */
public class Request {

    private String url;
    private String title;
    private String mimeType;

    public Request(String url, String title, String mimeType) {
        this.url = url;
        this.title = title;
        this.mimeType = mimeType;
    }


    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getMimeType() {
        return mimeType;
    }
}
