package org.ekstep.genieservices.commons.bean;

/**
 * This class holds the current index of copying item, total number of contents, when importing a content.
 */

public class ImportContentProgress {

    private int currentCount;
    private int totalCount;

    public ImportContentProgress(int currentCount, int totalCount) {
        this.currentCount = currentCount;
        this.totalCount = totalCount;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
