package org.ekstep.genieservices.commons.bean;

/**
 * Created on 23/5/17.
 * shriharsh
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