package org.ekstep.genieservices.commons;

/**
 * Created on 14/4/17.
 *
 * @author shriharsh
 */
public class Response<T> {

    private T instance;
    private boolean status;
    private int errorCode;

    public T getInstance() {
        return instance;
    }

    public void setInstance(T instance) {
        this.instance = instance;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
