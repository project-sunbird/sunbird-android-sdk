package org.ekstep.genieservices.async;

/**
 * Created by swayangjit on 18/4/18.
 */

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IAuthSession;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.Map;

/**
 * This class provides all the required APIs to interact with the Keycloak service
 */
public class AuthSessionService {

    private IAuthSession authSession;

    public AuthSessionService(GenieService genieService) {
        this.authSession = genieService.getAuthSession();
    }

    public void createSession(final String userToken, IResponseHandler<Map<String, Object>> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<String>() {
            @Override
            public GenieResponse<String> perform() {
                return authSession.createSession(userToken);
            }
        }, responseHandler);
    }

    public void refreshSession(final String refreshToken, IResponseHandler<Map<String, Object>> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<String>() {
            @Override
            public GenieResponse<String> perform() {
                return authSession.refreshSession(refreshToken);
            }
        }, responseHandler);
    }
}
