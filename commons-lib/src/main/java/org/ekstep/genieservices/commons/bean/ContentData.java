package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;
import java.util.List;

/**
 * This class holds all the data related to Content Data
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
    private List<String> gradeLevel;
    private String osId;
    private String contentType;
    private List<ContentVariant> contentVariantList;
    private List<String> screenshots;

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

    public String getTotalRatings() {
        return me_totalRatings;
    }

    public String getAverageRating() {
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

    public List<String> getGradeLevel() {
        return gradeLevel;
    }

    public String getOsId() {
        return osId;
    }

    public List<ContentVariant> getVariants() {
        return contentVariantList;
    }

    public void setVariants(List<ContentVariant> contentVariantList) {
        this.contentVariantList = contentVariantList;
    }

    public String getContentType() {
        return contentType;
    }

    public List<String> getScreenshots() {
        return screenshots;
    }

    @Override
    public String toString() {
        return "ContentData{" +
                "identifier='" + identifier + '\'' +
                ", name='" + name + '\'' +
                ", appIcon='" + appIcon + '\'' +
                ", description='" + description + '\'' +
                ", pkgVersion='" + pkgVersion + '\'' +
                ", status='" + status + '\'' +
                ", size='" + size + '\'' +
                ", owner='" + owner + '\'' +
                ", subject='" + subject + '\'' +
                ", publisher='" + publisher + '\'' +
                ", me_totalRatings='" + me_totalRatings + '\'' +
                ", me_averageRating='" + me_averageRating + '\'' +
                ", copyright='" + copyright + '\'' +
                ", license='" + license + '\'' +
                ", expires='" + expires + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", language=" + language +
                ", gradeLevel=" + gradeLevel +
                ", osId='" + osId + '\'' +
                ", contentType='" + contentType + '\'' +
                ", contentVariantList=" + contentVariantList +
                ", screenshots=" + screenshots +
                '}';
    }
}