package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IPartnerService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerData;

public class PartnerService {
    private IPartnerService partnerService;

    public PartnerService(GenieService genieService) {
        this.partnerService = genieService.getPartnerService();
    }

    public void registerPartner(final PartnerData partnerData, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return partnerService.registerPartner(partnerData);
            }
        });
    }

    public void isRegistered(final String partnerID, IResponseHandler<Boolean> responseHandler) {
        new AsyncHandler<Boolean>(responseHandler).execute(new IPerformable<Boolean>() {
            @Override
            public GenieResponse<Boolean> perform() {
                return partnerService.isRegistered(partnerID);
            }
        });
    }

    public void startPartnerSession(final PartnerData partnerData, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return partnerService.startPartnerSession(partnerData);
            }
        });
    }

    public void terminatePartnerSession(final PartnerData partnerData, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return partnerService.terminatePartnerSession(partnerData);
            }
        });
    }

    public void sendData(final PartnerData partnerData, IResponseHandler<String> responseHandler) {
        new AsyncHandler<String>(responseHandler).execute(new IPerformable<String>() {
            @Override
            public GenieResponse<String> perform() {
                return partnerService.sendData(partnerData);
            }
        });
    }

}
