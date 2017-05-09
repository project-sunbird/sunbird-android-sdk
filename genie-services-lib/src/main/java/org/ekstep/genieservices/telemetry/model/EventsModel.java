package org.ekstep.genieservices.telemetry.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IDeviceInfo;
import org.ekstep.genieservices.commons.db.DbConstants;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.telemetry.db.contract.TelemetryEntry;
import org.ekstep.genieservices.telemetry.processors.EventProcessorFactory;
import org.ekstep.genieservices.telemetry.processors.IProcessEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by swayangjit on 26/4/17.
 */

public class EventsModel implements IReadable, ICleanable {

    private List<EventModel> events;
    private List<IProcessEvent> eventProcessors;
    private IDBSession mDbSession;


    private EventsModel(IDBSession dbSession, EventProcessorFactory factory, IDeviceInfo deviceInfo) {
        mDbSession = dbSession;
        events = new ArrayList<>();
        this.eventProcessors = factory.getProcessors(mDbSession,this, deviceInfo);
    }

    private EventsModel(IDBSession dbSession,IDeviceInfo deviceInfo) {
        this(dbSession, new EventProcessorFactory(),deviceInfo);
    }

    public static EventsModel build(IDBSession dbSession,IDeviceInfo deviceInfo){
        return new EventsModel(dbSession, new EventProcessorFactory(),deviceInfo);
    }

    public static EventsModel find(IDBSession dbSession,IDeviceInfo deviceInfo){
        EventsModel eventsModel = new EventsModel(dbSession, deviceInfo);
        dbSession.read(eventsModel);
        return eventsModel;
    }


    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst())
            do {
                events.add(EventModel.build(mDbSession, resultSet));
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
        for (EventModel event : events)
            eventMaps.add(event.getEventMap());
        return eventMaps;
    }

    public void clear(){
        mDbSession.clean(this);
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
