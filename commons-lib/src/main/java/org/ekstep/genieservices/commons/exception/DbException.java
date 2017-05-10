package org.ekstep.genieservices.commons.exception;

public class DbException extends RuntimeException {
    public DbException(String detailMessage) {
        super(detailMessage);
    }
}
