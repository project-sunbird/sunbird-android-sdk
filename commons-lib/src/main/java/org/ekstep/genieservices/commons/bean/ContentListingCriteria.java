package org.ekstep.genieservices.commons.bean;

import java.util.List;

/**
 * This class accepts contentListingId, subject, {@link Profile}, and list of {@link PartnerFilter} while building the criteria for content listing request.
 *
 */
public class ContentListingCriteria {

    private String contentListingId;
    private String subject;
    private Profile profile;
    private List<PartnerFilter> partnerFilters;

    private ContentListingCriteria(String contentListingId, String subject, Profile profile, List<PartnerFilter> partnerFilters) {
        this.contentListingId = contentListingId;
        this.subject = subject;
        this.profile = profile;
        this.partnerFilters = partnerFilters;
    }

    public String getContentListingId() {
        return contentListingId;
    }

    public String getSubject() {
        return subject;
    }

    public Profile getProfile() {
        return profile;
    }

    public List<PartnerFilter> getPartnerFilters() {
        return partnerFilters;
    }

    public static class Builder {

        private String contentListingId;
        private String subject;
        private Profile profile;
        private List<PartnerFilter> partnerFilters;

        public Builder listingId(String contentListingId) {
            this.contentListingId = contentListingId;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder profile(Profile profile) {
            this.profile = profile;
            return this;
        }

        public Builder partnerFilters(List<PartnerFilter> partnerFilters) {
            this.partnerFilters = partnerFilters;
            return this;
        }

        public ContentListingCriteria build() {
            return new ContentListingCriteria(contentListingId, subject, profile, partnerFilters);
        }
    }
}
