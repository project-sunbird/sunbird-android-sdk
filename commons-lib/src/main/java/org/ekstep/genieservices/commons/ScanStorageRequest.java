package org.ekstep.genieservices.commons;

import org.ekstep.genieservices.commons.utils.StringUtil;

/**
 * Created on 10/10/17.
 * shriharsh
 */

public class ScanStorageRequest {

    private String storageFilePath;

    public ScanStorageRequest(String storageFilePath) {
        this.storageFilePath = storageFilePath;
    }

    public String getStorageFilePath() {
        return storageFilePath;
    }

    public static class Builder {
        private String storageFilePath;

        /**
         * Storage file path whose size has to be checked.
         */
        public ScanStorageRequest.Builder storagePath(String storageFilePath) {
            if (StringUtil.isNullOrEmpty(storageFilePath)) {
                throw new IllegalArgumentException("Illegal toFolder");
            }
            this.storageFilePath = storageFilePath;
            return this;
        }

        public ScanStorageRequest build() {
            if (StringUtil.isNullOrEmpty(storageFilePath)) {
                throw new IllegalStateException("Folder path required.");
            }

            return new ScanStorageRequest(storageFilePath);
        }
    }


}
