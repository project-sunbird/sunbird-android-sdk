package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerData;

/**
 * IPartnerService is the interface with all the required APIs for performing partner related
 * operations.
 *
 */
public interface IPartnerService {

    /**
     * This api is used for registering the partner.
     *
     * @param partnerData {@link PartnerData}
     * @return {@link GenieResponse<Void>}
     */
    GenieResponse<Void> registerPartner(PartnerData partnerData);

    /**
     * This api is used to check if the partnerId is already registered.
     *
     * @param partnerID
     * @return {@link GenieResponse<Boolean>}
     */
    GenieResponse<Boolean> isRegistered(String partnerID);

    /**
     * This api is used to start the Partner Session.
     *
     * @param partnerData Required to get the related PartnerData to start the session
     * @return {@link GenieResponse<Void>}
     */
    GenieResponse<Void> startPartnerSession(PartnerData partnerData);

    /**
     * This api is used to terminate the Partner Session.
     *
     * @param partnerData Required to get the related PartnerData to start the session
     * @return {@link GenieResponse<Void>}
     */
    GenieResponse<Void> terminatePartnerSession(PartnerData partnerData);

    /**
     * This api is used to send data.
     *
     * @param partnerData Required to get the related PartnerData to send
     * @return {@link GenieResponse<String>}
     */
    GenieResponse<String> sendData(PartnerData partnerData);
}
