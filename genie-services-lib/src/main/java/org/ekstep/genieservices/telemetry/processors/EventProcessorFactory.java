package org.ekstep.genieservices.telemetry.processors;


import org.ekstep.genieservices.commons.IDeviceInfo;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.telemetry.model.EventsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swayangjit on 26/4/17.
 */

public class EventProcessorFactory {
    public List<IProcessEvent> getProcessors(IDBSession dbSession, EventsModel events, IDeviceInfo deviceInfo) {
        ArrayList<IProcessEvent> processors = new ArrayList<>();
        processors.add(new DataPopulator(dbSession,events, deviceInfo));
        processors.add(new DataZipper());
        return processors;
    }
}
