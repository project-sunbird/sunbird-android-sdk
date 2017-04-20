package org.ekstep.genieservices.commons.network;

import java.io.IOException;
import java.util.Map;

/**
 * Created on 4/19/2017.
 *
 * @author anil
 */
public interface IHttpClient extends IConnectionInfo {

    Void createClient();

    Void setTimeouts(int connectionTimeout, int readTimeout);

    Void handleAuth();

    Void createRequest(String url);

    Void setHeaders(Map<String, String> headers);

    ApiResponse doGet(String requestBody) throws IOException;

    ApiResponse doPost(String requestBody) throws IOException;

}
