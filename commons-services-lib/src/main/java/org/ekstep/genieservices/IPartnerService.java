package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerData;

/**
 * Created on 5/10/2017.
 *
 * @author anil
 */
public interface IPartnerService {

    GenieResponse<Void> registerPartner(PartnerData partnerData);

    GenieResponse<Boolean> isRegistered(String partnerID);

    GenieResponse<Void> startPartnerSession(PartnerData partnerData);

    GenieResponse<Void> terminatePartnerSession(PartnerData partnerData);

    GenieResponse<String> sendData(PartnerData partnerData);
}
