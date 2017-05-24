package org.ekstep.genieservices.commons.bean.enums;

/**
 * Created on 5/11/2017.
 *
 * @author anil
 */
public enum ContentType {

    GAME("game"), STORY("story"), WORKSHEET("worksheet"), COLLECTION("collection"), TEXTBOOK("textbook"), TEXTBOOK_UNIT("textbookunit");

    private String value;

    ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
