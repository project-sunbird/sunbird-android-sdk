package org.ekstep.genieservices.telemetry.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.telemetry.db.contract.TelemetryProcessedEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swayangjit on 26/4/17.
 */

public class ProcessedEvents implements IReadable {

    private List<ProcessedEvent> processedEvents;
    private AppContext mAppContext;

    private ProcessedEvents(AppContext appContext) {
        this.mAppContext = appContext;
        this.processedEvents = new ArrayList<>();
    }

    private ProcessedEvents(AppContext appContext, List<ProcessedEvent> processedEvents) {
        this.mAppContext = appContext;
        this.processedEvents = processedEvents;
    }

    public static ProcessedEvents find(AppContext appContext) {
        ProcessedEvents processedEvents = new ProcessedEvents(appContext);
        appContext.getDBSession().read(processedEvents);
        return processedEvents;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst()) {
            do {
                ProcessedEvent processedEvent = ProcessedEvent.build(mAppContext);
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

    public List<ProcessedEvent> getAllProcessedEvents() {
        return processedEvents;
    }

    public int getTotalEvents() {
        int totalEvents = 0;
        List<ProcessedEvent> processedEventList = getAllProcessedEvents();
        if (processedEventList != null && processedEventList.size() > 0) {
            for (ProcessedEvent processedEvent : processedEventList) {
                totalEvents = totalEvents + processedEvent.getNumberOfEvents();
            }
        }
        return totalEvents;
    }
}
