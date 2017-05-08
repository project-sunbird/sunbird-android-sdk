package org.ekstep.genieservices.telemetry.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.telemetry.db.contract.EventPriorityEntry;

import java.util.Locale;

/**
 * Created by swayangjit on 26/4/17.
 */

public class PriorityModel implements IReadable {

    private final int DEFAULT_PRIORITY = 2;
    private IDBSession mDBSession;
    private Integer priority;
    private String eventType;

    private PriorityModel(IDBSession dbSession, String eventType) {
        this.mDBSession = dbSession;
        this.eventType = eventType;
    }

    private PriorityModel(IDBSession dbSession, String eventType, int priority) {
        this.mDBSession = dbSession;
        this.eventType = eventType;
        this.priority = priority;
    }

    public static PriorityModel build(IDBSession dbSession, String eventType) {
        return new PriorityModel(dbSession, eventType);
    }

    public static PriorityModel build(IDBSession dbSession, String eventType, int priority) {
        return new PriorityModel(dbSession, eventType, priority);
    }

    public static PriorityModel findByType(IDBSession dbSession, String eventType) {
        PriorityModel priority = new PriorityModel(dbSession, eventType);
        dbSession.read(priority);
        return priority;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst())
            do {
                eventType = resultSet.getString(resultSet.getColumnIndex(EventPriorityEntry.COLUMN_NAME_EVENT));
                priority = resultSet.getInt(resultSet.getColumnIndex(EventPriorityEntry.COLUMN_NAME_PRIORITY));
            } while (resultSet.moveToNext());
        return this;
    }

    @Override
    public String getTableName() {
        return EventPriorityEntry.TABLE_NAME;
    }

    @Override
    public String orderBy() {
        return "";
    }

    @Override
    public String filterForRead() {
        return String.format(Locale.US, "where event = '%s'", eventType);
    }

    @Override
    public String[] selectionArgsForFilter() {
        return null;
    }

    @Override
    public String limitBy() {
        return "limit 1";
    }

    public int getPriority() {
        if (priority != null)
            return priority;
        return DEFAULT_PRIORITY;
    }

    public String getEventType() {
        return eventType;
    }

}
