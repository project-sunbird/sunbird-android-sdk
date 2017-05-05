package org.ekstep.genieservices.partner;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.PartnerData;
import org.ekstep.genieservices.commons.db.DbConstants;
import org.ekstep.genieservices.commons.exception.DbException;
import org.ekstep.genieservices.commons.exception.EncryptionException;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.partner.db.model.PartnerModel;
import org.ekstep.genieservices.util.Base64;
import org.ekstep.genieservices.util.Crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created on 28/4/17.
 *
 * @author shriharsh
 */

public class PartnerService extends BaseService {

    public static final String TAG = PartnerService.class.getSimpleName();
    public static final String UNREGISTERED_PARTNER = "UNREGISTERED_PARTNER";
    public static final String CATASTROPHIC_FAILURE = "CATASTROPHIC_FAILURE";
    public static final String ENCRYPTION_FAILURE = "ENCRYPTION_FAILURE";
    public static final String MISSING_PARTNER_ID = "MISSING_PARTNER_ID";
    public static final String MISSING_PUBLIC_KEY = "MISSING_PUBLIC_KEY";
    public static final String INVALID_RSA_PUBLIC_KEY = "INVALID_RSA_PUBLIC_KEY";
    private static final String TEST = "test";
    private AppContext appContext;
    private String data;
    private String encryptedKey;
    private String iv;
    private List<String> errorMessages;
    private String publicKeyID;


    public PartnerService(AppContext appContext) {
        super(appContext);
        this.appContext = appContext;
        errorMessages = new ArrayList<String>();
    }

    public void register(PartnerData request, IResponseHandler<String> responseHandler) {
        Logger.i(appContext, TAG, "REGISTERING Partner: " + request.partnerID());
        GenieResponse<String> genieResponse;

        try {
            PartnerModel partnerModel = PartnerModel.findByPartnerId(appContext.getDBSession(), appContext, request.partnerID(), request.getPublicKey(), getPublicKeyId(request));

            if (partnerModel.exists()) {
                Logger.i(appContext, TAG, "EXISTS Partner: " + request.partnerID());
                genieResponse = GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
                responseHandler.onSuccess(genieResponse);
            } else {
                Logger.i(appContext, TAG, "CREATING Partner: " + request.partnerID());
                if (isValid(request)) {
                    partnerModel.save();
                    Logger.i(appContext, TAG, "CREATED Partner: " + request.partnerID());
                    genieResponse = GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
                    responseHandler.onSuccess(genieResponse);
                } else {
                    Logger.e(appContext, TAG, "INVALID PARTNER DATA" + request.partnerID());
                    List<String> errorMessages = getErrorMessages();
                    String errorMessage = "INVALID_DATA";
                    if (!errorMessages.isEmpty())
                        errorMessage = errorMessages.get(0);
                    genieResponse = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, errorMessage, TAG);
                    responseHandler.onError(genieResponse);
                }
            }
        } catch (DbException e) {
            genieResponse = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, DbConstants.ERROR, TAG);
            responseHandler.onError(genieResponse);
        }

        Logger.i(appContext, TAG, "REGISTERED Partner: " + request.partnerID());

    }

    private String getPublicKeyId(PartnerData request) {
        if (request.getPublicKey() != null) {
            try {
                return this.publicKeyID = Crypto.checksum(request.getPublicKey());
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                Logger.e(appContext, TAG, "Bad Algorithm", e);
                return null;
            }
        }

        return null;
    }

    public void isRegistered(String partnerID, IResponseHandler<String> responseHandler) {
        GenieResponse<String> response = new GenieResponse<>();
        responseHandler.onSuccess(response);
    }

    public void startPartnerSession(PartnerData request, IResponseHandler<String> responseHandler) {
        Logger.i(appContext, TAG, "STARTING Partner Session" + request.partnerID());
        GenieResponse<String> genieResponse;

        PartnerModel partnerModel = PartnerModel.findByPartnerId(appContext.getDBSession(), appContext, request.partnerID(), null, getPublicKeyId(request));

        if (partnerModel.exists()) {
            Logger.i(appContext, TAG, "EXISTS Partner");
            Logger.i(appContext, TAG, "TERMINATING Partner Session" + request.partnerID());

            // TODO: 5/5/17 Need to check how to dissolve this code block as the logic is copied in the if-else block
            if (partnerModel.exists()) {
                Logger.i(appContext, TAG, "EXISTS Partner");
                partnerModel.terminateSession();
                genieResponse = GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
                responseHandler.onSuccess(genieResponse);
            } else {
                Logger.e(appContext, TAG, UNREGISTERED_PARTNER);
                genieResponse = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, UNREGISTERED_PARTNER +
                        String.format(Locale.US, "Session termination failed! Partner: %s",
                                request.partnerID()), TAG);
                responseHandler.onError(genieResponse);
            }
            Logger.i(appContext, TAG, "TERMINATED Partner Session " + request.partnerID());
            partnerModel.startSession();
        } else {
            Logger.e(appContext, TAG, UNREGISTERED_PARTNER);
            genieResponse = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, UNREGISTERED_PARTNER + String.format(Locale.US, "Session start failed! Partner: %s",
                    request.partnerID()), TAG);
            responseHandler.onError(genieResponse);
        }

        Logger.i(appContext, TAG, "STARTED Partner Session" + request.partnerID());
    }

    public void terminatePartnerSession(PartnerData request, IResponseHandler<String> responseHandler) {
        String partnerID = request.partnerID();
        Logger.i(appContext, TAG, "TERMINATING Partner Session" + partnerID);
        GenieResponse<String> genieResponse;
        PartnerModel partnerModel = PartnerModel.findByPartnerId(appContext.getDBSession(), appContext, partnerID, null, getPublicKeyId(request));
        if (partnerModel.exists()) {
            Logger.i(appContext, TAG, "EXISTS Partner");
            partnerModel.terminateSession();
            genieResponse = GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            responseHandler.onSuccess(genieResponse);
        } else {
            Logger.e(appContext, TAG, UNREGISTERED_PARTNER);
            genieResponse = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, UNREGISTERED_PARTNER + String.format(Locale.US, "Session termination failed! Partner: %s", partnerID), TAG);
            responseHandler.onError(genieResponse);
        }
        Logger.i(appContext, TAG, "TERMINATED Partner Session " + partnerID);
    }

    public void sendData(PartnerData request, IResponseHandler<String> responseHandler) {
        Logger.i(appContext, TAG, "SENDING Partner Data " + request.partnerID());

        GenieResponse<String> genieResponse;
        PartnerModel partnerModel = PartnerModel.findByPartnerId(appContext.getDBSession(), appContext, request.partnerID(), null, getPublicKeyId(request));

        if (partnerModel.exists()) {
            Logger.i(appContext, TAG, "EXISTS Partner");
            try {
                setData(request);
                sendEventData(request);
                Map<String, Object> result = new HashMap<>();
                result.put("encrypted_data", getData());
                result.put("encrypted_key", getEncryptedKey());
                result.put("iv", getIV());
                genieResponse = GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
                genieResponse.setResult(result.toString());
                responseHandler.onSuccess(genieResponse);
            } catch (RuntimeException e) {
                Logger.e(appContext, TAG, CATASTROPHIC_FAILURE, e);
                List<String> errorMessages = new ArrayList<>();
                String errorMessage = e.getMessage();
                errorMessages.add(errorMessage);
                genieResponse = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, CATASTROPHIC_FAILURE + String.format(Locale.US, "Sending data failed! Partner: %s", request.partnerID()), TAG);
                responseHandler.onError(genieResponse);
            } catch (EncryptionException e) {
                Logger.e(appContext, TAG, ENCRYPTION_FAILURE, e);
                List<String> errorMessages = new ArrayList<>();
                String errorMessage = e.getMessage();
                errorMessages.add(errorMessage);
                genieResponse = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, ENCRYPTION_FAILURE + String.format(Locale.US, "Encrypting data failed! Partner: %s", request.partnerID()), TAG);
                responseHandler.onError(genieResponse);
            }
        } else {
            Logger.e(appContext, TAG, UNREGISTERED_PARTNER);
            genieResponse = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, UNREGISTERED_PARTNER + String.format(Locale.US, "Sending data failed! Partner: %s", request.partnerID()), TAG);
            responseHandler.onError(genieResponse);
        }
    }

    private void sendEventData(PartnerData partnerData) {
        Logger.i(appContext, TAG, "SEND DATA Partner: " + partnerData.partnerID());
        // TODO: 2/5/17 Generate and save GESendPartnerData
    }

    private String getData() {
        return this.data;
    }

    private void setData(PartnerData partnerData) throws EncryptionException {
        Logger.i(appContext, TAG, "--> ENCRYPTING");
        try {
            SecretKey AESKey = Crypto.generateAESKey(); //ok
            IvParameterSpec iv = Crypto.generateIVSpecForAES();
            String ivString = Base64.encodeToString(iv.getIV(), Base64.DEFAULT);
            PublicKey publicKey = Crypto.generatePublicKey(partnerData.getPublicKey());
            byte[] encryptedKey = Crypto.encryptSecretKeyWithRSAPublic(AESKey, publicKey);
            String encryptedKeyString = Base64.encodeToString(encryptedKey, Base64.DEFAULT);
            String encryptedData = encryptDataWithAES(partnerData.partnerData(), AESKey, iv);
            this.iv = ivString;
            Logger.i(appContext, TAG, "IV:" + ivString);
            this.data = encryptedData;
            this.encryptedKey = encryptedKeyString;
        } catch (NoSuchAlgorithmException e) {
            Logger.e(appContext, TAG, "NoSuchAlgorithmException", e);
            throw new EncryptionException(e.getMessage());
        } catch (InvalidKeySpecException e) {
            Logger.e(appContext, TAG, "InvalidKeySpecException", e);
            throw new EncryptionException(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            Logger.e(appContext, TAG, "IllegalBlockSizeException", e);
            throw new EncryptionException(e.getMessage());
        } catch (InvalidKeyException e) {
            Logger.e(appContext, TAG, "InvalidKeyException", e);
            throw new EncryptionException(e.getMessage());
        } catch (BadPaddingException e) {
            Logger.e(appContext, TAG, "BadPaddingException", e);
            throw new EncryptionException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            Logger.e(appContext, TAG, "NoSuchPaddingException", e);
            throw new EncryptionException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            Logger.e(appContext, TAG, "UnsupportedEncodingException", e);
            throw new EncryptionException(e.getMessage());
        }
        Logger.i(appContext, TAG, "<-- ENCRYPTED");
    }

    private String getEncryptedKey() {
        return this.encryptedKey;
    }

    private String getIV() {
        return this.iv;
    }

    private String encryptDataWithAES(String data, SecretKey AESKey, IvParameterSpec iv) throws EncryptionException {
        try {
            return Crypto.encryptWithAES(data, AESKey, iv);
        } catch (InvalidKeyException e) {
            Logger.e(appContext, TAG, "InvalidKeyException", e);
            throw new EncryptionException(e.getMessage());
        } catch (BadPaddingException e) {
            Logger.e(appContext, TAG, "BadPaddingException", e);
            throw new EncryptionException(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            Logger.e(appContext, TAG, "IllegalBlockSizeException", e);
            throw new EncryptionException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Logger.e(appContext, TAG, "NoSuchAlgorithmException", e);
            throw new EncryptionException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            Logger.e(appContext, TAG, "NoSuchPaddingException", e);
            throw new EncryptionException(e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            Logger.e(appContext, TAG, "InvalidAlgorithmParameterException", e);
            throw new EncryptionException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            Logger.e(appContext, TAG, "UnsupportedEncodingException", e);
            throw new EncryptionException(e.getMessage());
        }
    }


    private String encryptDataWithPublic(byte[] data, String publicKey) throws EncryptionException {
        try {
            return Crypto.encryptWithPublic(data, publicKey);
        } catch (InvalidKeyException e) {
            Logger.e(appContext, TAG, "InvalidKeyException", e);
            throw new EncryptionException(e.getMessage());
        } catch (InvalidKeySpecException e) {
            Logger.e(appContext, TAG, "InvalidKeySpecException", e);
            throw new EncryptionException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            Logger.e(appContext, TAG, "UnsupportedEncodingException", e);
            throw new EncryptionException(e.getMessage());
        } catch (BadPaddingException e) {
            Logger.e(appContext, TAG, "BadPaddingException", e);
            throw new EncryptionException(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            Logger.e(appContext, TAG, "IllegalBlockSizeException", e);
            throw new EncryptionException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Logger.e(appContext, TAG, "NoSuchAlgorithmException", e);
            throw new EncryptionException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            Logger.e(appContext, TAG, "NoSuchPaddingException", e);
            throw new EncryptionException(e.getMessage());
        }
    }

    private Boolean isValid(PartnerData request) {
        if (request.partnerID() == null || request.partnerID().isEmpty())
            errorMessages.add(MISSING_PARTNER_ID);
        if (request.getPublicKey() == null || request.getPublicKey().isEmpty())
            errorMessages.add(MISSING_PUBLIC_KEY);
        try {
            Crypto.encryptWithPublic(TEST, request.getPublicKey());
            Crypto.checksum(request.getPublicKey());
        } catch (Exception e) {
            Logger.e(appContext, TAG, "ERROR Bad Public Key!", e);
            errorMessages.add(INVALID_RSA_PUBLIC_KEY);
        }
        Boolean result = errorMessages.isEmpty();
        Logger.i(appContext, TAG, "Valid? " + result);
        Logger.i(appContext, TAG, "Errors: " + errorMessages.toString());
        return result;
    }

    private List<String> getErrorMessages() {
        return errorMessages;
    }
}
