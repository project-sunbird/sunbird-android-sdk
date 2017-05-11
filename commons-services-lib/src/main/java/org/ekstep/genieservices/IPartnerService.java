package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerData;

/**
 * IPartnerService is the interface with all the required APIs for performing partner related
 * operations.
 */
public interface IPartnerService {

    /**
     * This api is used to register a partner with the specified partnerID and publicKey in PartnerData.
     * <p>
     * <p>In case of a success response, response.getStatus() will return TRUE and response.getMessage() will return the string "successful"
     * <p>
     * <p>In case of a failed response, response.getStatus() will return FALSE and response.getError() could return one of the following:
     * <p>MISSING_PARTNER_ID - partnerID is not provided</p>
     * <p>MISSING_PUBLIC_KEY - publicKey is not provided</p>
     * <p>INVALID_RSA_PUBLIC_KEY - publicKey is not as per specs</p>
     * <p>INVALID_DATA - some data is still invalid</p>
     *
     * @param partnerData {@link PartnerData}
     * @return {@link GenieResponse<Void>}
     */
    GenieResponse<Void> registerPartner(PartnerData partnerData);

    /**
     * This api is used to check if the partnerId is already registered.
     * <p>
     * <p>Response will always be success response, but response.getResult() in Boolean type will return TRUE if the partner is registered
     * or FALSE if the partner is not registered.
     *
     * @param partnerID
     * @return {@link GenieResponse<Boolean>}
     */
    GenieResponse<Boolean> isRegistered(String partnerID);

    /**
     * This api is used to start the Partner Session with the specified partnerID in PartnerData.
     * <p>
     * <p>In case of a success response, response.getStatus() will return TRUE and response.getMessage() will return the string "successful"
     * <p>
     * <p>
     * <p>In case of a failed response, response.getStatus() will return FALSE and response.getError() will return the following:
     * <p>UNREGISTERED_PARTNER - Session start failed! Partner: <partnerId> </p>
     *
     * @param partnerData Required to get the related PartnerData to start the session
     * @return {@link GenieResponse<Void>}
     */
    GenieResponse<Void> startPartnerSession(PartnerData partnerData);

    /**
     * This api is used to terminate the Partner Session with the specified partnerID.
     * <p>
     * <p>In case of a success response, response.getStatus() will return TRUE and response.getMessage() will return the string "successful"
     * <p>
     * <p>
     * <p>In case of a failed response, response.getStatus() will return FALSE and response.getError() will return the following:
     * <p>UNREGISTERED_PARTNER - Session termination failed! Partner: <partnerId> </p>
     *
     * @param partnerData Required to get the related PartnerData to start the session
     * @return {@link GenieResponse<Void>}
     */
    GenieResponse<Void> terminatePartnerSession(PartnerData partnerData);

    /**
     * This api is used to send Partner data specified in PartnerData. This api will encrypt the partnerData with the publicKey given earlier during registration.
     * <p>
     * <p>In case of a success response, response.getStatus() will return TRUE and response.getResult() will return the response string.
     * <p>
     * <p> In case of a failed response, response.getError() could return one
     * of the following:
     * <p>
     * <p>
     * <p>UNREGISTERED_PARTNER - Sending data failed! Partner: <partnerId> </p>
     * <p>ENCRYPTION_FAILURE - Encrypting data failed! Partner: <partnerId> </p>
     *
     * @param partnerData Required to get the related PartnerData to send
     * @return {@link GenieResponse<String>}
     */
    GenieResponse<String> sendData(PartnerData partnerData);
}
