package org.ekstep.genieservices.partner.db.model;


import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.partner.db.contract.PartnerEntry;

import java.util.Locale;
import java.util.Set;

public class PartnerModel implements IWritable, IReadable {
    private static final String TAG = "model-Partner";
    private String publicKeyId;
    private String partnerID;
    private String publicKey;
    private ContentValues contentValues;
    private AppContext appContext;
    private IDBSession idbSession;

    private PartnerModel(String partnerID, String publicKey, AppContext appContext, IDBSession idbSession, String publicKeyId) {
        this.partnerID = partnerID;
        this.appContext = appContext;
        this.idbSession = idbSession;
        this.publicKey = publicKey;
        contentValues = new ContentValues();
        this.publicKeyId = publicKeyId;
    }

    public static PartnerModel findByPartnerId(IDBSession idbSession, AppContext appContext, String partnerID, String publicKey, String publicKeyId) {
        PartnerModel partnerModel = new PartnerModel(partnerID, publicKey, appContext, idbSession, publicKeyId);
        idbSession.read(partnerModel);
        return partnerModel;
    }

    @Override
    public ContentValues getContentValues() {
        contentValues.clear();
        contentValues.put(PartnerEntry.COLUMN_NAME_UID, partnerID);
        contentValues.put(PartnerEntry.COLUMN_NAME_KEY, publicKey);
        contentValues.put(PartnerEntry.COLUMN_NAME_KEY_ID, publicKeyId);
        return contentValues;
    }

    @Override
    public void updateId(long id) {

    }

    public boolean exists() {
        return (partnerID != null && !partnerID.isEmpty());
    }

    @Override
    public IReadable read(IResultSet cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            partnerID = cursor.getString(1);
            publicKey = cursor.getString(2);
            publicKeyId = cursor.getString(3);
        } else
            partnerID = "";
        return this;
    }

    @Override
    public String getTableName() {
        return PartnerEntry.TABLE_NAME;
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {
        Logger.i(appContext, TAG, String.format("SEARCH partnerID: %s", partnerID));
        String selectionCriteria = String.format(Locale.US, "where partnerID = '%s'", partnerID);
        return selectionCriteria;
    }

    @Override
    public String[] selectionArgsForFilter() {
        return null;
    }

    @Override
    public String limitBy() {
        return "limit 1";
    }

    public void save() {
        // TODO: 2/5/17 Generate GERegisterPartner
        idbSession.executeInTransaction(new IDBTransaction() {
            @Override
            public Void perform(IDBSession dbSession) {
                dbSession.create(PartnerModel.this);
                return null;
            }
        });
    }

    public void startSession() {
        Logger.i(appContext, TAG, "SESSION START Partner: " + partnerID);
        PartnerSessionModel partnerSessionModel = PartnerSessionModel.build(appContext, partnerID);
        partnerSessionModel.save();
        // TODO: 2/5/17 Generate GEStartPartnerSession
    }


    public void terminateSession() {
        if (isSessionToTerminate(partnerID)) {
            Logger.i(appContext, TAG, "SESSION TERMINATE Partner: " + partnerID);
            PartnerSessionModel partnerSessionModel = PartnerSessionModel.build(appContext, partnerID);
            partnerSessionModel.clear();
        }
    }

    private Boolean isSessionToTerminate(String partnerId) {
        Set<String> activePartners = PartnerSessionModel.findActivePartners(appContext);
        return (activePartners != null && activePartners.contains(partnerId));
    }
}
