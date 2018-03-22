package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.GsonUtil;

/**
 * Created on 1/3/18.
 *
 * @author souvikmondal
 */
public class Session {

    private String accessToken;
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
