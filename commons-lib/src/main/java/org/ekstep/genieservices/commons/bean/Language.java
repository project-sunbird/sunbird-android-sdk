package org.ekstep.genieservices.commons.bean;

/**
 * This class holds the name and code of the language.
 *
 */
public class Language {
    private String name;
    private String code;

    public Language(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}