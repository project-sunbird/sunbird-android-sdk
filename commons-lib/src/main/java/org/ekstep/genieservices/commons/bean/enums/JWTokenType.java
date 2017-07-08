package org.ekstep.genieservices.commons.bean.enums;

/**
 * Created by swayangjit on 22/5/17.
 */

public enum JWTokenType {

    HS256("HS256", "HmacSHA256");

    private String algorithmName;
    private String tokenType;

    JWTokenType(String tokenType, String algorithmName) {
        this.algorithmName = algorithmName;
        this.tokenType = tokenType;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public String getTokenType() {
        return tokenType;
    }

}
