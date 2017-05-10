package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.IDeviceInfo;
import org.ekstep.genieservices.commons.db.DbConstants;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.commons.exception.DbException;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.telemetry.model.EventsModel;
import org.ekstep.genieservices.telemetry.model.ProcessedEventModel;
import org.ekstep.genieservices.telemetry.processors.EventProcessorFactory;
import org.ekstep.genieservices.telemetry.processors.IProcessEvent;

import java.io.IOException;
import java.util.List;

/**
 * Created by swayangjit on 7/5/17.
 */

public class ExportAdapter {
    private AppContext mAppContext;
    private EventsModel events;

    public ExportAdapter(AppContext context) {
        this(context, EventsModel.build(context.getDBSession(), context.getDeviceInfo()));
    }

    ExportAdapter(AppContext context, EventsModel events) {
        this.mAppContext = context;
        this.events = events;
    }

    public void export(IExport exportingStrategy) {
        try {
            Logger.i("service-GEA", "About to export");

            processEvents();

//            return exportingStrategy.send(mAppContext.getDBSession());
        } catch (DbException e) {
            Logger.e("service-GEA", "Db exception:" + e.getMessage());
//            return failedResponse(DbConstants.ERROR, e.getMessage());
        } catch (IOException e) {
            Logger.e("service-GEA", "Error zipping:" + e.getMessage());
//            return failedResponse("PROCESSING_ERROR", e.getMessage());
        } catch (Exception e) {
//            return failedResponse("NETWORK_ERROR", e.getMessage());
        }
    }

    // TODO Refactor this; make processEvents an explicit task
    public GenieResponse exportWithoutProcessEvents(IExport exportingStrategy) {
        try {
            Logger.i("service-GEA", "About to export");
            return exportingStrategy.send(mAppContext.getDBSession());
        } catch (DbException e) {
            Logger.e("service-GEA", "Db exception:" + e.getMessage());
            return failedResponse(DbConstants.ERROR, e.getMessage());
        } catch (Exception e) {
            return failedResponse("NETWORK_ERROR", e.getMessage());
        }
    }


    private GenieResponse failedResponse(String error, String errorMessage) {
        return GenieResponseBuilder.getErrorResponse("failed", error, errorMessage, String.class);
    }

    private void processEvents() throws IOException {

        final IDBSession dbSession = mAppContext.getDBSession();
        final IDeviceInfo deviceInfo = mAppContext.getDeviceInfo();

        dbSession.executeInTransaction(new IDBTransaction() {
            @Override
            public Void perform(IDBSession dbSession) {
                EventsModel events=null;
                events = EventsModel.find(dbSession, mAppContext.getDeviceInfo());
                while (!events.isEmpty()) {
                    List<IProcessEvent> eventProcessors = new EventProcessorFactory().getProcessors(dbSession, events, deviceInfo);
                    ProcessedEventModel processedEvent = ProcessedEventModel.build(dbSession);
                    for (IProcessEvent processor : eventProcessors) {
                        processedEvent = processor.process(processedEvent);
                    }
                    processedEvent.save();
                    events.clear();
                    events = EventsModel.find(dbSession, mAppContext.getDeviceInfo());
                }

                return null;
            }
        });

    }

}
