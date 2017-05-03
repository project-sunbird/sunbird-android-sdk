package org.ekstep.genieservices.util;


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
//import java.util.Base64;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
    private static final String ALGO = "RSA";
    private static final String CIPHER_ALGO = "RSA/ECB/PKCS1Padding";
    private static final String TAG = "org.ekstep.genieservices.util-Crypto";
    public static final String AES = "AES";
    public static final String CIPHER_AES = "AES/CBC/PKCS7Padding";

    public static String checksum(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = null;
        md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return sha1hash.toString();
    }

    public static SecretKey generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey;
    }

//    public static String encryptWithAES(String plainText,SecretKey secretKey, IvParameterSpec iv)
//            throws
//            NoSuchPaddingException,
//            NoSuchAlgorithmException,
//            InvalidKeyException,
//            BadPaddingException,
//            IllegalBlockSizeException, InvalidAlgorithmParameterException, UnsupportedEncodingException {
//        Cipher cipher = Cipher.getInstance(CIPHER_AES);
//        byte[] plainTextByte = plainText.getBytes("UTF-8");
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey,iv);
//        byte[] encryptedBytes = cipher.doFinal(plainTextByte);
//        String encryptedText = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
//        return encryptedText;
//    }

//    public static String encryptWithPublic(byte[] testString, String publicKey)
//            throws
//                InvalidKeyException,
//                InvalidKeySpecException,
//                UnsupportedEncodingException,
//                BadPaddingException,
//                IllegalBlockSizeException,
//                NoSuchAlgorithmException,
//                NoSuchPaddingException {
//        String temp = new String(publicKey);
//        temp = temp.replace("-----BEGIN PUBLIC KEY-----\n", "");
//        temp = temp.replace("-----END PUBLIC KEY-----", "");
//        byte[] byteKey = Base64.decode(temp.getBytes("UTF-8"), Base64.DEFAULT);
//        X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
//        KeyFactory kf = KeyFactory.getInstance(ALGO);
//        PublicKey _public_key = kf.generatePublic(X509publicKey);
//        Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
//        cipher.init(Cipher.ENCRYPT_MODE,_public_key);
////        byte[] stringBytes = testString.getBytes("UTF-8");
//        byte[] encryptedBytes = cipher.doFinal(testString);
//        String  encryptedData = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
//        return encryptedData;
//    }

    public static byte[] encryptSecretKeyWithRSAPublic(SecretKey skey, PublicKey pubicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubicKey );
        byte[] key = cipher.doFinal(skey.getEncoded());
        return key;
    }

//    public static String encryptWithPublic(String testString, String publicKey)
//            throws
//            InvalidKeyException,
//            InvalidKeySpecException,
//            UnsupportedEncodingException,
//            BadPaddingException,
//            IllegalBlockSizeException,
//            NoSuchAlgorithmException,
//            NoSuchPaddingException {
//        String temp = new String(publicKey);
//        temp = temp.replace("-----BEGIN PUBLIC KEY-----\n", "");
//        temp = temp.replace("-----END PUBLIC KEY-----", "");
//        byte[] byteKey = Base64.decode(temp.getBytes("UTF-8"), Base64.DEFAULT);
//        X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
//        KeyFactory kf = KeyFactory.getInstance(ALGO);
//        PublicKey _public_key = kf.generatePublic(X509publicKey);
//        Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
//        cipher.init(Cipher.ENCRYPT_MODE,_public_key);
//        byte[] stringBytes = testString.getBytes("UTF-8");
//        byte[] encryptedBytes = cipher.doFinal(stringBytes);
//        String  encryptedData = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
//        return encryptedData;
//    }

//    public static String decrypt(byte[] encrypted, PrivateKey key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
//        Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
//        cipher.init(Cipher.DECRYPT_MODE,key);
//        byte[] decryptedBytes = cipher.doFinal(encrypted);
//        String  decryptedData = Base64.encodeToString(decryptedBytes, Base64.DEFAULT);
//        return decryptedData;
//    }

//    public static PrivateKey generatePrivateKey(String privateKey) throws
//            Exception {
//        String temp = new String(privateKey);
//        String privKeyPEM = temp.replace("-----BEGIN PRIVATE KEY-----\n", "");
//        privKeyPEM = privKeyPEM.replace("-----END PRIVATE KEY-----", "");
//
//        byte[] decoded = Base64.decode(privKeyPEM.getBytes("UTF-8"), Base64.DEFAULT);
//
//        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
//        KeyFactory kf = KeyFactory.getInstance(ALGO);
//        return kf.generatePrivate(spec);
//    }

//    public static PublicKey generatePublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
//        String temp = new String(publicKey);
//        temp = temp.replace("-----BEGIN PUBLIC KEY-----\n", "");
//        temp = temp.replace("-----END PUBLIC KEY-----", "");
//        byte[] byteKey = Base64.decode(temp.getBytes("UTF-8"), Base64.DEFAULT);
//        X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
//        KeyFactory kf = KeyFactory.getInstance(ALGO);
//        PublicKey _public_key = kf.generatePublic(X509publicKey);
//        return _public_key;
//    }

//    public static String convertSecretKeyToString(SecretKey aesKey) {
//        return Base64.encodeToString(aesKey.getEncoded(), Base64.DEFAULT);
//    }

//    public static SecretKey generateSecretKey(String secretKeyString) throws UnsupportedEncodingException {
//        byte[] byteKey = Base64.decode(secretKeyString.getBytes("UTF-8"), Base64.DEFAULT);
//        return new SecretKeySpec(byteKey, 0, byteKey.length, AES);
//    }

//    public static String decryptWithSecretKey(String data, SecretKey secretKey, String ivString) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
//        byte[] encryptedDataBytes = Base64.decode(data.getBytes("UTF-8"), Base64.DEFAULT);
//        Cipher cipher = Cipher.getInstance(CIPHER_AES);
//        byte[] ivBytes = Base64.decode(ivString.getBytes("UTF-8"), Base64.DEFAULT);
//        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
//        cipher.init(Cipher.DECRYPT_MODE, secretKey,ivSpec);
//        byte[] decryptedBytes = cipher.doFinal(encryptedDataBytes);
//        String decryptedText = new String(decryptedBytes);
//        return decryptedText;
//    }

    public static IvParameterSpec generateIVSpecForAES() throws NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance(CIPHER_AES);
        SecureRandom random = new SecureRandom();
        byte[] realIV = new byte[cipher.getBlockSize()];
        random.nextBytes(realIV);
        return new IvParameterSpec(realIV);
    }

    public static String getUID(){
        return UUID.randomUUID().toString();
    }
}
