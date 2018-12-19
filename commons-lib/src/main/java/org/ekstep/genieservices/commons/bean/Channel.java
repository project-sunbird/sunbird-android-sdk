package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.GsonUtil;

import java.util.List;

/**
 * Created on 12/3/18.
 *
 * @author anil
 */
public class Channel {

    private String identifier;
    private String code;
    private String consumerId;
    private String channel;
    private String description;
    private List<LinkedFramework> frameworks;
    private String createdOn;
    private String versionKey;
    private String appId;
    private String name;
    private String lastUpdatedOn;
    private String defaultFramework;
    private String status;

    public String getIdentifier() {
        return identifier;
    }

    public String getCode() {
        return code;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public String getChannel() {
        return channel;
    }

    public String getDescription() {
        return description;
    }

    public List<LinkedFramework> getFrameworks() {
        return frameworks;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getVersionKey() {
        return versionKey;
    }

    public String getAppId() {
        return appId;
    }

    public String getName() {
        return name;
    }

    public String getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public String getDefaultFramework() {
        return defaultFramework;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
