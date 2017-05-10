package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created on 1/5/2016.
 *
 * @author anil
 */
public class ContentData implements Serializable {

    private String identifier;
    private String osId;
    private List<String> ageGroup;
    private String idealScreenSize;
    private String visibility;
    private String downloadUrl;
    private String contentType;
    private String mediaType;
    private String description;
    private String name;
    private String mimeType;
    private String pkgVersion;
    private List<String> os;
    private String idealScreenDensity;
    private String status;
    private String code;
    private String lastUpdatedOn;
    private String size;
    private String owner;
    private List<String> gradeLevel;
    private List<String> language;
    private String objectType;
    // Server specific
    private String artifactUrl;
    private String createdOn;
    private String lastPublishedOn;
    private String es_metadata_id;
    private String graph_id;
    private String license;
    private String nodeType;
    private String node_id;
    // Local data specific
    private String appIcon;
    private String subject;
    private String launchUrl;
    private List<String> tags;
    private String communication_scheme;
    private boolean isSkipContent;
    private String activity_class;
    private String format;
    //    private ContentMetadata contentMetadata;
    private String developer;
    private String grayScaleAppIcon;
    private String posterImage;
    private String filter;
    private String expires;
    private String publisher;
    private String me_totalRatings;
    private String me_averageRating;
    //    private List<Prerequisites> pre_requisites;
    private String copyright;
    private String s3Key;
    private Object variants;

    public ContentData() {
    }

    public String getS3Key() {
        return s3Key;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getOsId() {
        return osId;
    }

    public void setOsId(String osId) {
        this.osId = osId;
    }

    public List<String> getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(List<String> ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getIdealScreenSize() {
        return idealScreenSize;
    }

    public void setIdealScreenSize(String idealScreenSize) {
        this.idealScreenSize = idealScreenSize;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getPkgVersion() {
        return pkgVersion;
    }

    public void setPkgVersion(String pkgVersion) {
        this.pkgVersion = pkgVersion;
    }

    public List<String> getOs() {
        return os;
    }

    public void setOs(List<String> os) {
        this.os = os;
    }

    public String getIdealScreenDensity() {
        return idealScreenDensity;
    }

    public void setIdealScreenDensity(String idealScreenDensity) {
        this.idealScreenDensity = idealScreenDensity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(String lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(List<String> gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public List<String> getLanguage() {
        return language;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getArtifactUrl() {
        return artifactUrl;
    }

    public void setArtifactUrl(String artifactUrl) {
        this.artifactUrl = artifactUrl;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getLastPublishedOn() {
        return lastPublishedOn;
    }

    public void setLastPublishedOn(String lastPublishedOn) {
        this.lastPublishedOn = lastPublishedOn;
    }

    public String getEs_metadata_id() {
        return es_metadata_id;
    }

    public void setEs_metadata_id(String es_metadata_id) {
        this.es_metadata_id = es_metadata_id;
    }

    public String getGraph_id() {
        return graph_id;
    }

    public void setGraph_id(String graph_id) {
        this.graph_id = graph_id;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLaunchUrl() {
        return launchUrl;
    }

    public void setLaunchUrl(String launchUrl) {
        this.launchUrl = launchUrl;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCommunication_scheme() {
        return communication_scheme;
    }

    public void setCommunication_scheme(String communication_scheme) {
        this.communication_scheme = communication_scheme;
    }

    public boolean isSkipContent() {
        return isSkipContent;
    }

    public void setSkipContent(boolean skipContent) {
        isSkipContent = skipContent;
    }

    public String getActivity_class() {
        return activity_class;
    }

    public void setActivity_class(String activity_class) {
        this.activity_class = activity_class;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getGrayScaleAppIcon() {
        return grayScaleAppIcon;
    }

    public void setGrayScaleAppIcon(String grayScaleAppIcon) {
        this.grayScaleAppIcon = grayScaleAppIcon;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(String posterImage) {
        this.posterImage = posterImage;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getTotalRatings() {
        return me_totalRatings;
    }

    public String getAverageRating() {
        return me_averageRating;
    }

    public Variants getVariants() {
        Variants contentVariants = null;

        if (variants != null) {
            String variantsString;
            if (variants instanceof Map) {
                variantsString = GsonUtil.getGson().toJson(variants);
            } else {
                variantsString = (String) variants;
            }

            variantsString = variantsString.replace("\\", "");
            contentVariants = GsonUtil.fromJson(variantsString, Variants.class);
        }

        return contentVariants;
    }

    @Override
    public String toString() {
        return "ContentData{" +
                "osId='" + osId + '\'' +
                ", description='" + description + '\'' +
                ", communication_scheme='" + communication_scheme + '\'' +
                ", identifier='" + identifier + '\'' +
                ", name='" + name + '\'' +
                ", appIcon='" + appIcon + '\'' +
                ", activity_class='" + activity_class + '\'' +
                ", grayScaleAppIcon='" + grayScaleAppIcon + '\'' +
                ", code='" + code + '\'' +
                ", launchUrl='" + launchUrl + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", posterImage='" + posterImage + '\'' +
                ", pkgVersion='" + pkgVersion + '\'' +
                ", filter='" + filter + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", contentType='" + contentType + '\'' +
                ", publisher='" + publisher + '\'' +
                ", owner='" + owner + '\'' +
                ", language=" + language +
                ", size='" + size + '\'' +
                '}';
    }

}