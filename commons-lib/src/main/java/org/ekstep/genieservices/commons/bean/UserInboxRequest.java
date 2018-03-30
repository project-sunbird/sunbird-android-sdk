package org.ekstep.genieservices.commons.bean;

/**
 * @author indraja on 30/3/18.
 */
public class UserInboxRequest {

    private int limit;
    private int offset;

    private UserInboxRequest(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }


    public static class Builder {

        private int limit;
        private int offset;

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder offset(int offset) {
            this.offset = offset;
            return this;
        }

        public UserInboxRequest build() {
            return new UserInboxRequest(limit, offset);
        }
    }
}
