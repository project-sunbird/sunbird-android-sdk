package org.ekstep.genieservices.telemetry.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.telemetry.db.contract.TelemetryProcessedEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swayangjit on 26/4/17.
 */

public class ProcessedEventsModel implements IReadable {

    private List<ProcessedEventModel> processedEvents;
    private IDBSession mDBSession;

    private ProcessedEventsModel(IDBSession dbSession) {
        this.mDBSession = dbSession;
        this.processedEvents = new ArrayList<>();
    }

    private ProcessedEventsModel(IDBSession dbSession, List<ProcessedEventModel> processedEvents) {
        this.mDBSession = dbSession;
        this.processedEvents = processedEvents;
    }

    public static ProcessedEventsModel find(IDBSession dbSession) {
        ProcessedEventsModel processedEvents = new ProcessedEventsModel(dbSession);
        dbSession.read(processedEvents);
        return processedEvents;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            do {
                ProcessedEventModel processedEvent = ProcessedEventModel.build(mDBSession);
                processedEvent.readWithoutMoving(resultSet);
                processedEvents.add(processedEvent);
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

    public List<ProcessedEventModel> getAllProcessedEvents() {
        return processedEvents;
    }

    public int getTotalEvents() {
        int totalEvents = 0;
        List<ProcessedEventModel> processedEventList = getAllProcessedEvents();
        if (processedEventList != null && processedEventList.size() > 0) {
            for (ProcessedEventModel processedEvent : processedEventList) {
                totalEvents = totalEvents + processedEvent.getNumberOfEvents();
            }
        }
        return totalEvents;
    }
}
