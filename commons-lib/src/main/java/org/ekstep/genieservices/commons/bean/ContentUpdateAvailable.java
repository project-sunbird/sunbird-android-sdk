package org.ekstep.genieservices.commons.bean;

/**
 * This class holds the identifier of the content on which the update is available.
 */

public class ContentUpdateAvailable {

    private String identifier;

    public ContentUpdateAvailable(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
