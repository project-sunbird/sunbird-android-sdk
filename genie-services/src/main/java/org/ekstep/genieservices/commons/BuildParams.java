package org.ekstep.genieservices.commons;

import android.content.Context;

import org.ekstep.genieservices.Constants;
import org.ekstep.genieservices.commons.bean.enums.LogLevel;
import org.ekstep.genieservices.commons.utils.ReflectionUtil;
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
    private int logLevel;
    private String qualifier;
    private String profilePath;

    public BuildParams(Context context, String packageName) {
        baseApiUrl = BuildConfigUtil.getBuildConfigValue(packageName, Constants.Params.API_BASE_URL);
        userName = BuildConfigUtil.getBuildConfigValue(packageName, Constants.Params.API_USER);
        password = BuildConfigUtil.getBuildConfigValue(packageName, Constants.Params.API_PASS);
        versionName = BuildConfigUtil.getBuildConfigValue(packageName, Constants.Params.VERSION_NAME);
        gId = BuildConfigUtil.getBuildConfigValue(packageName, Constants.Params.GID);
        logLevel = LogLevel.getLogLevel(BuildConfigUtil.getBuildConfigValue(packageName, Constants.Params.LOGLEVEL)).getLevel();
        qualifier = BuildConfigUtil.getBuildConfigValue(packageName, Constants.Params.APP_QUALIFIER);
        String profileConfigClass = BuildConfigUtil.getBuildConfigValue(packageName, Constants.Params.PROFILE_CONFIG);
        if (profileConfigClass != null) {
            Class<?> classInstance = ReflectionUtil.getClass(profileConfigClass);
            if (classInstance != null) {
                IProfileConfig profileConfiguration = (IProfileConfig) ReflectionUtil.getInstance(classInstance);
                profilePath = profileConfiguration.getProfilePath(context);
            }

        }
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

    @Override
    public int getLogLevel() {
        return logLevel;
    }

    @Override
    public String getQualifier() {
        return qualifier;
    }

    @Override
    public String getProfilePath() {
        return profilePath;
    }


}
