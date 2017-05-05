package org.ekstep.genieservices.partner;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.PartnerData;
import org.ekstep.genieservices.commons.exception.EncryptionException;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.partner.db.model.PartnerModel;
import org.ekstep.genieservices.partner.db.model.PartnerSessionModel;
import org.ekstep.genieservices.commons.utils.Base64;
import org.ekstep.genieservices.commons.utils.Crypto;

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

    public PartnerService(AppContext appContext) {
        super(appContext);
        this.appContext = appContext;
    }

    public void registerPartner(PartnerData partnerData, IResponseHandler<String> responseHandler) {
        Logger.i(appContext, TAG, "REGISTERING Partner: " + partnerData.getPartnerID());
        GenieResponse<String> genieResponse;

        PartnerModel partnerModel = PartnerModel.findByPartnerId(appContext.getDBSession(), partnerData.getPartnerID());
        if (partnerModel != null) {
            genieResponse = GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            responseHandler.onSuccess(genieResponse);
        } else {
            List<String> errors = isValid(partnerData);
            if (errors.isEmpty()) {
                partnerModel = PartnerModel.build(appContext.getDBSession(), partnerData.getPartnerID(), partnerData.getPublicKey(), getPublicKeyId(partnerData));
                partnerModel.save();
                // TODO: 2/5/17 Generate GERegisterPartner
                genieResponse = GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
                responseHandler.onSuccess(genieResponse);
            } else {
                String errorMessage = "INVALID_DATA";
                genieResponse = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, errorMessage, TAG);
                genieResponse.setErrorMessages(errors);
                responseHandler.onError(genieResponse);
            }
        }

    }

    private String getPublicKeyId(PartnerData request) {
        if (request.getPublicKey() != null) {
            try {
                return Crypto.checksum(request.getPublicKey());
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                Logger.e(appContext, TAG, "Bad Algorithm", e);
                return null;
            }
        }
        return null;
    }

    public void isRegistered(String partnerID, IResponseHandler<Boolean> responseHandler) {
        PartnerModel partnerModel = PartnerModel.findByPartnerId(appContext.getDBSession(), partnerID);
        GenieResponse<Boolean> response = GenieResponse.getSuccessResponse("");
        if (partnerModel != null) {
            response.setResult(true);
        }
        responseHandler.onSuccess(response);
    }

    public void startPartnerSession(PartnerData partnerData, IResponseHandler<String> responseHandler) {
        Logger.i(appContext, TAG, "STARTING Partner Session" + partnerData.getPartnerID());
        GenieResponse<String> genieResponse;
        PartnerModel partnerModel = PartnerModel.findByPartnerId(appContext.getDBSession(), partnerData.getPartnerID());

        if (partnerModel != null) {
            PartnerSessionModel partnerSessionModel = PartnerSessionModel.findPartnerSession(appContext);
            if (partnerSessionModel != null) {
                //TODO END Session telemetry to be added - use partnerSessionModel.getSessionLength() to get session length for telemetry
                partnerSessionModel.endSession();
            }
            partnerSessionModel = PartnerSessionModel.build(appContext, partnerData.getPartnerID());
            partnerSessionModel.startSession();
            genieResponse = GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            responseHandler.onSuccess(genieResponse);
        } else {
            Logger.e(appContext, TAG, UNREGISTERED_PARTNER);
            genieResponse = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, UNREGISTERED_PARTNER + String.format(Locale.US, "Session start failed! Partner: %s",
                    partnerData.getPartnerID()), TAG);
            responseHandler.onError(genieResponse);
        }
    }

    public void terminatePartnerSession(PartnerData partnerData, IResponseHandler<String> responseHandler) {
        String partnerID = partnerData.getPartnerID();
        Logger.i(appContext, TAG, "TERMINATING Partner Session" + partnerID);
        GenieResponse<String> genieResponse;
        PartnerModel partnerModel = PartnerModel.findByPartnerId(appContext.getDBSession(), partnerID);
        PartnerSessionModel partnerSessionModel = PartnerSessionModel.findPartnerSession(appContext);
        if (partnerModel != null && partnerSessionModel != null && partnerID.equals(partnerSessionModel.getPartnerID())) {
            partnerSessionModel.endSession();
            genieResponse = GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
            responseHandler.onSuccess(genieResponse);
        } else {
            genieResponse = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, UNREGISTERED_PARTNER + String.format(Locale.US, "Session termination failed! Partner: %s", partnerID), TAG);
            responseHandler.onError(genieResponse);
        }
    }

    public void sendData(PartnerData partnerData, IResponseHandler<String> responseHandler) {
        Logger.i(appContext, TAG, "SENDING Partner Data " + partnerData.getPartnerID());

        GenieResponse<String> genieResponse;
        PartnerModel partnerModel = PartnerModel.findByPartnerId(appContext.getDBSession(), partnerData.getPartnerID());

        if (partnerModel != null) {
            try {
                Map<String, String> data = processData(partnerData);
                sendEventData(data);
                genieResponse = GenieResponse.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
                genieResponse.setResult(data.toString());
                responseHandler.onSuccess(genieResponse);
            } catch (RuntimeException e) {
                Logger.e(appContext, TAG, CATASTROPHIC_FAILURE, e);
                List<String> errorMessages = new ArrayList<>();
                String errorMessage = e.getMessage();
                errorMessages.add(errorMessage);
                genieResponse = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, CATASTROPHIC_FAILURE + String.format(Locale.US, "Sending data failed! Partner: %s", partnerData.getPartnerID()), TAG);
                responseHandler.onError(genieResponse);
            } catch (EncryptionException e) {
                Logger.e(appContext, TAG, ENCRYPTION_FAILURE, e);
                List<String> errorMessages = new ArrayList<>();
                String errorMessage = e.getMessage();
                errorMessages.add(errorMessage);
                genieResponse = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, ENCRYPTION_FAILURE + String.format(Locale.US, "Encrypting data failed! Partner: %s", partnerData.getPartnerID()), TAG);
                responseHandler.onError(genieResponse);
            }
        } else {
            Logger.e(appContext, TAG, UNREGISTERED_PARTNER);
            genieResponse = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, UNREGISTERED_PARTNER + String.format(Locale.US, "Sending data failed! Partner: %s", partnerData.getPartnerID()), TAG);
            responseHandler.onError(genieResponse);
        }
    }

    private void sendEventData(Map<String, String> data) {
        // TODO: 2/5/17 Generate and save GESendPartnerData
    }

    private Map<String, String> processData(PartnerData partnerData) throws EncryptionException {
        Map<String, String> data = new HashMap<>();
        try {
            SecretKey AESKey = Crypto.generateAESKey(); //ok
            IvParameterSpec iv = Crypto.generateIVSpecForAES();
            String ivString = Base64.encodeToString(iv.getIV(), Base64.DEFAULT);
            PublicKey publicKey = Crypto.generatePublicKey(partnerData.getPublicKey());
            byte[] encryptedKey = Crypto.encryptSecretKeyWithRSAPublic(AESKey, publicKey);
            String encryptedKeyString = Base64.encodeToString(encryptedKey, Base64.DEFAULT);
            String encryptedData = Crypto.encryptWithAES(partnerData.getPartnerData(), AESKey, iv);
            data.put("iv", ivString);
            data.put("encrypted_data", encryptedData);
            data.put("encrypted_key", encryptedKeyString);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException | NoSuchPaddingException | UnsupportedEncodingException | InvalidAlgorithmParameterException e) {
            throw new EncryptionException(e.getMessage());
        }
        return data;
    }

     //TODO This should be moved into the bean class using the ivalidate interface
    private List<String> isValid(PartnerData request) {
        List<String> errorMessages = new ArrayList<String>();
        if (request.getPartnerID() == null || request.getPartnerID().isEmpty())
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
        return errorMessages;
    }

}
