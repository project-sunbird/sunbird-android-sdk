package org.ekstep.genieservices;

/**
 * Created by souvikmondal on 1/3/18.
 */

public interface IAuthSession<SessionData> {

    void startSession(SessionData sessionData);

    void endSession();

    SessionData getSessionData();

}
