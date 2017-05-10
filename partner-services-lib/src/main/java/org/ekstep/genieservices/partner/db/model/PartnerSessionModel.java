package org.ekstep.genieservices.partner.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.UUID;

/**
 * Created on 4/5/17.
 *
 * @author shriharsh
 */
public class PartnerSessionModel {

    private static final String SHARED_PREF_SESSION_KEY = "partnersessionid";
    private static final String SHARED_PREF_PARTNERSET_EPOCH = "partnerSET";
    private static final String SHARED_PREF_PARTNER_ID = "partnerid";

    private static final String TAG = PartnerSessionModel.class.getSimpleName();
    private AppContext appContext;
    private String partnerID;

    private PartnerSessionModel(AppContext appContext, String partnerID) {
        this.appContext = appContext;
        this.partnerID = partnerID;
    }

    public static PartnerSessionModel build(AppContext appContext, String partnerID) {
        PartnerSessionModel partnerSessionModel = new PartnerSessionModel(appContext, partnerID);
        return partnerSessionModel;
    }

    public static PartnerSessionModel findPartnerSession(AppContext appContext) {
        PartnerSessionModel partnerSessionModel = new PartnerSessionModel(appContext, null);
        partnerSessionModel.read();
        if (StringUtil.isNullOrEmpty(partnerSessionModel.partnerID)) {
            return null;
        } else {
            return partnerSessionModel;
        }
    }

    public Long getSessionLength() {
        Long length = 0L;
        Long t0 = appContext.getKeyValueStore().getLong(SHARED_PREF_PARTNERSET_EPOCH, 0L);
        if (t0 != 0L) {
            length = DateUtil.getEpochTime() - t0;
        }
        return length;
    }

    private void read() {
        this.partnerID = appContext.getKeyValueStore().getString(SHARED_PREF_PARTNER_ID, "");
    }

    public String getPartnerID() {
        return this.partnerID;
    }

    public void startSession() {
        appContext.getKeyValueStore().putString(SHARED_PREF_PARTNER_ID, partnerID);
        appContext.getKeyValueStore().putLong(SHARED_PREF_PARTNERSET_EPOCH, DateUtil.getEpochTime());
        appContext.getKeyValueStore().putString(SHARED_PREF_SESSION_KEY, UUID.randomUUID().toString());
    }

    public void endSession() {
        appContext.getKeyValueStore().putString(SHARED_PREF_PARTNER_ID, "");
        appContext.getKeyValueStore().remove(SHARED_PREF_PARTNERSET_EPOCH);
        appContext.getKeyValueStore().remove(SHARED_PREF_SESSION_KEY);
    }

}
