package org.ekstep.genieservices.commons.exception;

/**
 * Created on 27/4/17.
 *
 * @author swayangjit
 */
public class InvalidDataException extends RuntimeException {

    public InvalidDataException(String detailMessage) {
        super(detailMessage);
    }
}
