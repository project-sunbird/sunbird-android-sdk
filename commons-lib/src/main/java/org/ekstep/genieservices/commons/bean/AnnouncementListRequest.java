package org.ekstep.genieservices.commons.bean;

/**
 * @author indraja on 30/3/18.
 */
public class AnnouncementListRequest {

    private static final int DEFAULT_LIMIT = 100;

    private int limit;
    private int offset;

    private AnnouncementListRequest(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public static class Builder {

        private int limit;
        private int offset;

        public Builder() {
            this.limit = DEFAULT_LIMIT;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder offset(int offset) {
            this.offset = offset;
            return this;
        }

        public AnnouncementListRequest build() {
            return new AnnouncementListRequest(limit, offset);
        }
    }
}
