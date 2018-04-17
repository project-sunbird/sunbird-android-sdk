package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.AppContext;

/**
 * Created on 1/3/18.
 *
 * @author souvikmondal
 */
public interface IAuthSession<SessionData> {

    void initAuth(AppContext appContext);

    void createSession(String callBackUrl);

    void refreshSession(String refreshToken);

    void startSession(SessionData sessionData);

    void endSession();

    SessionData getSessionData();

}
