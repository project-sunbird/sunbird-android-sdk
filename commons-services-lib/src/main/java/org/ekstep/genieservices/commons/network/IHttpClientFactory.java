package org.ekstep.genieservices.commons.network;

/**
 * Created on 16/6/17.
 */

public interface IHttpClientFactory {

    public IHttpClient getClient();

    public IHttpAuthenticator getHttpAuthenticator();

}
