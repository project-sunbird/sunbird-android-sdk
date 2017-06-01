package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 1/5/2016.
 *
 * @author anil
 */
public class ContentData implements Serializable {

    private String identifier;
    private String name;
    private String appIcon;
    private String description;
    private String pkgVersion;
    private String status;
    private String size;
    private String owner;
    private String subject;
    private String publisher;
    private String me_totalRatings;
    private String me_averageRating;
    private String copyright;
    private String license;
    private String expires;
    private String downloadUrl;
    private List<String> language;
    private String osId;
    private List<Variant> variantList;
    //    private ContentMetadata contentMetadata;

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public String getDescription() {
        return description;
    }

    public String getPkgVersion() {
        return pkgVersion;
    }

    public String getStatus() {
        return status;
    }

    public String getSize() {
        return size;
    }

    public String getOwner() {
        return owner;
    }

    public String getSubject() {
        return subject;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getMe_totalRatings() {
        return me_totalRatings;
    }

    public String getMe_averageRating() {
        return me_averageRating;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getLicense() {
        return license;
    }

    public String getExpires() {
        return expires;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public List<String> getLanguage() {
        return language;
    }

    public String getOsId() {
        return osId;
    }

    public List<Variant> getVariants() {
        return variantList;
    }

    public void setVariants(List<Variant> variantList) {
        this.variantList = variantList;
    }

    @Override
    public String toString() {
        return "ContentData{" +
                ", description='" + description + '\'' +
                ", identifier='" + identifier + '\'' +
                ", name='" + name + '\'' +
                ", appIcon='" + appIcon + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", pkgVersion='" + pkgVersion + '\'' +
                ", publisher='" + publisher + '\'' +
                ", owner='" + owner + '\'' +
                ", size='" + size + '\'' +
                '}';
    }

}