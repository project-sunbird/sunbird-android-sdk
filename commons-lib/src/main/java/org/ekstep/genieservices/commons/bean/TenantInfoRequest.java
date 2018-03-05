package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 5/3/18.
 *
 * @author anil
 */
public class TenantInfoRequest {

    private String slug;
    private boolean refreshTenantInfo;

    private TenantInfoRequest(String slug, boolean refreshTenantInfo) {
        this.slug = slug;
        this.refreshTenantInfo = refreshTenantInfo;
    }

    public String getSlug() {
        return slug;
    }

    public boolean isRefreshTenantInfo() {
        return refreshTenantInfo;
    }

    public static class Builder {

        private String slug;
        private boolean refreshTenantInfo;

        public Builder bySlug(String slug) {
            if (StringUtil.isNullOrEmpty(slug)) {
                throw new IllegalArgumentException("slug required.");
            }

            this.slug = slug;
            return this;
        }

        /**
         * The tenant info is refreshed from the server only if this flag is set.
         */
        public Builder refreshTenantInfoFromServer() {
            this.refreshTenantInfo = true;
            return this;
        }

        public TenantInfoRequest build() {
            if (StringUtil.isNullOrEmpty(slug)) {
                throw new IllegalStateException("slug required.");
            }

            return new TenantInfoRequest(slug, refreshTenantInfo);
        }
    }

}
