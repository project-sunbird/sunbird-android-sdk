package org.ekstep.genieservices.commons.network;

import org.ekstep.genieservices.IAuthSession;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.auth.AuthHandler;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.Session;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.ReflectionUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.Map;

/**
 * Created by swayangjit on 16/4/18.
 */
public abstract class SunbirdBaseAPI extends BaseAPI {
    private AppContext mAppContext;

    public SunbirdBaseAPI(AppContext appContext, String url, String TAG) {
        super(appContext, url, TAG);
        this.mAppContext = appContext;
    }

    @Override
    protected void processAuthFailure(ApiResponse apiResponse) {
        if (apiResponse != null) {
            Map<String, Object> responseMap = GsonUtil.fromJson(apiResponse.getResponseBody(), Map.class);
            if (responseMap.containsKey("message")) {
                AuthHandler.resetAuthToken(mAppContext);
            } else {
                IAuthSession mAuthSession = null;
                String authSessionClass = mAppContext.getParams().getString(ServiceConstants.Params.OAUTH_SESSION);
                if (!StringUtil.isNullOrEmpty(authSessionClass)) {
                    Class<?> classInstance = ReflectionUtil.getClass(authSessionClass);
                    if (classInstance != null) {
                        mAuthSession = (IAuthSession) ReflectionUtil.getInstance(classInstance);
                    }
                }
                mAuthSession.initAuth(mAppContext);
                Session session = (Session) mAuthSession.getSessionData();
                String refreshToken = session.getRefreshToken();
                mAuthSession.refreshSession(refreshToken);

            }

        }
    }
}
