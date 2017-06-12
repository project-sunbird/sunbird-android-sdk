package org.ekstep.genieservices.telemetry.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.ImportedMetadataEntry;
import org.ekstep.genieservices.commons.db.core.ContentValues;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.core.IUpdatable;
import org.ekstep.genieservices.commons.db.core.IWritable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.Locale;

/**
 * Created on 6/7/2017.
 *
 * @author anil
 */
public class ImportedMetadataModel implements IWritable, IReadable, IUpdatable, ICleanable {

    private IDBSession dbSession;
    private Long id = -1L;
    private String importedId;
    private String deviceId;
    private int count;

    private ImportedMetadataModel(IDBSession dbSession) {
        this.dbSession = dbSession;
    }

    private ImportedMetadataModel(IDBSession dbSession, String importedId, String deviceId) {
        this.dbSession = dbSession;
        this.importedId = importedId;
        this.deviceId = deviceId;
    }

    private ImportedMetadataModel(IDBSession dbSession, String importedId, String deviceId, int count) {
        this(dbSession, importedId, deviceId);
        this.count = count;
    }

    public static ImportedMetadataModel find(IDBSession dbSession, String importedId, String deviceId) {
        ImportedMetadataModel model = new ImportedMetadataModel(dbSession, importedId, deviceId);
        dbSession.read(model);

        if (model.id == -1L) {
            return null;
        } else {
            return model;
        }
    }

    public static ImportedMetadataModel build(IDBSession dbSession) {
        return new ImportedMetadataModel(dbSession);
    }

    public static ImportedMetadataModel build(IDBSession dbSession, String importedId, String deviceId, int count) {
        return new ImportedMetadataModel(dbSession, importedId, deviceId, count);
    }

    public Void save() {
        dbSession.create(this);
        return null;
    }

    public Void update() {
        dbSession.update(this);
        return null;
    }

    public Void clear() {
        dbSession.clean(this);
        return null;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            readWithoutMoving(resultSet);
        }
        return this;
    }

    public void readWithoutMoving(IResultSet resultSet) {
        id = resultSet.getLong(0);
        importedId = resultSet.getString(resultSet.getColumnIndex(ImportedMetadataEntry.COLUMN_NAME_IMPORTED_ID));
        deviceId = resultSet.getString(resultSet.getColumnIndex(ImportedMetadataEntry.COLUMN_NAME_DEVICE_ID));
        count = resultSet.getInt(resultSet.getColumnIndex(ImportedMetadataEntry.COLUMN_NAME_COUNT));
    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {
        return String.format(Locale.US, "where %s = '%s' AND %s = '%s'",
                ImportedMetadataEntry.COLUMN_NAME_IMPORTED_ID, importedId,
                ImportedMetadataEntry.COLUMN_NAME_DEVICE_ID, deviceId);
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
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ImportedMetadataEntry.COLUMN_NAME_IMPORTED_ID, importedId);
        contentValues.put(ImportedMetadataEntry.COLUMN_NAME_DEVICE_ID, deviceId);
        contentValues.put(ImportedMetadataEntry.COLUMN_NAME_COUNT, count);
        return contentValues;
    }

    @Override
    public void updateId(long id) {
        this.id = id;
    }

    @Override
    public ContentValues getFieldsToUpdate() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ImportedMetadataEntry.COLUMN_NAME_COUNT, count);
        return contentValues;
    }

    @Override
    public String getTableName() {
        return ImportedMetadataEntry.TABLE_NAME;
    }

    @Override
    public void clean() {

    }

    @Override
    public String selectionToClean() {
        return String.format(Locale.US, "where %s = '%s' AND %s = '%s'",
                ImportedMetadataEntry.COLUMN_NAME_IMPORTED_ID, importedId,
                ImportedMetadataEntry.COLUMN_NAME_DEVICE_ID, deviceId);
    }

    @Override
    public String updateBy() {
        return String.format(Locale.US, "where %s = '%s' AND %s = '%s'",
                ImportedMetadataEntry.COLUMN_NAME_IMPORTED_ID, importedId,
                ImportedMetadataEntry.COLUMN_NAME_DEVICE_ID, deviceId);
    }

    @Override
    public void beforeWrite(AppContext context) {

    }

    public String getImportedId() {
        return importedId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public int getCount() {
        return count;
    }
}
