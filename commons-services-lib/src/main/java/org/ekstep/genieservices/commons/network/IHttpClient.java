package org.ekstep.genieservices.commons.network;

import java.io.IOException;
import java.util.Map;

/**
 * Created on 4/19/2017.
 *
 * @author anil
 */
public interface IHttpClient {

    Void setAuthHeaders();

    Void createRequest(String url);

    Void setHeaders(Map<String, String> headers);

    ApiResponse doGet() throws IOException;

    ApiResponse doPost(byte[] requestBody) throws IOException;

}
