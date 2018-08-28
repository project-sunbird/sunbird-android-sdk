package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerData;

/**
 * This is the interface with all the required APIs for performing partner related
 * operations.
 */
public interface IPartnerService {

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
     * @param partnerData {@link PartnerData}
     * @return {@link GenieResponse<Void>}
     */
    GenieResponse<Void> registerPartner(PartnerData partnerData);

    /**
     * This api is used to check if the partnerId is already registered.
     * <p>
     * <p>Response status always be True, with result type as Boolean and will return TRUE if the partner is registered
     * or FALSE if the partner is not registered.
     *
     * @param partnerID - {@link String}
     * @return {@link GenieResponse<Boolean>}
     */
    GenieResponse<Boolean> isRegistered(String partnerID);

    /**
     * This api is used to start the Partner Session with the specified partnerID in PartnerData.
     * <p>
     * <p>On successful starting the session, the response will have status as TRUE and message as "successful".
     * <p>
     * <p>
     * <p>On failing to start the session, the response will have status as FALSE with the following error.
     * <p>UNREGISTERED_PARTNER - Session start failed! Partner: <partnerId> </p>
     *
     * @param partnerData - {@link PartnerData} - Required to get the related PartnerData to start the session
     * @return {@link GenieResponse<Void>}
     */
    GenieResponse<Void> startPartnerSession(PartnerData partnerData);

    /**
     * This api is used to terminate the Partner Session with the specified partnerID.
     * <p>
     * <p>On successful terminating the session, the response will have status as TRUE and message as "successful"
     * <p>
     * <p>
     * <p>On failing to start the session, the response will have status as FALSE with the following error.
     * <p>UNREGISTERED_PARTNER - Session start failed! Partner: <partnerId> </p>
     *
     * @param partnerData - {@link PartnerData} - Required to get the related PartnerData to start the session
     * @return {@link GenieResponse<Void>}
     */
    GenieResponse<Void> terminatePartnerSession(PartnerData partnerData);

    /**
     * This api is used to send Partner data specified in PartnerData. This api will encrypt the partnerData with the publicKey given earlier during registration.
     * <p>
     * <p>On successful sending the data, the response will have status as TRUE and result can be fetched from the response.
     * <p>
     * <p>On failing to send data, the response will return status as FALSE and the errors could be one of the following:
     * <p>UNREGISTERED_PARTNER - Sending data failed! Partner: <partnerId> </p>
     * <p>ENCRYPTION_FAILURE - Encrypting data failed! Partner: <partnerId> </p>
     *
     * @param partnerData - {@link PartnerData} -  Required to get the related PartnerData to send
     * @return {@link GenieResponse<String>}
     */
    GenieResponse<String> sendData(PartnerData partnerData);
}
