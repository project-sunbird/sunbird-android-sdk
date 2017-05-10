package org.ekstep.genieservices.commons;

import org.ekstep.genieservices.Constants;
import org.ekstep.genieservices.utils.BuildConfigUtil;

/**
 * Created on 27/4/17.
 *
 * @author swayangjit
 */
public class BuildParams implements IParams {

    private String baseApiUrl;
    private String userName;
    private String password;
    private String versionName;
    private String gId;

    public BuildParams(String packageName) {
        baseApiUrl = BuildConfigUtil.getBuildConfigValue(packageName, Constants.Params.API_BASE_URL);
        userName = BuildConfigUtil.getBuildConfigValue(packageName, Constants.Params.API_USER);
        password = BuildConfigUtil.getBuildConfigValue(packageName, Constants.Params.API_PASS);
        versionName = BuildConfigUtil.getBuildConfigValue(packageName, Constants.Params.VERSION_NAME);
        gId = BuildConfigUtil.getBuildConfigValue(packageName, Constants.Params.GID);
    }

    @Override
    public String getBaseApiUrl() {
        return baseApiUrl;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getVersionName() {
        return versionName;
    }

    @Override
    public String getGid() {
        return gId;
    }
}
