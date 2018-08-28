package org.ekstep.genieservices.telemetry.model;

import org.ekstep.genieservices.commons.db.DbConstants;
import org.ekstep.genieservices.commons.db.contract.TelemetryEntry;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 26/4/17.
 *
 * @author swayangjit
 */
public class EventsModel implements IReadable, ICleanable {

    private IDBSession dbSession;
    private List<EventModel> events;

    private EventsModel(IDBSession dbSession) {
        this.dbSession = dbSession;
        this.events = new ArrayList<>();
    }

    public static EventsModel find(IDBSession dbSession) {
        EventsModel eventsModel = new EventsModel(dbSession);
        dbSession.read(eventsModel);
        return eventsModel;
    }

    public void delete() {
        dbSession.clean(this);
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst())
            do {
                EventModel eventModel = EventModel.build(dbSession);

                eventModel.readWithoutMoving(resultSet);

                events.add(eventModel);
            } while (resultSet.moveToNext());
        return this;
    }

    @Override
    public String getTableName() {
        return TelemetryEntry.TABLE_NAME;
    }

    @Override
    public String orderBy() {
        return "order by " + TelemetryEntry.COLUMN_NAME_TIMESTAMP;
    }

    @Override
    public String filterForRead() {
        return String.format(Locale.US, "where %s = (select min(%s) from %s)", TelemetryEntry.COLUMN_NAME_PRIORITY, TelemetryEntry.COLUMN_NAME_PRIORITY, getTableName());
    }

    @Override
    public String[] selectionArgsForFilter() {
        return null;
    }

    @Override
    public String limitBy() {
        return String.format(Locale.US, "limit %d", DbConstants.MAX_NUM_OF_EVENTS);
    }

    @Override
    public void clean() {
        events = new ArrayList<>();
    }

    @Override
    public String selectionToClean() {
        return String.format(Locale.US, "WHERE _id in (%s)", StringUtil.join(",", getIds()));
    }

    private List<Long> getIds() {
        ArrayList<Long> ids = new ArrayList<>();
        for (EventModel event : events) {
            ids.add(event.getId());
        }
        return ids;
    }

    public List<Map> getEventsMap() {
        ArrayList<Map> eventMaps = new ArrayList<>();
        for (EventModel event : events) {
            eventMaps.add(event.getEventMap());
        }
        return eventMaps;
    }

    public Boolean isEmpty() {
        return events == null || events.isEmpty();
    }

    public int size() {
        return events.size();
    }

    public int getPriority() {
        return events.get(0).getPriority();
    }

}
