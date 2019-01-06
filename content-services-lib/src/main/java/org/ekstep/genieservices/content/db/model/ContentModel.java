package org.ekstep.genieservices.content.db.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.ContentAccessEntry;
import org.ekstep.genieservices.commons.db.contract.ContentEntry;
import org.ekstep.genieservices.commons.db.contract.ContentMarkerEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.ContentHandler;

import java.util.Locale;
import java.util.Map;

import static java.lang.String.valueOf;

/**
 * Created on 5/3/2017.
 *
 * @author anil
 */
public class ContentModel implements IWritable, IUpdatable, IReadable, ICleanable {

    private IDBSession mDBSession;
    private Long id = -1L;

    private String identifier;
    private String serverData;
    private String localData;
    private String mimeType;
    private String path;
    private String visibility;
    //The content reference count default value will be 1. Don't change this value.
    private int refCount = 1;
    private int contentState;
    private String contentType;
    private String manifestVersion;
    private String localLastUpdatedTime;
    private String serverLastUpdatedOn;
    private String audience;
    private String pragma;
    private Long sizeOnDevice;
    private Long lastUsedTime;
    private boolean updateLocalLastUpdatedTime = true;

    private ContentModel(IDBSession dbSession) {
        this.mDBSession = dbSession;
    }

    private ContentModel(IDBSession dbSession, String identifier) {
        this.mDBSession = dbSession;
        this.identifier = identifier;
    }

    private ContentModel(IDBSession dbSession, String identifier, String serverData, String serverLastUpdatedOn,
                         String manifestVersion, String localData, String mimeType, String contentType, String visibility, String audience, String pragma) {
        this.mDBSession = dbSession;
        this.identifier = identifier;
        this.serverData = serverData;
        this.serverLastUpdatedOn = serverLastUpdatedOn;
        this.manifestVersion = manifestVersion;
        this.localData = localData;
        this.mimeType = mimeType;
        this.contentType = contentType;
        this.visibility = visibility;
        this.audience = audience;
        this.pragma = pragma;
    }

    private ContentModel(IDBSession dbSession, String identifier, String manifestVersion, String localData,
                         String mimeType, String contentType, String visibility, String path,
                         int refCount, int contentState, String audience, String pragma, long sizeOnDevice) {
        this(dbSession, identifier, null, null, manifestVersion, localData, mimeType, contentType, visibility, audience, pragma);
        this.path = path;
        this.refCount = refCount;
        this.contentState = contentState;
        this.sizeOnDevice = sizeOnDevice;
    }

    public static ContentModel find(IDBSession dbSession, Object identifier) {
        ContentModel content = new ContentModel(dbSession, valueOf(identifier));
        dbSession.read(content);

        if (content.id == -1L) {
            return null;
        } else {
            return content;
        }
    }

    public static ContentModel build(IDBSession dbSession) {
        return new ContentModel(dbSession);
    }

    public static ContentModel build(IDBSession dbSession, String identifier, String serverData, String serverLastUpdatedOn,
                                     String manifestVersion, String localData, String mimeType, String contentType, String visibility, String audience, String pragma) {
        ContentModel contentModel = new ContentModel(dbSession, identifier, serverData, serverLastUpdatedOn,
                manifestVersion, localData, mimeType, contentType, visibility, audience, pragma);
        return contentModel;
    }

    public static ContentModel build(IDBSession dbSession, String identifier, String manifestVersion, String localData,
                                     String mimeType, String contentType, String visibility, String path,
                                     int refCount, int contentState, String audience, String pragma, long sizeOnDevice) {
        ContentModel contentModel = new ContentModel(dbSession, identifier, manifestVersion, localData,
                mimeType, contentType, visibility, path, refCount, contentState, audience, pragma, sizeOnDevice);

        return contentModel;
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
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        with(contentValues, ContentEntry.COLUMN_NAME_MANIFEST_VERSION, manifestVersion);
        with(contentValues, ContentEntry.COLUMN_NAME_IDENTIFIER, identifier);
        with(contentValues, ContentEntry.COLUMN_NAME_SERVER_DATA, serverData);
        with(contentValues, ContentEntry.COLUMN_NAME_LOCAL_DATA, localData);
        with(contentValues, ContentEntry.COLUMN_NAME_MIME_TYPE, mimeType);
        with(contentValues, ContentEntry.COLUMN_NAME_PATH, path);
        with(contentValues, ContentEntry.COLUMN_NAME_VISIBILITY, visibility);
        with(contentValues, ContentEntry.COLUMN_NAME_CONTENT_TYPE, contentType);
        with(contentValues, ContentEntry.COLUMN_NAME_LOCAL_LAST_UPDATED_ON, localLastUpdatedOn());
        with(contentValues, ContentEntry.COLUMN_NAME_SERVER_LAST_UPDATED_ON, serverLastUpdatedOn);
        with(contentValues, ContentEntry.COLUMN_NAME_REF_COUNT, refCount);
        with(contentValues, ContentEntry.COLUMN_NAME_CONTENT_STATE, contentState);
        with(contentValues, ContentEntry.COLUMN_NAME_AUDIENCE, audience);
        with(contentValues, ContentEntry.COLUMN_NAME_PRAGMA, pragma);
//        with(contentValues, ContentEntry.COLUMN_NAME_INDEX, null);
        with(contentValues, ContentEntry.COLUMN_NAME_SIZE_ON_DEVICE, sizeOnDevice);

        return contentValues;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            readWithoutMoving(resultSet);
        }
        return this;
    }

    @Override
    public ContentValues getFieldsToUpdate() {
        ContentValues contentValues = new ContentValues();

        with(contentValues, ContentEntry.COLUMN_NAME_MANIFEST_VERSION, manifestVersion);
        with(contentValues, ContentEntry.COLUMN_NAME_IDENTIFIER, identifier);
        with(contentValues, ContentEntry.COLUMN_NAME_SERVER_DATA, serverData);
        with(contentValues, ContentEntry.COLUMN_NAME_LOCAL_DATA, localData);
        with(contentValues, ContentEntry.COLUMN_NAME_MIME_TYPE, mimeType);
        with(contentValues, ContentEntry.COLUMN_NAME_PATH, path);
        with(contentValues, ContentEntry.COLUMN_NAME_VISIBILITY, visibility);
        with(contentValues, ContentEntry.COLUMN_NAME_CONTENT_TYPE, contentType);
        if (updateLocalLastUpdatedTime) {
            with(contentValues, ContentEntry.COLUMN_NAME_LOCAL_LAST_UPDATED_ON, localLastUpdatedOn());
        }
        with(contentValues, ContentEntry.COLUMN_NAME_SERVER_LAST_UPDATED_ON, serverLastUpdatedOn);
        with(contentValues, ContentEntry.COLUMN_NAME_REF_COUNT, refCount);
        with(contentValues, ContentEntry.COLUMN_NAME_CONTENT_STATE, contentState);
        with(contentValues, ContentEntry.COLUMN_NAME_AUDIENCE, audience);
        with(contentValues, ContentEntry.COLUMN_NAME_PRAGMA, pragma);
//        with(contentValues, ContentEntry.COLUMN_NAME_INDEX, null);
        with(contentValues, ContentEntry.COLUMN_NAME_SIZE_ON_DEVICE, sizeOnDevice);
        return contentValues;
    }

    @Override
    public String getTableName() {
        return ContentEntry.TABLE_NAME;
    }

    @Override
    public void clean() {
        id = -1L;
    }

    @Override
    public String selectionToClean() {
        return String.format(Locale.US, "where %s = '%s'", ContentEntry.COLUMN_NAME_IDENTIFIER, identifier);
    }

    @Override
    public String updateBy() {
        return String.format(Locale.US, "%s = '%s'", ContentEntry.COLUMN_NAME_IDENTIFIER, identifier);
    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {
        return String.format(Locale.US, "where %s = ?", ContentEntry.COLUMN_NAME_IDENTIFIER);
    }

    @Override
    public String[] selectionArgsForFilter() {
        return new String[]{identifier};
    }

    @Override
    public String limitBy() {
        return "limit 1";
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    public void readWithoutMoving(IResultSet resultSet) {
        id = resultSet.getLong(0);
        identifier = resultSet.getString(resultSet.getColumnIndex(ContentEntry.COLUMN_NAME_IDENTIFIER));
        serverData = resultSet.getString(resultSet.getColumnIndex(ContentEntry.COLUMN_NAME_SERVER_DATA));
        localData = resultSet.getString(resultSet.getColumnIndex(ContentEntry.COLUMN_NAME_LOCAL_DATA));
        mimeType = resultSet.getString(resultSet.getColumnIndex(ContentEntry.COLUMN_NAME_MIME_TYPE));
        path = resultSet.getString(resultSet.getColumnIndex(ContentEntry.COLUMN_NAME_PATH));
        visibility = resultSet.getString(resultSet.getColumnIndex(ContentEntry.COLUMN_NAME_VISIBILITY));
        refCount = resultSet.getInt(resultSet.getColumnIndex(ContentEntry.COLUMN_NAME_REF_COUNT));
        contentState = resultSet.getInt(resultSet.getColumnIndex(ContentEntry.COLUMN_NAME_CONTENT_STATE));
        contentType = resultSet.getString(resultSet.getColumnIndex(ContentEntry.COLUMN_NAME_CONTENT_TYPE));
        localLastUpdatedTime = resultSet.getString(resultSet.getColumnIndex(ContentEntry.COLUMN_NAME_LOCAL_LAST_UPDATED_ON));
        audience = resultSet.getString(resultSet.getColumnIndex(ContentEntry.COLUMN_NAME_AUDIENCE));
        pragma = resultSet.getString(resultSet.getColumnIndex(ContentEntry.COLUMN_NAME_PRAGMA));
        sizeOnDevice = resultSet.getLong(resultSet.getColumnIndex(ContentEntry.COLUMN_NAME_SIZE_ON_DEVICE));

        if (resultSet.getColumnIndex(ContentAccessEntry.COLUMN_NAME_EPOCH_TIMESTAMP) != -1) {
            lastUsedTime = resultSet.getLong(resultSet.getColumnIndex(ContentAccessEntry.COLUMN_NAME_EPOCH_TIMESTAMP));
        }
        if (resultSet.getColumnIndex(ContentMarkerEntry.COLUMN_NAME_DATA) != -1) {
            if (StringUtil.isNullOrEmpty(localData)) {
                localData = resultSet.getString(resultSet.getColumnIndex(ContentMarkerEntry.COLUMN_NAME_DATA));
            }
            if (!StringUtil.isNullOrEmpty(localData)) {
                Map contentData = GsonUtil.fromJson(localData, Map.class);

                if (StringUtil.isNullOrEmpty(identifier)) {
                    identifier = ContentHandler.readIdentifier(contentData);
                }
                if (StringUtil.isNullOrEmpty(mimeType)) {
                    mimeType = ContentHandler.readMimeType(contentData);
                }
                if (StringUtil.isNullOrEmpty(visibility)) {
                    visibility = ContentHandler.readVisibility(contentData);
                }
                if (StringUtil.isNullOrEmpty(contentType)) {
                    contentType = ContentHandler.readContentType(contentData);
                }
            }
        }
    }

    private void with(ContentValues contentValues, String key, String value) {
        if (value != null) {
            contentValues.put(key, value);
        }
    }

    private void with(ContentValues contentValues, String key, int value) {
        contentValues.put(key, value);
    }

    private void with(ContentValues contentValues, String key, long value) {
        contentValues.put(key, value);
    }

    private String localLastUpdatedOn() {
        if (localData != null) {
            return DateUtil.getCurrentTimestamp();
        }
        return null;
    }

    public int getRefCount() {
        return refCount;
    }

    public void addOrUpdateRefCount(int refCount) {
        if (refCount < 0) {
            refCount = 0;
        }
        this.refCount = refCount;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getServerData() {
        return serverData;
    }

    public void setServerData(String serverData) {
        this.serverData = serverData;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public void setServerLastUpdatedOn(String serverLastUpdatedOn) {
        this.serverLastUpdatedOn = serverLastUpdatedOn;
    }

    public String getLocalData() {
        return localData;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public int getContentState() {
        return contentState;
    }

    public void addOrUpdateContentState(int contentState) {
        this.contentState = contentState;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getLocalLastUpdatedTime() {
        return localLastUpdatedTime;
    }

    public Long getSizeOnDevice() {
        return sizeOnDevice;
    }

    public void setSizeOnDevice(Long sizeOnDevice) {
        this.sizeOnDevice = sizeOnDevice;
    }

    public Long getLastUsedTime() {
        return lastUsedTime;
    }

    public String getPragma() {
        return pragma;
    }

    public void setPragma(String pragma) {
        this.pragma = pragma;
    }

    public void doNotUpdateLocalLastUpdatedTime() {
        this.updateLocalLastUpdatedTime = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContentModel content = (ContentModel) o;

        if (serverData != null ? !serverData.equals(content.serverData) : content.serverData != null) {
            return false;
        }

        if (localData != null ? !localData.equals(content.localData) : content.localData != null) {
            return false;
        }

        if (!identifier.equals(content.identifier)) {
            return false;
        }

        if (mimeType != null ? !mimeType.equals(content.mimeType) : content.mimeType != null) {
            return false;
        }

        if (path != null ? !path.equals(content.path) : content.path != null) {
            return false;
        }

        if (visibility != null ? !visibility.equals(content.visibility) : content.visibility != null) {
            return false;
        }

        if (!id.equals(content.id)) {
            return false;
        }

        return !(manifestVersion != null ? !manifestVersion.equals(content.manifestVersion) : content.manifestVersion != null);
    }

    @Override
    public int hashCode() {
        int result = serverData != null ? serverData.hashCode() : 0;
        result = 31 * result + (localData != null ? localData.hashCode() : 0);
        result = 31 * result + identifier.hashCode();
        result = 31 * result + (mimeType != null ? mimeType.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (visibility != null ? visibility.hashCode() : 0);
        result = 31 * result + id.hashCode();
        result = 31 * result + (manifestVersion != null ? manifestVersion.hashCode() : 0);
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        return result;
    }

}