package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This class accepts contentId and isChildContent while building it, and is used when deleting a content and set if it is a child content.
 */
public class ContentDeleteRequest {

    private List<ContentDelete> contentDeleteList;

    private ContentDeleteRequest(List<ContentDelete> contentDeleteList) {
        this.contentDeleteList = contentDeleteList;
    }

    public List<ContentDelete> getContentDeleteList() {
        return contentDeleteList;
    }

    public static class Builder {
        private List<ContentDelete> contentDeleteList;

        public Builder() {
            this.contentDeleteList = new ArrayList<>();
        }

        public Builder add(ContentDelete contentDelete) {
            if (contentDelete == null) {
                throw new IllegalArgumentException("contentDelete can not be null.");
            }
            if (StringUtil.isNullOrEmpty(contentDelete.getContentId())) {
                throw new IllegalArgumentException("Illegal contentId");
            }

            this.contentDeleteList.add(contentDelete);
            return this;
        }

        public ContentDeleteRequest build() {
            if (CollectionUtil.isNullOrEmpty(contentDeleteList)) {
                throw new IllegalStateException("Add at least one content to delete.");
            }

            return new ContentDeleteRequest(contentDeleteList);
        }
    }
}
