package org.ekstep.genieservices.telemetry.model;

import org.ekstep.genieservices.commons.db.contract.TelemetryProcessedEntry;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created on 26/4/17.
 *
 * @author swayangjit
 */
public class ProcessedEventsModel implements IReadable, ICleanable {

    private IDBSession dBSession;
    private List<ProcessedEventModel> processedEventList;
    private boolean onlyCount;
    private int count;

    private ProcessedEventsModel(IDBSession dbSession) {
        this.dBSession = dbSession;
    }

    private ProcessedEventsModel(IDBSession dbSession, boolean onlyCount) {
        this(dbSession);
        this.onlyCount = onlyCount;
    }

    public static ProcessedEventsModel find(IDBSession dbSession) {
        ProcessedEventsModel model = new ProcessedEventsModel(dbSession);
        dbSession.read(model);

        if (model.processedEventList == null) {
            return null;
        } else {
            return model;
        }
    }

    public static int count(IDBSession dbSession) {
        String query = String.format(Locale.US, "select sum(%s) from %s;", TelemetryProcessedEntry.COLUMN_NAME_NUMBER_OF_EVENTS, TelemetryProcessedEntry.TABLE_NAME);
        ProcessedEventsModel model = new ProcessedEventsModel(dbSession, true);
        dbSession.read(model, query);
        return model.count;
    }

    public static ProcessedEventsModel build(IDBSession dbSession) {
        return new ProcessedEventsModel(dbSession);
    }

    public Void deleteAll() {
        dBSession.clean(this);
        return null;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            if (onlyCount) {
                count = resultSet.getInt(0);
            } else {
                processedEventList = new ArrayList<>();
                do {
                    ProcessedEventModel processedEvent = ProcessedEventModel.build(dBSession);
                    processedEvent.readWithoutMoving(resultSet);
                    processedEventList.add(processedEvent);
                } while (resultSet.moveToNext());
            }
        }
        return this;
    }

    @Override
    public String getTableName() {
        return TelemetryProcessedEntry.TABLE_NAME;
    }

    @Override
    public void clean() {

    }

    @Override
    public String selectionToClean() {
        return "";
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

    public List<ProcessedEventModel> getProcessedEventList() {
        return processedEventList;
    }

}
