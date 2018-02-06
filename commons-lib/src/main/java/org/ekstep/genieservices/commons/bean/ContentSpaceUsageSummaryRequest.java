package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.CollectionUtil;

import java.util.List;

/**
 * Created on 5/2/18.
 *
 * @author anil
 */
public class ContentSpaceUsageSummaryRequest {

    private List<String> paths;

    private ContentSpaceUsageSummaryRequest(List<String> paths) {
        this.paths = paths;
    }

    public List<String> getPaths() {
        return paths;
    }

    public static class Builder {

        private List<String> paths;

        public Builder paths(List<String> paths) {
            if (CollectionUtil.isNullOrEmpty(paths)) {
                throw new IllegalArgumentException("Illegal paths");
            }
            this.paths = paths;
            return this;
        }

        public ContentSpaceUsageSummaryRequest build() {
            if (CollectionUtil.isNullOrEmpty(paths)) {
                throw new IllegalStateException("Paths required.");
            }
            return new ContentSpaceUsageSummaryRequest(paths);
        }

    }
}
