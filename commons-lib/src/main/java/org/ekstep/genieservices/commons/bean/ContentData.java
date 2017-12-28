package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
    private String me_totalDownloads;
    private String copyright;
    private String license;
    private String expires;
    private String downloadUrl;
    private Object variants;
    private String artifactUrl;
    private List<String> language;
    private List<String> gradeLevel;
    private String osId;
    private String contentType;
    private String resourceType;
    private String mimeType;
    private String artifactMimeType;
    private String versionKey;
    private String contentEncoding;
    private String contentDisposition;
    private String contentTypesCount;
    private String lastPublishedOn;
    private String createdOn;
    private List<String> screenshots;

    public ContentData() {
    }

    public ContentData(String identifier) {
        this.identifier = identifier;
    }

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

    public String getSize(String variantName) {
        if (!StringUtil.isNullOrEmpty(variantName)) {
            Map variants = getVariants();
            if (variants != null && variants.get(variantName) != null) {
                Map variantMap = (Map) variants.get(variantName);
                return variantMap.get("size").toString();
            }
        }

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

    public String getTotalDownloads() {
        return me_totalDownloads;
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

    public Map<String, Object> getVariants() {
        Map<String, Object> variantMap = null;
        if (variants != null) {
            if (variants instanceof Map) {
                variantMap = (Map<String, Object>) variants;
            } else {
                variantMap = GsonUtil.fromJson(((String) variants).replace("\\", ""), Map.class);
            }
        }
        return variantMap;
    }

    public String getArtifactUrl() {
        return artifactUrl;
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

    public String getContentType() {
        return contentType;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getArtifactMimeType() {
        return artifactMimeType;
    }

    public String getVersionKey() {
        return versionKey;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public String getContentTypesCount() {
        return contentTypesCount;
    }

    public String getLastPublishedOn() {
        return lastPublishedOn;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public List<String> getScreenshots() {
        return screenshots;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContentData contentData = (ContentData) o;

        return identifier.equals(contentData.identifier);
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
                ", me_totalDownloads='" + me_totalDownloads + '\'' +
                ", copyright='" + copyright + '\'' +
                ", license='" + license + '\'' +
                ", expires='" + expires + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", variants='" + variants + '\'' +
                ", artifactUrl='" + artifactUrl + '\'' +
                ", language=" + language +
                ", gradeLevel=" + gradeLevel +
                ", osId='" + osId + '\'' +
                ", contentType='" + contentType + '\'' +
                ", resourceType='" + resourceType + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", artifactMimeType='" + artifactMimeType + '\'' +
                ", versionKey='" + versionKey + '\'' +
                ", contentEncoding='" + contentEncoding + '\'' +
                ", contentDisposition='" + contentDisposition + '\'' +
                ", contentTypesCount='" + contentTypesCount + '\'' +
                ", lastPublishedOn='" + lastPublishedOn + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", screenshots=" + screenshots +
                '}';
    }
}