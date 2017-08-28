package org.ekstep.genieresolvers.partner;

import android.content.Context;

import org.ekstep.genieresolvers.BaseService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.PartnerData;

/**
 * This is the {@link PartnerService} class with all the required APIs for performing partner related operations.
 */
public class PartnerService extends BaseService {

    private String appQualifier;
    private Context context;

    public PartnerService(Context context, String appQualifier) {
        this.context = context;
        this.appQualifier = appQualifier;
    }

    /**
     * This api is used to register a partner with the specified partnerID and publicKey in PartnerData.
     * <p>
     * <p>On successful registering the partner, the response will return status as TRUE and with "successful" message.
     * <p>
     * <p>On failing to register the partner, the response will return status as FALSE and the errors could be one of the following:
     * <p>MISSING_PARTNER_ID - partnerID is not provided</p>
     * <p>MISSING_PUBLIC_KEY - publicKey is not provided</p>
     * <p>INVALID_RSA_PUBLIC_KEY - publicKey is not as per specs</p>
     * <p>INVALID_DATA - some data is still invalid</p>
     *
     * @param partnerData
     * @param responseHandler
     */
    public void registerPartner(PartnerData partnerData, IResponseHandler responseHandler) {
        RegisterPartnerTask registerPartnerTask = new RegisterPartnerTask(context, appQualifier, partnerData);
        createAndExecuteTask(responseHandler, registerPartnerTask);
    }

    /**
     * This api is used to start the Partner Session with the specified partnerID in PartnerData.
     * <p>
     * <p>On successful starting the session, the response will have status as TRUE and message as "successful".
     * <p>
     * <p>
     * <p>On failing to start the session, the response will have status as FALSE with the following error.
     * <p>UNREGISTERED_PARTNER - Session start failed! Partner: <partnerId> </p>
     *
     * @param partnerData
     * @param responseHandler
     */
    public void startPartnerSession(PartnerData partnerData, IResponseHandler responseHandler) {
        StartPartnerSessionTask startPartnerSessionTask = new StartPartnerSessionTask(context, appQualifier, partnerData);
        createAndExecuteTask(responseHandler, startPartnerSessionTask);
    }

    /**
     * This api is used to end the Partner Session with the specified partnerID in PartnerData.
     * <p>
     * <p>On successful terminating the session, the response will have status as TRUE and message as "successful"
     * <p>
     * <p>
     * <p>On failing to start the session, the response will have status as FALSE with the following error.
     * <p>UNREGISTERED_PARTNER - Session start failed! Partner: <partnerId> </p>
     *
     * @param partnerData
     * @param responseHandler
     */
    public void endPartnerSession(PartnerData partnerData, IResponseHandler responseHandler) {
        EndPartnerSessionTask endPartnerSessionTask = new EndPartnerSessionTask(context, appQualifier, partnerData);
        createAndExecuteTask(responseHandler, endPartnerSessionTask);
    }

    /**
     * This api is used to send Partner data specified in PartnerData. This api will encrypt the partnerData with the publicKey given earlier during registration.
     * <p>
     * <p>On successful sending the data, the response will have status as TRUE and result can be fetched from the response.
     * <p>
     * <p>On failing to send data, the response will return status as FALSE and the errors could be one of the following:
     * <p>UNREGISTERED_PARTNER - Sending data failed! Partner: <partnerId> </p>
     * <p>ENCRYPTION_FAILURE - Encrypting data failed! Partner: <partnerId> </p>
     *
     * @param partnerData
     * @param responseHandler
     */
    public void sendPartnerData(PartnerData partnerData, IResponseHandler responseHandler) {
        SendPartnerDataTask sendPartnerDataTask = new SendPartnerDataTask(context, appQualifier, partnerData);
        createAndExecuteTask(responseHandler, sendPartnerDataTask);
    }
}
