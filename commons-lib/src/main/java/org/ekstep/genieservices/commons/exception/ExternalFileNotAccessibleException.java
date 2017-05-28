package org.ekstep.genieservices.commons.exception;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class ExternalFileNotAccessibleException extends RuntimeException {
    public ExternalFileNotAccessibleException() {
        super("External file could not be loaded.");
    }
}
