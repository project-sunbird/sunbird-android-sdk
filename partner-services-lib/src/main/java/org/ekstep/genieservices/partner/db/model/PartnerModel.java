package org.ekstep.genieservices.partner.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.PartnerEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.Locale;

/**
 * PartnerModel handles all the DB related operations like Reading and Writing data to {@link PartnerEntry} table by implementing
 * the {@link IWritable} and {@link IReadable} interfaces
 *
 */
public class PartnerModel implements IWritable, IReadable {

    private static final String TAG = "model-Partner";
    private IDBSession dbSession;

    private Long id = -1L;
    private String partnerID;
    private String publicKeyId;
    private String publicKey;

    private PartnerModel(IDBSession dbSession, String partnerID, String publicKey, String publicKeyId) {
        this.dbSession = dbSession;
        this.partnerID = partnerID;
        this.publicKey = publicKey;
        this.publicKeyId = publicKeyId;
    }

    /**
     * This api fetches the {@link PartnerModel} based on the partnerId
     *
     * @param dbSession
     * @param partnerID
     * @return
     */
    public static PartnerModel findByPartnerId(IDBSession dbSession, String partnerID) {
        PartnerModel partnerModel = new PartnerModel(dbSession, partnerID, null, null);
        dbSession.read(partnerModel);
        if (StringUtil.isNullOrEmpty(partnerModel.publicKey)) {
            return null;
        } else {
            return partnerModel;
        }
    }

    /**
     * This api builds the {@link PartnerModel} with all the inputs provided
     *
     * @param idbSession
     * @param partnerID
     * @param publicKey
     * @param publicKeyId
     * @return
     */
    public static PartnerModel build(IDBSession idbSession, String partnerID, String publicKey, String publicKeyId) {
        return new PartnerModel(idbSession, partnerID, publicKey, publicKeyId);
    }

    public void save() {
        dbSession.create(this);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PartnerEntry.COLUMN_NAME_UID, partnerID);
        contentValues.put(PartnerEntry.COLUMN_NAME_KEY, publicKey);
        contentValues.put(PartnerEntry.COLUMN_NAME_KEY_ID, publicKeyId);
        return contentValues;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    @Override
    public IReadable read(IResultSet cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getLong(0);
            partnerID = cursor.getString(1);
            publicKey = cursor.getString(2);
            publicKeyId = cursor.getString(3);
        }
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

    public String getPartnerID() {
        return partnerID;
    }

    public String getPublicKeyId() {
        return publicKeyId;
    }
}
