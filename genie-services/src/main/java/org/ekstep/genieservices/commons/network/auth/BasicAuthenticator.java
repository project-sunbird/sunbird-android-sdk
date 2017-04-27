package org.ekstep.genieservices.commons.network.auth;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.ekstep.genieservices.commons.exception.AuthenticationFailedException;
import org.ekstep.genieservices.utils.BuildConfigUtil;

import java.io.IOException;
import java.net.Proxy;

/**
 * Created on 4/19/2017.
 *
 * @author anil
 */
public class BasicAuthenticator implements Authenticator {

    private static final int MAX_RETRY = 1;
    private int currentAttempt = 0;

    @Override
    public Request authenticate(Proxy proxy, Response response) throws IOException {

        if (currentAttempt >= MAX_RETRY) {
            throw new AuthenticationFailedException("Invalid credentials");
        }

        currentAttempt++;

        String credentials = Credentials.basic(BuildConfigUtil.API_USER, BuildConfigUtil.API_PASS);

        return response.request().newBuilder().header("Authorization", credentials).build();
    }

    @Override
    public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        return null;
    }

}
