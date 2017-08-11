package org.ekstep.genieservices.commons.bean.enums;

/**
 * Created on 7/21/2017.
 *
 * @author anil
 */
public enum ContentImportStatus {

    NOT_FOUND(-1),
    ENQUEUED_FOR_DOWNLOAD(0), DOWNLOAD_STARTED(1), DOWNLOAD_FAILED(2), DOWNLOAD_COMPLETED(3),
    IMPORT_STARTED(4),
    IMPORT_FAILED(5),
    NOT_COMPATIBLE(6),
    CONTENT_EXPIRED(7), // This is for draft content.
    ALREADY_EXIST(8),
    IMPORT_COMPLETED(100);

    private int value;

    ContentImportStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
