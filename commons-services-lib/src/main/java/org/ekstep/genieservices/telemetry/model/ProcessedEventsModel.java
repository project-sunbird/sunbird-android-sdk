package org.ekstep.genieservices.telemetry.model;

import org.ekstep.genieservices.commons.db.contract.TelemetryProcessedEntry;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 26/4/17.
 *
 * @author swayangjit
 */
public class ProcessedEventsModel implements IReadable {

    private IDBSession dBSession;
    private List<ProcessedEventModel> processedEventList;

    private ProcessedEventsModel(IDBSession dbSession) {
        this.dBSession = dbSession;
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

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            processedEventList = new ArrayList<>();
            do {
                ProcessedEventModel processedEvent = ProcessedEventModel.build(dBSession);
                processedEvent.readWithoutMoving(resultSet);
                processedEventList.add(processedEvent);
            } while (resultSet.moveToNext());
        }
        return this;
    }

    @Override
    public String getTableName() {
        return TelemetryProcessedEntry.TABLE_NAME;
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
