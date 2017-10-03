package org.ekstep.genieservices.commons.bean;

import java.util.List;

/**
 * Created by swayangjit on 28/9/17.
 */

public class ContentImport {
    private boolean isChildContent;
    private String destinationFolder;
    private String contentId;
    private List<CorrelationData> correlationData;

    public ContentImport(String contentId, String destinationFolder) {
        this.contentId = contentId;
        this.destinationFolder = destinationFolder;
    }

    public ContentImport(String contentId, boolean isChildContent, String destinationFolder) {
        this.contentId = contentId;
        this.isChildContent = isChildContent;
        this.destinationFolder = destinationFolder;
    }

    public boolean isChildContent() {
        return isChildContent;
    }


    public String getDestinationFolder() {
        return destinationFolder;
    }

    public String getContentId() {
        return contentId;
    }

    public List<CorrelationData> getCorrelationData() {
        return correlationData;
    }

    public void setCorrelationData(List<CorrelationData> correlationData) {
        this.correlationData = correlationData;
    }
}
