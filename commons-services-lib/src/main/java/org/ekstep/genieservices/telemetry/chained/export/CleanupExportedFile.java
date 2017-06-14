package org.ekstep.genieservices.telemetry.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.db.contract.MetaEntry;
import org.ekstep.genieservices.commons.db.contract.TelemetryProcessedEntry;
import org.ekstep.genieservices.commons.db.model.CustomReadersModel;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created on 6/10/2017.
 *
 * @author anil
 */
public class CleanupExportedFile implements IChainable {

    private static final String TAG = CleanupExportedFile.class.getSimpleName();
    private IChainable nextLink;

    private String destinationDBFilePath;

    public CleanupExportedFile(String destinationDBFilePath) {
        this.destinationDBFilePath = destinationDBFilePath;
    }

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {

        List<String> allTables = getAllTables(importContext.getDBSession());
        List<String> allTableToExclude = getAllTableToExclude();

        removeTables(importContext.getDBSession(), allTables, allTableToExclude);

        try {
            removeJournalFile();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(TAG, e.getMessage());

            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, e.getMessage(), TAG);
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Export telemetry failed", TAG);
        }
    }

    @Override
    public IChainable then(IChainable link) {
        nextLink = link;
        return link;
    }

    private List<String> getAllTables(IDBSession dbSession) {
        List<String> tables;

        String allTablesQuery = String.format(Locale.US, "select name from sqlite_master where type='%s'", "table");
        CustomReadersModel customReadersModel = CustomReadersModel.find(dbSession, allTablesQuery);
        if (customReadersModel != null) {
            tables = customReadersModel.getDataList();
        } else {
            tables = new ArrayList<>();
        }

        return tables;
    }

    private List<String> getAllTableToExclude() {
        List<String> tablesToExclude = new ArrayList<>();

        tablesToExclude.add(MetaEntry.TABLE_NAME);
        tablesToExclude.add(TelemetryProcessedEntry.TABLE_NAME);

        return tablesToExclude;
    }

    private void removeTables(IDBSession dbSession, List<String> allTables, List<String> allTableToExclude) {
        for (String table : allTables) {
            if (allTableToExclude.contains(table)) {
                continue;
            }
            String dropTableQuery = String.format(Locale.US, "DROP TABLE IF EXISTS %s", table);
            dbSession.execute(dropTableQuery);
        }
    }

    private void removeJournalFile() throws Exception {
        File file = new File(destinationDBFilePath + "-journal");
        file.delete();
    }
}
