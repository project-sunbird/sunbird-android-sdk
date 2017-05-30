package org.ekstep.genieservices.commons.bean;

import java.util.List;

/**
 * Created on 5/30/2017.
 *
 * @author anil
 */
public class PageAssembleCriteria {

    private String pageIdentifier;
    private String subject;
    private boolean currentProfileFilter;
    private List<PartnerFilter> partnerFilters;

    public String getPageIdentifier() {
        return pageIdentifier;
    }

    public void setPageIdentifier(String pageIdentifier) {
        this.pageIdentifier = pageIdentifier;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isCurrentProfileFilter() {
        return currentProfileFilter;
    }

    public void setCurrentProfileFilter(boolean currentProfileFilter) {
        this.currentProfileFilter = currentProfileFilter;
    }

    public List<PartnerFilter> getPartnerFilters() {
        return partnerFilters;
    }

    public void setPartnerFilters(List<PartnerFilter> partnerFilters) {
        this.partnerFilters = partnerFilters;
    }
}
