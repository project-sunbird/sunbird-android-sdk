package org.ekstep.genieservices.commons.bean;

/**
 * Created on 10/3/2017.
 * <p>
 * This class accepts contentId and isChildContent while building it, and is used when deleting a content and set if it is a child content.
 *
 * @author anil
 */

public class ContentDelete {

    private String contentId;
    private boolean isChildContent;

    /**
     * @param contentId Content id which you want to delete
     */
    public ContentDelete(String contentId) {
        this.contentId = contentId;
    }

    /**
     * Call it only if the deleting content is a child content.
     */
    public ContentDelete(String contentId, boolean isChildContent) {
        this.contentId = contentId;
        this.isChildContent = isChildContent;
    }

    public String getContentId() {
        return contentId;
    }

    public boolean isChildContent() {
        return isChildContent;
    }
}
