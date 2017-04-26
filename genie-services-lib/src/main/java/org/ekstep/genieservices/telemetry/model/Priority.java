package org.ekstep.genieservices.telemetry.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.telemetry.db.contract.EventPriorityEntry;

import java.util.Locale;

/**
 * Created by swayangjit on 26/4/17.
 */

public class Priority implements IReadable {

    private final int DEFAULT_PRIORITY = 2;
    private AppContext mAppContext;
    private Integer priority;
    private String eventType;

    private Priority(AppContext appContext, String eventType) {
        this.mAppContext = appContext;
        this.eventType = eventType;
    }

    private Priority(AppContext appContext, String eventType, int priority) {
        this.mAppContext = appContext;
        this.eventType = eventType;
        this.priority = priority;
    }

    public static Priority build(AppContext appContext, String eventType) {
        return new Priority(appContext, eventType);
    }

    public static Priority build(AppContext appContext, String eventType, int priority) {
        return new Priority(appContext, eventType, priority);
    }

    public static Priority findByType(AppContext appContext, String eventType) {
        Priority priority = new Priority(appContext, eventType);
        appContext.getDBSession().read(priority);
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
