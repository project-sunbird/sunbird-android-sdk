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
    private Object variants;
    //    private String osId;
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