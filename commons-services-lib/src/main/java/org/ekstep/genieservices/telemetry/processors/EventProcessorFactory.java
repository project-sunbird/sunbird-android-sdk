package org.ekstep.genieservices.telemetry.processors;


import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IDeviceInfo;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.telemetry.model.EventsModel;
import org.ekstep.genieservices.telemetry.model.ProcessedEventModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 26/4/17.
 *
 * @author swayangjit
 */
public class EventProcessorFactory {

    public static void processEvents(final AppContext appContext) {
        appContext.getDBSession().executeInTransaction(new IDBTransaction() {
            @Override
            public Void perform(IDBSession dbSession) {
                EventsModel events = EventsModel.find(dbSession);

                while (!events.isEmpty()) {
                    List<IProcessEvent> eventProcessors = getProcessors(dbSession, events, appContext.getDeviceInfo());
                    ProcessedEventModel processedEvent = ProcessedEventModel.build(dbSession);
                    for (IProcessEvent processor : eventProcessors) {
                        processedEvent = processor.process(processedEvent);
                    }
                    processedEvent.save();

                    events.delete();
                    events = EventsModel.find(dbSession);
                }
                return null;
            }
        });
    }

    private static List<IProcessEvent> getProcessors(IDBSession dbSession, EventsModel events, IDeviceInfo deviceInfo) {
        ArrayList<IProcessEvent> processors = new ArrayList<>();
        processors.add(new DataPopulator(dbSession, events, deviceInfo));
        processors.add(new DataZipper());
        return processors;
    }
}
