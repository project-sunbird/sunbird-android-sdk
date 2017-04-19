package org.ekstep.genieservices.commons.network;

import java.io.IOException;

/**
 * Created on 4/19/2017.
 *
 * @author anil
 */
public interface API {

    void handleAuth();

    Void fetchFromServer();

    String createRequestData();

    void handle(String responseBody) throws IOException;

}
