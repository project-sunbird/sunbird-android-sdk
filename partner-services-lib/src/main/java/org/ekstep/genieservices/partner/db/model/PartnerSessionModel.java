package org.ekstep.genieservices.partner.db.model;

import org.ekstep.genieservices.ServiceConstants;
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


    private static final String TAG = PartnerSessionModel.class.getSimpleName();
    private AppContext appContext;
    private String partnerID;
    private Long epochTime;
    private String sessionId;

    private PartnerSessionModel(AppContext appContext, String partnerID) {
        this.appContext = appContext;
        this.partnerID = partnerID;
    }

    public static PartnerSessionModel build(AppContext appContext, String partnerID) {
        PartnerSessionModel partnerSessionModel = new PartnerSessionModel(appContext, partnerID);
        return partnerSessionModel;
    }

    public static PartnerSessionModel find(AppContext appContext) {
        PartnerSessionModel partnerSessionModel = new PartnerSessionModel(appContext, null);
        partnerSessionModel.read();
        if (StringUtil.isNullOrEmpty(partnerSessionModel.partnerID)) {
            return null;
        } else {
            return partnerSessionModel;
        }
    }

    private void read() {
        this.partnerID = appContext.getKeyValueStore().getString(ServiceConstants.PreferenceKey.KEY_ACTIVE_PARTNER_ID, "");
        this.epochTime = appContext.getKeyValueStore().getLong(ServiceConstants.PreferenceKey.SHARED_PREF_PARTNERSET_EPOCH, 0L);
        this.sessionId = appContext.getKeyValueStore().getString(ServiceConstants.PreferenceKey.SHARED_PREF_SESSION_KEY, "");
    }


    public void save() {
        appContext.getKeyValueStore().putString(ServiceConstants.PreferenceKey.KEY_ACTIVE_PARTNER_ID, partnerID);
        appContext.getKeyValueStore().putLong(ServiceConstants.PreferenceKey.SHARED_PREF_PARTNERSET_EPOCH, DateUtil.getEpochTime());
        appContext.getKeyValueStore().putString(ServiceConstants.PreferenceKey.SHARED_PREF_SESSION_KEY, UUID.randomUUID().toString());
    }

    public void clear() {
        appContext.getKeyValueStore().remove(ServiceConstants.PreferenceKey.KEY_PARTNER_ID);
        appContext.getKeyValueStore().putString(ServiceConstants.PreferenceKey.KEY_ACTIVE_PARTNER_ID, "");
        appContext.getKeyValueStore().remove(ServiceConstants.PreferenceKey.SHARED_PREF_PARTNERSET_EPOCH);
        appContext.getKeyValueStore().remove(ServiceConstants.PreferenceKey.SHARED_PREF_SESSION_KEY);
    }

    public String getPartnerID() {
        return this.partnerID;
    }

    public String getPartnerSessionId() {
        return appContext.getKeyValueStore().getString(ServiceConstants.PreferenceKey.SHARED_PREF_SESSION_KEY, "");
    }

    public Long getEpochTime() {
        return epochTime;
    }

}
