package org.ekstep.genieservices.partner;

import org.ekstep.genieservices.BaseService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerDataRequest;
import org.ekstep.genieservices.commons.bean.PartnerRegistrationRequest;
import org.ekstep.genieservices.commons.bean.StartPartnerSessionRequest;
import org.ekstep.genieservices.commons.bean.TerminatePartnerSessionRequest;
import org.ekstep.genieservices.commons.db.DbConstants;
import org.ekstep.genieservices.commons.exception.DbException;
import org.ekstep.genieservices.commons.exception.EncryptionException;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.partner.db.model.PartnerModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    private AppContext appContext;

    public PartnerService(AppContext appContext) {
        super(appContext);
        this.appContext = appContext;
    }

    public GenieResponse<String> register(PartnerRegistrationRequest request) {
        Logger.i(appContext, TAG, "REGISTERING Partner: " + request.partnerID());
        GenieResponse<String> genieResponse = new GenieResponse<String>();

        try {
            PartnerModel partnerModel = PartnerModel.findByPartnerId(appContext.getDBSession(), appContext, request.partnerID(), request.publicKey());

            if (partnerModel.exists()) {
                Logger.i(appContext, TAG, "EXISTS Partner: " + request.partnerID());
            } else {
                Logger.i(appContext, TAG, "CREATING Partner: " + request.partnerID());
                partnerModel = PartnerModel.findByPartnerId(appContext.getDBSession(), appContext, request.partnerID(), request.publicKey());
                if (partnerModel.isValid()) {
                    partnerModel.save();
                    Logger.i(appContext, TAG, "CREATED Partner: " + request.partnerID());
                } else {
                    Logger.e(appContext, TAG, "INVALID PARTNER DATA" + request.partnerID());
                    List<String> errorMessages = partnerModel.getErrorMessages();
                    String errorMessage = "INVALID_DATA";
                    if (!errorMessages.isEmpty())
                        errorMessage = errorMessages.get(0);
                    genieResponse = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, errorMessage, TAG);
                }
            }
        } catch (DbException e) {
            genieResponse = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, DbConstants.ERROR, TAG);
            return genieResponse;
        }

        Logger.i(appContext, TAG, "REGISTERED Partner: " + request.partnerID());
        return genieResponse;
    }

    public GenieResponse<String> isRegistered(String partnerID) {
        GenieResponse<String> response = new GenieResponse<>();
        return response;
    }

    public GenieResponse<String> startPartnerSession(StartPartnerSessionRequest request) {
        Logger.i(appContext, TAG, "STARTING Partner Session" + request.partnerID());
        GenieResponse<String> genieResponse = new GenieResponse<String>();

        PartnerModel partnerModel = PartnerModel.findByPartnerId(appContext.getDBSession(), appContext, request.partnerID(), null);

        if (partnerModel.exists()) {
            Logger.i(appContext, TAG, "EXISTS Partner");
            Logger.i(appContext, TAG, "TERMINATING Partner Session" + request.partnerID());
            GenieResponse<String> response1 = new GenieResponse<>();

            PartnerModel partnerModel1 = PartnerModel.findByPartnerId(appContext.getDBSession(), appContext, request.partnerID(), null);
            if (partnerModel1.exists()) {
                Logger.i(appContext, TAG, "EXISTS Partner");
                partnerModel1.terminateSession();
            } else {
                Logger.e(appContext, TAG, UNREGISTERED_PARTNER);
                response1 = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, UNREGISTERED_PARTNER +
                        String.format(Locale.US, "Session termination failed! Partner: %s",
                                request.partnerID()), TAG);
            }
            Logger.i(appContext, TAG, "TERMINATED Partner Session " + request.partnerID());
            response1.toString();
            partnerModel.startSession();
        } else {
            Logger.e(appContext, TAG, UNREGISTERED_PARTNER);
            genieResponse = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, UNREGISTERED_PARTNER + String.format(Locale.US, "Session start failed! Partner: %s",
                    request.partnerID()), TAG);
        }

        Logger.i(appContext, TAG, "STARTED Partner Session" + request.partnerID());
        return genieResponse;
    }

    public GenieResponse<String> terminatePartnerSession(TerminatePartnerSessionRequest request) {
        String partnerID = request.partnerID();
        Logger.i(appContext, TAG, "TERMINATING Partner Session" + partnerID);
        GenieResponse<String> response = new GenieResponse<>();
        PartnerModel partnerModel = PartnerModel.findByPartnerId(appContext.getDBSession(), appContext, partnerID, null);
        if (partnerModel.exists()) {
            Logger.i(appContext, TAG, "EXISTS Partner");
            partnerModel.terminateSession();
        } else {
            Logger.e(appContext, TAG, UNREGISTERED_PARTNER);
            response = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, UNREGISTERED_PARTNER + String.format(Locale.US, "Session termination failed! Partner: %s", partnerID), TAG);
        }
        Logger.i(appContext, TAG, "TERMINATED Partner Session " + partnerID);
        return response;
    }

    public GenieResponse<String> sendData(PartnerDataRequest request) {
        Logger.i(appContext, TAG, "SENDING Partner Data " + request.partnerID());

        GenieResponse<String> genieResponse = new GenieResponse<>();
        PartnerModel partnerModel = PartnerModel.findByPartnerId(appContext.getDBSession(), appContext, request.partnerID(), null);

        if (partnerModel.exists()) {
            Logger.i(appContext, TAG, "EXISTS Partner");
            try {
                partnerModel.setData(request.partnerData());
                partnerModel.sendData();
                Map<String, Object> result = new HashMap<>();
                result.put("encrypted_data", partnerModel.getData());
                result.put("encrypted_key", partnerModel.getEncryptedKey());
                result.put("iv", partnerModel.getIV());
                genieResponse.setResult(result.toString());
            } catch (RuntimeException e) {
                Logger.e(appContext, TAG, CATASTROPHIC_FAILURE, e);
                List<String> errorMessages = new ArrayList<>();
                String errorMessage = e.getMessage();
                errorMessages.add(errorMessage);
                genieResponse = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, CATASTROPHIC_FAILURE + String.format(Locale.US, "Sending data failed! Partner: %s", request.partnerID()), TAG);
            } catch (EncryptionException e) {
                Logger.e(appContext, TAG, ENCRYPTION_FAILURE, e);
                List<String> errorMessages = new ArrayList<>();
                String errorMessage = e.getMessage();
                errorMessages.add(errorMessage);
                genieResponse = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, ENCRYPTION_FAILURE + String.format(Locale.US, "Encrypting data failed! Partner: %s", request.partnerID()), TAG);
            }

        } else {
            Logger.e(appContext, TAG, UNREGISTERED_PARTNER);
            genieResponse = GenieResponse.getErrorResponse(appContext, ServiceConstants.FAILED_RESPONSE, UNREGISTERED_PARTNER + String.format(Locale.US, "Sending data failed! Partner: %s", request.partnerID()), TAG);
        }

        return genieResponse;
    }


}
