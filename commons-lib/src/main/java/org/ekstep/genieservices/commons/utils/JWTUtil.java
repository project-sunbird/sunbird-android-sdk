package org.ekstep.genieservices.commons.utils;

import org.ekstep.genieservices.commons.bean.enums.JWTokenType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 7/7/17.
 */

public class JWTUtil {

    private static final String SEPARATOR = ".";

    public static String createJWToken(String subject, String secretKey, JWTokenType tokenType) {
        if (tokenType == null) {
            tokenType = JWTokenType.HS256;
        }
        String payLoad = createHeader(tokenType) + SEPARATOR + createBody(subject);
        String signature = encodeToBase64Uri(CryptoUtil.generateHMAC(payLoad, secretKey.getBytes(), tokenType.getAlgorithmName()));
        return payLoad + SEPARATOR + signature;
    }

    private static String createHeader(JWTokenType tokenType) {
        Map<String, String> headerData = new HashMap<>();
        headerData.put("alg", tokenType.getTokenType());
        return encodeToBase64Uri(GsonUtil.toJson(headerData).getBytes());
    }


    private static String createBody(String subject) {
        Map<String, String> payloadData = new HashMap<>();
        payloadData.put("iss", subject);
        return encodeToBase64Uri(GsonUtil.toJson(payloadData).getBytes());
    }

    private static String encodeToBase64Uri(byte[] data) {
        //The 11 magic number indicates that it should be base64Uri and without wrap and with the = at the end
        return Base64Util.encodeToString(data, 11);
    }

}
