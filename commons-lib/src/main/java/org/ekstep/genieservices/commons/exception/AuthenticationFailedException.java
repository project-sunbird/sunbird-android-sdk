package org.ekstep.genieservices.commons.exception;

/**
 * Created on 4/19/2017.
 *
 * @author anil
 */
public class AuthenticationFailedException extends RuntimeException {

    public AuthenticationFailedException(String detailMessage) {
        super(detailMessage);
    }

}
