package org.ekstep.genieservices.commons.bean;

import com.google.gson.annotations.SerializedName;

import org.ekstep.genieservices.commons.utils.GsonUtil;

/**
 * Created on 1/3/18.
 *
 * @author souvikmondal
 */
public class Session {

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("refresh_token")
    private String refreshToken;
    private String userToken;

    public Session(String accessToken, String refreshToken, String userToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userToken = userToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getUserToken() {
        return userToken;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
