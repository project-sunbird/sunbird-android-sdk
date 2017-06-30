package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.List;

/**
 * This class accepts contentId while building it, and is used when requesting child contents.
 */
public class ChildContentRequest {

    private String contentId;
    private List<HierarchyInfo> hierarchyInfo;
    private int level;
    private boolean attachFeedback;
    private boolean attachContentAccess;

    private ChildContentRequest(String contentId, List<HierarchyInfo> hierarchyInfo, int level, boolean attachFeedback, boolean attachContentAccess) {
        this.contentId = contentId;
        this.hierarchyInfo = hierarchyInfo;
        this.level = level;
        this.attachFeedback = attachFeedback;
        this.attachContentAccess = attachContentAccess;
    }

    public String getContentId() {
        return contentId;
    }

    public List<HierarchyInfo> getHierarchyInfo() {
        return hierarchyInfo;
    }

    public int getLevel() {
        return level;
    }

    public boolean attachFeedback() {
        return attachFeedback;
    }

    public boolean attachContentAccess() {
        return attachContentAccess;
    }

    public static class Builder {
        private String contentId;
        private List<HierarchyInfo> hierarchyInfo;
        private int level;
        private boolean attachFeedback;
        private boolean attachContentAccess;

        public Builder() {
            this.level = -1;
        }

        /**
         * Content id for which you want child contents.
         */
        public Builder forContent(String contentId) {
            if (StringUtil.isNullOrEmpty(contentId)) {
                throw new IllegalArgumentException("contentId required.");
            }
            this.contentId = contentId;
            return this;
        }

        /**
         * Hierarchy info of the content which is return in {@link Content}.
         */
        public Builder hierarchyInfo(List<HierarchyInfo> hierarchyInfo) {
            if (hierarchyInfo == null) {
                throw new IllegalArgumentException("hierarchyInfo required. Set hierarchyInfo of the content which is return in {@link Content}");
            }
            this.hierarchyInfo = hierarchyInfo;
            return this;
        }

        /**
         * Return next level children.
         */
        public Builder nextLevel() {
            this.level = 1;
            return this;
        }

        /**
         * Set the level/depth of the child contents.
         */
        public Builder level(int level) {
            this.level = level;
            return this;
        }

        /**
         * If want feedback, provided by given uid.
         */
        public Builder withFeedback() {
            this.attachFeedback = true;
            return this;
        }

        /**
         * If want content access by given uid.
         */
        public Builder withContentAccess() {
            this.attachContentAccess = true;
            return this;
        }

        public ChildContentRequest build() {
            if (StringUtil.isNullOrEmpty(contentId)) {
                throw new IllegalStateException("contentId required.");
            }
            if (hierarchyInfo == null) {
                throw new IllegalStateException("hierarchyInfo required. Set hierarchyInfo of the content which is return in {@link Content}");
            }
            return new ChildContentRequest(contentId, hierarchyInfo, level, attachFeedback, attachContentAccess);
        }
    }
}
