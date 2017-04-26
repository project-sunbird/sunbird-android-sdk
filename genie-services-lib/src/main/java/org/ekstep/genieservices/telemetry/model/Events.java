package org.ekstep.genieservices.telemetry.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.DbConstants;
import org.ekstep.genieservices.commons.db.core.ICleanable;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.telemetry.db.contract.TelemetryEntry;
import org.ekstep.genieservices.telemetry.processors.EventProcessorFactory;
import org.ekstep.genieservices.telemetry.processors.IProcessEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by swayangjit on 26/4/17.
 */

public class Events implements IReadable, ICleanable {

    private List<Event> events;
    private List<IProcessEvent> eventProcessors;
    private AppContext mAppContext;


    private Events(AppContext appContext, EventProcessorFactory factory) {
        mAppContext = appContext;
        events = new ArrayList<>();
//        this.eventProcessors = factory.getProcessors(this, new DeviceInfo(context));
    }

    private Events(AppContext appContext) {
        this(appContext, new EventProcessorFactory());
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst())
            do {
                events.add(Event.getForRead(mAppContext, resultSet));
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
        for (Event event : events) {
            ids.add(event.getId());
        }
        return ids;
    }
}
