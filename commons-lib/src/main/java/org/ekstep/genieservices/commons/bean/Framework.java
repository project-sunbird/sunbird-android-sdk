package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.List;

/**
 * Created on 13/3/18.
 *
 * @author anil
 */
public class Framework {

    private String identifier;
    private String code;
    private String name;
    private String description;
    private String status;
    private String objectType;
    private String owner;
    private String consumerId;
    private String channel;
    private String type;
    private String createdOn;
    private String versionKey;
    private List<Channel> channels;
    private String appId;
    private String lastUpdatedOn;
    private List<Category> categories;

    public String getIdentifier() {
        return identifier;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getObjectType() {
        return objectType;
    }

    public String getOwner() {
        return owner;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public String getChannel() {
        return channel;
    }

    public String getType() {
        return type;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getVersionKey() {
        return versionKey;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public String getAppId() {
        return appId;
    }

    public String getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
