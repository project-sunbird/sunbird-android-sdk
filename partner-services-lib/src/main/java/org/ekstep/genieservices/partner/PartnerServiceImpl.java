package org.ekstep.genieservices.partner;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.IPartnerService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerData;
import org.ekstep.genieservices.commons.bean.telemetry.Actor;
import org.ekstep.genieservices.commons.bean.telemetry.Audit;
import org.ekstep.genieservices.commons.bean.telemetry.ExData;
import org.ekstep.genieservices.commons.exception.EncryptionException;
import org.ekstep.genieservices.commons.utils.Base64Util;
import org.ekstep.genieservices.commons.utils.CryptoUtil;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.partner.db.model.PartnerModel;
import org.ekstep.genieservices.partner.db.model.PartnerSessionModel;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

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
 * This is the implementation of the interface {@link IPartnerService}
 */
public class PartnerServiceImpl extends BaseService implements IPartnerService {

    public static final String TAG = PartnerServiceImpl.class.getSimpleName();

    private static final String TEST = "test";
    private AppContext appContext;

    /**
     * Constructor of PartnerServiceImpl
     *
     * @param appContext {@link AppContext}
     */
    public PartnerServiceImpl(AppContext appContext) {
        super(appContext);
        this.appContext = appContext;
    }

    @Override
    public GenieResponse<Void> registerPartner(PartnerData partnerData) {
        Logger.i(TAG, "REGISTERING Partner: " + partnerData.getPartnerID());
        GenieResponse<Void> response;

        PartnerModel partnerModel = PartnerModel.findByPartnerId(appContext.getDBSession(), partnerData.getPartnerID());
        if (partnerModel != null) {
            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Void.class);
            return response;
        } else {
            List<String> errors = isValid(partnerData);
            if (errors.isEmpty()) {
                partnerModel = PartnerModel.build(appContext.getDBSession(), partnerData.getPartnerID(), partnerData.getPublicKey(), getPublicKeyId(partnerData));
                partnerModel.save();

                Map<String, Object> map = new HashMap<>();
                map.put("partnerid", partnerData.getPartnerID());
                map.put("publickey", partnerData.getPublicKey());
                map.put("did", mAppContext.getDeviceInfo().getDeviceID());

                Audit audit = new Audit(null, GsonUtil.toJson(map), null, Actor.TYPE_SYSTEM);
                TelemetryLogger.log(audit);

                response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Void.class);
                TelemetryLogger.logSuccess(mAppContext, response, TAG, "registerPartner@PartnerServiceImpl", new HashMap<String, Object>());
                return response;
            } else {
                String errorMessage = "INVALID_DATA";
                response = GenieResponseBuilder.getErrorResponse(ServiceConstants.FAILED_RESPONSE, errorMessage, TAG, Void.class);
                response.setErrorMessages(errors);

                TelemetryLogger.logFailure(mAppContext, response, TAG, "registerPartner@PartnerServiceImpl", new HashMap<String, Object>(), errorMessage);
                return response;
            }
        }
    }

    private String getPublicKeyId(PartnerData request) {
        if (request.getPublicKey() != null) {
            try {
                return CryptoUtil.checksum(request.getPublicKey());
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                Logger.e(TAG, "Bad Algorithm", e);
                return null;
            }
        }
        return null;
    }

    @Override
    public GenieResponse<Boolean> isRegistered(String partnerID) {
        PartnerModel partnerModel = PartnerModel.findByPartnerId(appContext.getDBSession(), partnerID);
        GenieResponse<Boolean> response = GenieResponseBuilder.getSuccessResponse("", Boolean.class);
        if (partnerModel != null) {
            response.setResult(true);
        } else {
            response.setResult(false);
        }

        return response;
    }

    @Override
    public GenieResponse<Void> startPartnerSession(PartnerData partnerData) {
        String methodName = "startPartnerSession@PartnerServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("partnerData", GsonUtil.toJson(partnerData));
        params.put("logLevel", "2");
        Logger.i(TAG, "STARTING Partner Session" + partnerData.getPartnerID());
        GenieResponse<Void> response;
        PartnerModel partnerModel = PartnerModel.findByPartnerId(appContext.getDBSession(), partnerData.getPartnerID());

        if (partnerModel != null) {
            PartnerSessionModel partnerSessionModel = PartnerSessionModel.find(appContext);
            if (partnerSessionModel != null) {

                Map<String, Object> map = new HashMap<>();
                map.put("action", "Partner-Session-Terminated");
                map.put("partnerid", partnerSessionModel.getPartnerID());
                map.put("did", mAppContext.getDeviceInfo().getDeviceID());
                map.put("duration", DateUtil.getEpochDiff(partnerSessionModel.getEpochTime()));
                map.put("partnersessionid", partnerSessionModel.getPartnerSessionId());

                Audit audit = new Audit(null, GsonUtil.toJson(map), null, Actor.TYPE_SYSTEM);
                TelemetryLogger.log(audit);

                partnerSessionModel.clear();
            }
            partnerSessionModel = PartnerSessionModel.build(appContext, partnerData.getPartnerID());
            partnerSessionModel.save();


            Map<String, Object> map = new HashMap<>();
            map.put("action", "Partner-Session-Started");
            map.put("partnerid", partnerSessionModel.getPartnerID());
            map.put("did", mAppContext.getDeviceInfo().getDeviceID());
            map.put("partnersessionid", partnerSessionModel.getPartnerSessionId());

            Audit audit = new Audit(null, GsonUtil.toJson(map), null, Actor.TYPE_SYSTEM);
            TelemetryLogger.log(audit);

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Void.class);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
            return response;
        } else {
            String errorMessage = String.format(Locale.US, "- Session start failed! Partner: %s", partnerData.getPartnerID());
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.UNREGISTERED_PARTNER, errorMessage, TAG, Void.class);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, errorMessage);
            return response;
        }
    }

    @Override
    public GenieResponse<Void> terminatePartnerSession(PartnerData partnerData) {
        String methodName = "terminatePartnerSession@PartnerServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("partnerData", GsonUtil.toJson(partnerData));
        params.put("logLevel", "2");
        String partnerID = partnerData.getPartnerID();
        Logger.i(TAG, "TERMINATING Partner Session" + partnerID);

        GenieResponse<Void> response;
        PartnerModel partnerModel = PartnerModel.findByPartnerId(appContext.getDBSession(), partnerID);
        PartnerSessionModel partnerSessionModel = PartnerSessionModel.find(appContext);
        if (partnerModel != null && partnerSessionModel != null && partnerID.equals(partnerSessionModel.getPartnerID())) {
            partnerSessionModel.clear();

            Map<String, Object> map = new HashMap<>();
            map.put("action", "Partner-Session-Terminated");
            map.put("partnerid", partnerSessionModel.getPartnerID());
            map.put("did", mAppContext.getDeviceInfo().getDeviceID());
            map.put("duration", DateUtil.getEpochDiff(partnerSessionModel.getEpochTime()));
            map.put("partnersessionid", partnerSessionModel.getPartnerSessionId());

            Audit audit = new Audit(null, GsonUtil.toJson(map), null, Actor.TYPE_SYSTEM);
            TelemetryLogger.log(audit);

            response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, Void.class);
            TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
            return response;
        } else {
            String errorMessage = String.format(Locale.US, "- Session termination failed! Partner: %s", partnerID);
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.UNREGISTERED_PARTNER, errorMessage, TAG, Void.class);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, errorMessage);
            return response;
        }
    }

    @Override
    public GenieResponse<String> sendData(PartnerData partnerData) {
        String methodName = "sendData@PartnerServiceImpl";
        Map<String, Object> params = new HashMap<>();
        params.put("partnerData", GsonUtil.toJson(partnerData));
        params.put("logLevel", "2");
        Logger.i(TAG, "SENDING Partner Data " + partnerData.getPartnerID());

        GenieResponse<String> response;
        PartnerModel partnerModel = PartnerModel.findByPartnerId(appContext.getDBSession(), partnerData.getPartnerID());

        if (partnerModel != null) {
            try {
                Map<String, String> data = processData(partnerData);

                Map<String, String> eventMap = new HashMap<>();
                eventMap.put("partnerid", partnerModel.getPartnerID());
                eventMap.put("publickeyid", partnerModel.getPublicKeyId());
                eventMap.put("data", data.get("encrypted_data"));
                eventMap.put("key", data.get("encrypted_key"));
                eventMap.put("iv", data.get("iv"));
                ExData exData = new ExData("partnerdata", GsonUtil.toJson(eventMap));
                TelemetryLogger.log(exData);

                response = GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE, String.class);
                response.setResult(data.toString());
                TelemetryLogger.logSuccess(mAppContext, response, TAG, methodName, params);
                return response;
            } catch (EncryptionException e) {
                String message = String.format(Locale.US, "- Encrypting data failed! Partner: %s", partnerData.getPartnerID());
                Logger.e(TAG, message, e);
                response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.ENCRYPTION_FAILURE, message, TAG, String.class);
                TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, params, message);
                return response;
            }
        } else {
            String errorMessage = String.format(Locale.US, "- Sending data failed! Partner: %s", partnerData.getPartnerID());
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.UNREGISTERED_PARTNER, errorMessage, TAG, String.class);
            TelemetryLogger.logFailure(mAppContext, response, TAG, methodName, new HashMap<String, Object>(), errorMessage);
            return response;
        }
    }


    private Map<String, String> processData(PartnerData partnerData) throws EncryptionException {
        Map<String, String> data = new HashMap<>();
        try {
            SecretKey AESKey = CryptoUtil.generateAESKey(); //ok
            IvParameterSpec iv = CryptoUtil.generateIVSpecForAES();
            String ivString = Base64Util.encodeToString(iv.getIV(), Base64Util.DEFAULT);
            PublicKey publicKey = CryptoUtil.generatePublicKey(partnerData.getPublicKey());
            byte[] encryptedKey = CryptoUtil.encryptSecretKeyWithRSAPublic(AESKey, publicKey);
            String encryptedKeyString = Base64Util.encodeToString(encryptedKey, Base64Util.DEFAULT);
            String encryptedData = CryptoUtil.encryptWithAES(partnerData.getPartnerData(), AESKey, iv);
            data.put("iv", ivString);
            data.put("encrypted_data", encryptedData);
            data.put("encrypted_key", encryptedKeyString);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException | NoSuchPaddingException | UnsupportedEncodingException | InvalidAlgorithmParameterException e) {
            throw new EncryptionException(e.getMessage());
        }
        return data;
    }

    //TODO This should be moved into the bean class using the IValidate interface
    private List<String> isValid(PartnerData request) {
        List<String> errorMessages = new ArrayList<>();
        if (request.getPartnerID() == null || request.getPartnerID().isEmpty())
            errorMessages.add(ServiceConstants.ErrorCode.MISSING_PARTNER_ID);
        if (request.getPublicKey() == null || request.getPublicKey().isEmpty())
            errorMessages.add(ServiceConstants.ErrorCode.MISSING_PUBLIC_KEY);
        try {
            CryptoUtil.encryptWithPublic(TEST, request.getPublicKey());
            CryptoUtil.checksum(request.getPublicKey());
        } catch (Exception e) {
            Logger.e(TAG, "ERROR Bad Public Key!", e);
            errorMessages.add(ServiceConstants.ErrorCode.INVALID_RSA_PUBLIC_KEY);
        }
        return errorMessages;
    }

}
