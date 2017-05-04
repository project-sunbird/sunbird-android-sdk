package org.ekstep.genieservices.partner.db.model;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.util.Crypto;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on 4/5/17.
 *
 * @author shriharsh
 */

public class PartnerSessionModel {

    private static final String SHARED_PREF_SESSION_KEY = "partnersessionid";
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

    public static Set<String> findActivePartners(AppContext appContext) {
        return appContext.getKeyValueStore().getStringSet(ServiceConstants.PreferenceKey.PARTNER_ID, null);
    }

    public static String findPartnerSID(AppContext appContext) {
        return appContext.getKeyValueStore().getString(SHARED_PREF_SESSION_KEY, "");
    }

    public static Long findSessionLength(AppContext appContext) {
        Long length = 0L;
        Long t0 = appContext.getKeyValueStore().getLong("partnerSET", 0L);
        if (t0 != 0L) {
            length = getEpoch() - t0;
        }
        return length;
    }

    private static Long getEpoch() {
        Date date = new Date();
        return date.getTime();
    }

    public void save() {
        Logger.i(appContext, TAG, String.format("UPDATE SharedPreferences; partnerID: %s", partnerID));
        String sid = Crypto.getUID();
        Set<String> partnerSet = appContext.getKeyValueStore().getStringSet(ServiceConstants.PreferenceKey.PARTNER_ID, null);
        if (partnerSet == null) {
            partnerSet = new HashSet<String>(Arrays.asList(partnerID));
        } else {
            partnerSet.add(partnerID);
        }
        appContext.getKeyValueStore().putStringSet(ServiceConstants.PreferenceKey.PARTNER_ID, partnerSet);
        appContext.getKeyValueStore().putLong("partnerSET", getEpoch());
        appContext.getKeyValueStore().putString(SHARED_PREF_SESSION_KEY, sid);
    }

    public void clear() {
        Logger.i(appContext, TAG, String.format("CLEAR SharedPreferences; partnerID: %s", partnerID));

        Set<String> activePartners = appContext.getKeyValueStore().getStringSet(ServiceConstants.PreferenceKey.PARTNER_ID, null);
        activePartners.remove(partnerID);

        appContext.getKeyValueStore().putStringSet(ServiceConstants.PreferenceKey.PARTNER_ID, activePartners);
        appContext.getKeyValueStore().putLong("partnerSET", 0L);
        appContext.getKeyValueStore().putLong("partnerUNSET", getEpoch());
        appContext.getKeyValueStore().putString(SHARED_PREF_SESSION_KEY, "");
    }

}
