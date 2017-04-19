package org.ekstep.genieservices.commons.network;

/**
 * Created by anil on 4/19/2017.
 *
 * @author anil
 */
public interface NetworkConstants {

    int NETWORK_READ_TIMEOUT_MINUTES = 3;
    int NETWORK_CONNECT_TIMEOUT_MINUTES = 3;

    String CONNECTION_ERROR = "CONNECTION_ERROR";
    String CONNECTION_ERROR_MESSAGE = "No internet connection, please try again later.";

    String SERVER_ERROR = "SERVER_ERROR";
    String SERVER_ERROR_MESSAGE = "Something went wrong. Please try again later.";

    String NETWORK_ERROR = "NETWORK_ERROR";

}
