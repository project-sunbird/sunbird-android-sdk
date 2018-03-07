package org.ekstep.genieservices;

/**
 * Created on 1/3/18.
 *
 * @author souvikmondal
 */
public interface IAuthSession<SessionData> {

    void startSession(SessionData sessionData);

    void endSession();

    SessionData getSessionData();

}
