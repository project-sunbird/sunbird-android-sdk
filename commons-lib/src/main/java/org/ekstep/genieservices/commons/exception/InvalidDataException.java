package org.ekstep.genieservices.commons.exception;

/**
 * Created by swayangjit on 27/4/17.
 */

public class InvalidDataException extends RuntimeException{
    public InvalidDataException() {
    }

    public InvalidDataException(String detailMessage) {
        super(detailMessage);
    }
}
