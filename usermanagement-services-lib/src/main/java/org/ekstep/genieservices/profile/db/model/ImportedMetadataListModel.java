package org.ekstep.genieservices.profile.db.model;

import org.ekstep.genieservices.commons.db.contract.ImportedMetadataEntry;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 6/7/2017.
 *
 * @author anil
 */
public class ImportedMetadataListModel implements IReadable {

    private IDBSession dbSession;
    private List<ImportedMetadataModel> importedMetadataModelList;

    private ImportedMetadataListModel(IDBSession dbSession) {
        this.dbSession = dbSession;
    }

    public static ImportedMetadataListModel findAll(IDBSession dbSession) {
        ImportedMetadataListModel model = new ImportedMetadataListModel(dbSession);
        dbSession.read(model);

        if (model.importedMetadataModelList == null) {
            return null;
        } else {
            return model;
        }
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            importedMetadataModelList = new ArrayList<>();
            do {
                ImportedMetadataModel importedMetadataModel = ImportedMetadataModel.build(dbSession);
                importedMetadataModel.readWithoutMoving(resultSet);

                importedMetadataModelList.add(importedMetadataModel);
            } while (resultSet.moveToNext());
        }
        return this;
    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {
        return "";
    }

    @Override
    public String[] selectionArgsForFilter() {
        return null;
    }

    @Override
    public String limitBy() {
        return "";
    }

    @Override
    public String getTableName() {
        return ImportedMetadataEntry.TABLE_NAME;
    }

    public List<ImportedMetadataModel> getImportedMetadataModelList() {
        return importedMetadataModelList;
    }
}
