package org.ekstep.genieservices.commons.bean.enums;

/**
 * Enum to hold existing content action required.
 */
public enum ExistingContentAction {

    IGNORE(1),              // Content in the new/destination folder will be used/kept.
    KEEP_HIGHER_VERSION(2),  // Keep the content with higher version
    KEEP_LOWER_VERSION(3),   // Keep the content with lower version
    KEEP_SOURCE(4),   // Keep the content that is in the source
    KEEP_DESTINATION(5);   // Keep the content that is in the destination

    private int value;

    ExistingContentAction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}