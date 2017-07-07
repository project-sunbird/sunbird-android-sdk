package org.ekstep.genieservices.commons.utils;


import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtil {
    public static final String AES = "AES";
    public static final String CIPHER_AES = "AES/CBC/PKCS7Padding";
    private static final String ALGO = "RSA";
    private static final String CIPHER_ALGO = "RSA/ECB/PKCS1Padding";
    private static final String TAG = "org.ekstep.genieservices.util-CryptoUtil";

    public static String checksum(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = null;
        md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < sha1hash.length; i++) {
            sb.append(Integer.toString((sha1hash[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static SecretKey generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey;
    }

    public static String encryptWithAES(String plainText, SecretKey secretKey, IvParameterSpec iv)
            throws
            NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException, InvalidAlgorithmParameterException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance(CIPHER_AES);
        byte[] plainTextByte = plainText.getBytes("UTF-8");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encryptedBytes = cipher.doFinal(plainTextByte);
        String encryptedText = Base64Util.encodeToString(encryptedBytes, Base64Util.DEFAULT);
        return encryptedText;
    }

    public static String encryptWithPublic(byte[] testString, String publicKey)
            throws
            InvalidKeyException,
            InvalidKeySpecException,
            UnsupportedEncodingException,
            BadPaddingException,
            IllegalBlockSizeException,
            NoSuchAlgorithmException,
            NoSuchPaddingException {
        String temp = new String(publicKey);
        temp = temp.replace("-----BEGIN PUBLIC KEY-----\n", "");
        temp = temp.replace("-----END PUBLIC KEY-----", "");
        byte[] byteKey = Base64Util.decode(temp.getBytes("UTF-8"), Base64Util.DEFAULT);
        X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
        KeyFactory kf = KeyFactory.getInstance(ALGO);
        PublicKey _public_key = kf.generatePublic(X509publicKey);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, _public_key);
//        byte[] stringBytes = testString.getBytes("UTF-8");
        byte[] encryptedBytes = cipher.doFinal(testString);
        String encryptedData = Base64Util.encodeToString(encryptedBytes, Base64Util.DEFAULT);
        return encryptedData;
    }

    public static byte[] encryptSecretKeyWithRSAPublic(SecretKey skey, PublicKey pubicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubicKey);
        byte[] key = cipher.doFinal(skey.getEncoded());
        return key;
    }

    public static String encryptWithPublic(String testString, String publicKey)
            throws
            InvalidKeyException,
            InvalidKeySpecException,
            UnsupportedEncodingException,
            BadPaddingException,
            IllegalBlockSizeException,
            NoSuchAlgorithmException,
            NoSuchPaddingException {
        String temp = new String(publicKey);
        temp = temp.replace("-----BEGIN PUBLIC KEY-----\n", "");
        temp = temp.replace("-----END PUBLIC KEY-----", "");
        byte[] byteKey = Base64Util.decode(temp.getBytes("UTF-8"), Base64Util.DEFAULT);
        X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
        KeyFactory kf = KeyFactory.getInstance(ALGO);
        PublicKey _public_key = kf.generatePublic(X509publicKey);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, _public_key);
        byte[] stringBytes = testString.getBytes("UTF-8");
        byte[] encryptedBytes = cipher.doFinal(stringBytes);
        String encryptedData = Base64Util.encodeToString(encryptedBytes, Base64Util.DEFAULT);
        return encryptedData;
    }

    public static String decrypt(byte[] encrypted, PrivateKey key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(encrypted);
        String decryptedData = Base64Util.encodeToString(decryptedBytes, Base64Util.DEFAULT);
        return decryptedData;
    }

    public static PrivateKey generatePrivateKey(String privateKey) throws
            Exception {
        String temp = new String(privateKey);
        String privKeyPEM = temp.replace("-----BEGIN PRIVATE KEY-----\n", "");
        privKeyPEM = privKeyPEM.replace("-----END PRIVATE KEY-----", "");

        byte[] decoded = Base64Util.decode(privKeyPEM.getBytes("UTF-8"), Base64Util.DEFAULT);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance(ALGO);
        return kf.generatePrivate(spec);
    }

    public static PublicKey generatePublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
        String temp = new String(publicKey);
        temp = temp.replace("-----BEGIN PUBLIC KEY-----\n", "");
        temp = temp.replace("-----END PUBLIC KEY-----", "");
        byte[] byteKey = Base64Util.decode(temp.getBytes("UTF-8"), Base64Util.DEFAULT);
        X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
        KeyFactory kf = KeyFactory.getInstance(ALGO);
        PublicKey _public_key = kf.generatePublic(X509publicKey);
        return _public_key;
    }

    public static String convertSecretKeyToString(SecretKey aesKey) {
        return Base64Util.encodeToString(aesKey.getEncoded(), Base64Util.DEFAULT);
    }

    public static SecretKey generateSecretKey(String secretKeyString) throws UnsupportedEncodingException {
        byte[] byteKey = Base64Util.decode(secretKeyString.getBytes("UTF-8"), Base64Util.DEFAULT);
        return new SecretKeySpec(byteKey, 0, byteKey.length, AES);
    }

    public static String decryptWithSecretKey(String data, SecretKey secretKey, String ivString) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        byte[] encryptedDataBytes = Base64Util.decode(data.getBytes("UTF-8"), Base64Util.DEFAULT);
        Cipher cipher = Cipher.getInstance(CIPHER_AES);
        byte[] ivBytes = Base64Util.decode(ivString.getBytes("UTF-8"), Base64Util.DEFAULT);
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedDataBytes);
        String decryptedText = new String(decryptedBytes);
        return decryptedText;
    }

    public static IvParameterSpec generateIVSpecForAES() throws NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance(CIPHER_AES);
        SecureRandom random = new SecureRandom();
        byte[] realIV = new byte[cipher.getBlockSize()];
        random.nextBytes(realIV);
        return new IvParameterSpec(realIV);
    }

}
