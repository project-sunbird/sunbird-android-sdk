package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.Map;

/**
 * Created on 1/3/18.
 *
 * @author souvikmondal
 */
public interface IAuthSession<SessionData> {

    void initAuth(AppContext appContext);

    GenieResponse<Map<String, Object>> createSession(String userToken);

    GenieResponse<Map<String, Object>> refreshSession(String refreshToken);

    void startSession(SessionData sessionData);

    void endSession();

    SessionData getSessionData();

}
