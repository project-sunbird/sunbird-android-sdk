package org.ekstep.genieservices.content.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.ContentFeedbackEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.DateUtil;

import java.util.Locale;

/**
 * Created on 5/3/2017.
 *
 * @author anil
 */
public class ContentFeedbackModel implements IWritable, IUpdatable, IReadable {

    private IDBSession mDBSession;

    private Long id = -1L;
    private String contentId;
    private String uid;
    private String rating = "";
    private String comments = "";
    private Long createdAt;

    private ContentFeedbackModel(IDBSession dbSession) {
        this.mDBSession = dbSession;
    }

    private ContentFeedbackModel(IDBSession dbSession, String uid, String contentId) {
        this.mDBSession = dbSession;
        this.uid = uid;
        this.contentId = contentId;
    }

    private ContentFeedbackModel(IDBSession dbSession, String uid, String contentId, String rating, String comments) {
        this(dbSession, uid, contentId);

        this.rating = rating;
        this.comments = comments;
    }

    public static ContentFeedbackModel build(IDBSession dbSession, String uid, String contentId, String rating, String comments) {
        ContentFeedbackModel contentFeedbackModel = new ContentFeedbackModel(dbSession, uid, contentId, rating, comments);
        return contentFeedbackModel;
    }

    public static ContentFeedbackModel build(IDBSession dbSession) {
        return new ContentFeedbackModel(dbSession);
    }

    public static ContentFeedbackModel find(IDBSession dbSession, String uid, String contentId) {
        ContentFeedbackModel feedback = new ContentFeedbackModel(dbSession, uid, contentId);
        dbSession.read(feedback);

        if (feedback.id == -1) {
            return null;
        } else {
            return feedback;
        }
    }

    public Void save() {
        mDBSession.create(this);
        return null;
    }

    public Void update() {
        mDBSession.update(this);
        return null;
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
        return String.format(Locale.US, "where %s = ? and %s = ? ", ContentFeedbackEntry.COLUMN_NAME_UID, ContentFeedbackEntry.COLUMN_NAME_CONTENT_ID);
    }

    @Override
    public String[] selectionArgsForFilter() {
        return new String[]{this.uid, this.contentId};
    }

    @Override
    public String limitBy() {
        return "limit 1";
    }

    @Override
    public ContentValues getFieldsToUpdate() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ContentFeedbackEntry.COLUMN_NAME_RATING, rating);
        contentValues.put(ContentFeedbackEntry.COLUMN_NAME_COMMENTS, comments);
        contentValues.put(ContentFeedbackEntry.COLUMN_NAME_CREATED_AT, DateUtil.getEpochTime());

        return contentValues;
    }

    @Override
    public String updateBy() {
        return String.format(Locale.US, "where %s = ? and %s = ? ", ContentFeedbackEntry.COLUMN_NAME_UID, ContentFeedbackEntry.COLUMN_NAME_CONTENT_ID);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ContentFeedbackEntry.COLUMN_NAME_CONTENT_ID, this.contentId);
        contentValues.put(ContentFeedbackEntry.COLUMN_NAME_UID, this.uid);
        contentValues.put(ContentFeedbackEntry.COLUMN_NAME_RATING, this.rating);
        contentValues.put(ContentFeedbackEntry.COLUMN_NAME_COMMENTS, this.comments);
        contentValues.put(ContentFeedbackEntry.COLUMN_NAME_CREATED_AT, DateUtil.getEpochTime());

        return contentValues;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    @Override
    public String getTableName() {
        return ContentFeedbackEntry.TABLE_NAME;
    }

    @Override
    public void beforeWrite(AppContext context) {
        // Do nothing.
    }

    public void readWithoutMoving(IResultSet resultSet) {
        id = resultSet.getLong(0);
        contentId = resultSet.getString(resultSet.getColumnIndex(ContentFeedbackEntry.COLUMN_NAME_CONTENT_ID));
        uid = resultSet.getString(resultSet.getColumnIndex(ContentFeedbackEntry.COLUMN_NAME_UID));
        rating = resultSet.getString(resultSet.getColumnIndex(ContentFeedbackEntry.COLUMN_NAME_RATING));
        comments = resultSet.getString(resultSet.getColumnIndex(ContentFeedbackEntry.COLUMN_NAME_COMMENTS));
        createdAt = resultSet.getLong(resultSet.getColumnIndex(ContentFeedbackEntry.COLUMN_NAME_CREATED_AT));
    }

    public String getContentId() {
        return contentId;
    }

    public String getUid() {
        return uid;
    }

    public String getRating() {
        return rating;
    }

    public String getComments() {
        return comments;
    }

    public Long getCreatedAt() {
        return createdAt;
    }
}
