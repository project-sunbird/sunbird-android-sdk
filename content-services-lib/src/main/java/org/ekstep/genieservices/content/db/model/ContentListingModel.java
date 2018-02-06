package org.ekstep.genieservices.content.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.ContentListingCriteria;
import org.ekstep.genieservices.commons.db.contract.PageEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created on 5/29/2017.
 *
 * @author anil
 */
public class ContentListingModel implements IWritable, IUpdatable, IReadable, ICleanable {

    private static final String TAG = ContentListingModel.class.getSimpleName();

    private Long id = -1L;
    private IDBSession mDBSession;

    private String mPageIdentifier;
    private String mJson;
    private int mAge;
    private int mStandard;
    private String mMedium;
    private String mBoard;
    private String mSubject;
    private long mExpiryTime;
    private String channelStr;
    private String audienceStr;
    private String pragmaStr;

    private ContentListingModel(IDBSession dbSession, ContentListingCriteria listingCriteria) {
        this.mDBSession = dbSession;
        this.mPageIdentifier = listingCriteria.getContentListingId();
        this.mAge = listingCriteria.getAge() != 0 ? listingCriteria.getAge() : -1;
        this.mStandard = listingCriteria.getGrade() != 0 ? listingCriteria.getGrade() : -1;
        this.mMedium = listingCriteria.getMedium() != null ? listingCriteria.getMedium() : "";
        this.mBoard = listingCriteria.getBoard() != null ? listingCriteria.getBoard() : "";
        this.mSubject = listingCriteria.getSubject() != null ? listingCriteria.getSubject() : "";
        this.channelStr = listingCriteria.getChannel() != null ? StringUtil.join(",", getSortedList(listingCriteria.getChannel())) : "";
        this.audienceStr = listingCriteria.getAudience() != null ? StringUtil.join(",", getSortedList(listingCriteria.getAudience())) : "";
        this.pragmaStr = listingCriteria.getPragma() != null ? StringUtil.join(",", getSortedList(listingCriteria.getPragma())) : "";
    }

    private ContentListingModel(IDBSession dbSession, ContentListingCriteria listingCriteria, String json, long expiryTime) {
        this(dbSession, listingCriteria);
        this.mJson = json;
        this.mExpiryTime = expiryTime;
    }

    public static ContentListingModel build(IDBSession dbSession, ContentListingCriteria listingCriteria, String json, long expiryTime) {
        ContentListingModel contentListingModel = new ContentListingModel(dbSession, listingCriteria, json, expiryTime);
        return contentListingModel;
    }

    public static ContentListingModel find(IDBSession dbSession, ContentListingCriteria listingCriteria) {
        ContentListingModel contentListingModel = new ContentListingModel(dbSession, listingCriteria);
        dbSession.read(contentListingModel);
         if (contentListingModel.id == -1) {
            return null;
        } else {
            return contentListingModel;
        }
    }

    private List<String> getSortedList(String input[]) {
        List<String> list = Arrays.asList(input);
        Collections.sort(list);
        return list;
    }

    public void save() {
        mDBSession.create(this);
    }

    public void update() {
        mDBSession.update(this);
    }

    public void delete() {
        mDBSession.clean(this);
    }

    private void readWithoutMoving(IResultSet resultSet) {
        id = resultSet.getLong(0);
        mPageIdentifier = resultSet.getString(resultSet.getColumnIndex(PageEntry.COLUMN_NAME_PAGE_IDENTIFIER));
        mJson = resultSet.getString(resultSet.getColumnIndex(PageEntry.COLUMN_NAME_PAGE_JSON));
        mExpiryTime = resultSet.getLong(resultSet.getColumnIndex(PageEntry.COLUMN_EXPIRY_TIME));
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            readWithoutMoving(resultSet);
        }
        return this;
    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {
        Logger.i(TAG, String.format("SEARCH page: %s", mPageIdentifier));

        String selectionCriteria = String.format(Locale.US, "%s = '%s' AND %s = %d  AND %s = %d  AND %s = '%s'  AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s'",
                PageEntry.COLUMN_NAME_PAGE_IDENTIFIER, mPageIdentifier,
                PageEntry.COLUMN_NAME_AGE, mAge,
                PageEntry.COLUMN_NAME_STANDARD, mStandard,
                PageEntry.COLUMN_NAME_MEDIUM, mMedium,
                PageEntry.COLUMN_NAME_BOARD, mBoard,
                PageEntry.COLUMN_NAME_SUBJECT, mSubject,
                PageEntry.COLUMN_NAME_CHANNEL, channelStr,
                PageEntry.COLUMN_NAME_AUDIENCE, audienceStr,
                PageEntry.COLUMN_NAME_PRAGMA, pragmaStr);

        Logger.i(TAG, String.format("SEARCH page: %s", selectionCriteria));
        return String.format(Locale.US, " where %s", selectionCriteria);
    }

    @Override
    public String[] selectionArgsForFilter() {
        return null;
    }

    @Override
    public String limitBy() {
        return "limit 1";
    }

    @Override
    public ContentValues getFieldsToUpdate() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PageEntry.COLUMN_NAME_PAGE_JSON, mJson);
        contentValues.put(PageEntry.COLUMN_EXPIRY_TIME, mExpiryTime);
        return contentValues;
    }

    @Override
    public String updateBy() {
        return String.format(Locale.US, "%s = '%s'", PageEntry.COLUMN_NAME_PAGE_IDENTIFIER, mPageIdentifier);
    }

    @Override
    public void clean() {
        id = -1L;
        mPageIdentifier = null;
        mJson = null;
    }

    @Override
    public String selectionToClean() {
        return String.format(Locale.US, "WHERE %s = '%s';", PageEntry._ID, id);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PageEntry.COLUMN_NAME_PAGE_IDENTIFIER, mPageIdentifier);
        contentValues.put(PageEntry.COLUMN_NAME_PAGE_JSON, mJson);
        contentValues.put(PageEntry.COLUMN_EXPIRY_TIME, mExpiryTime);
        contentValues.put(PageEntry.COLUMN_NAME_AGE, mAge);
        contentValues.put(PageEntry.COLUMN_NAME_STANDARD, mStandard);
        contentValues.put(PageEntry.COLUMN_NAME_MEDIUM, mMedium);
        contentValues.put(PageEntry.COLUMN_NAME_BOARD, mBoard);
        contentValues.put(PageEntry.COLUMN_NAME_SUBJECT, mSubject);
        contentValues.put(PageEntry.COLUMN_NAME_CHANNEL, channelStr);
        contentValues.put(PageEntry.COLUMN_NAME_AUDIENCE, audienceStr);
        contentValues.put(PageEntry.COLUMN_NAME_PRAGMA, pragmaStr);

        return contentValues;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    @Override
    public String getTableName() {
        return PageEntry.TABLE_NAME;
    }

    @Override
    public void beforeWrite(AppContext context) {
    }

    public String getJson() {
        return mJson;
    }

    public long getExpiryTime() {
        return mExpiryTime;
    }
}
