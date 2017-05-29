package org.ekstep.genieresolvers.partner;

import android.content.Context;

import org.ekstep.genieresolvers.BaseService;
import org.ekstep.genieresolvers.BaseTask;
import org.ekstep.genieresolvers.TaskHandler;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.PartnerData;

/**
 * Created on 24/5/17.
 * shriharsh
 */

public class PartnerService extends BaseService{

    private String appQualifier;
    private Context context;

    public PartnerService(Context context, String appQualifier) {
        this.context = context;
        this.appQualifier = appQualifier;
    }

    public void registerPartner(PartnerData partnerData, IResponseHandler responseHandler) {
        RegisterPartnerTask registerPartnerTask = new RegisterPartnerTask(context, appQualifier, partnerData);
        createAndExecuteTask(responseHandler, registerPartnerTask);
    }

    public void startPartnerSession(PartnerData partnerData, IResponseHandler responseHandler) {
        StartPartnerSessionTask startPartnerSessionTask = new StartPartnerSessionTask(context, appQualifier, partnerData);
        createAndExecuteTask(responseHandler, startPartnerSessionTask);
    }

    public void endPartnerSession(PartnerData partnerData, IResponseHandler responseHandler) {
        EndPartnerSessionTask endPartnerSessionTask = new EndPartnerSessionTask(context, appQualifier, partnerData);
        createAndExecuteTask(responseHandler, endPartnerSessionTask);
    }

    public void sendPartnerData(PartnerData partnerData, IResponseHandler responseHandler) {
        SendPartnerDataTask sendPartnerDataTask = new SendPartnerDataTask(context, appQualifier, partnerData);
        createAndExecuteTask(responseHandler, sendPartnerDataTask);
    }
}
