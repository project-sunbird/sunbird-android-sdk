package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponse;
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
import java.util.Arrays;
import java.util.List;

/**
 * Created by swayangjit on 7/5/17.
 */

public class ExportAdapter {
    private AppContext mAppContext;
    private EventsModel events;

    public ExportAdapter(AppContext context) {
        this(context,EventsModel.build(context.getDBSession(),context.getDeviceInfo()));
    }

    ExportAdapter(AppContext context, EventsModel events) {
        this.mAppContext = context;
        this.events = events;
    }

    public GenieResponse export(IExport exportingStrategy) {
        try {
            Logger.i(mAppContext,"service-GEA", "About to export");

            processEvents();

            return exportingStrategy.send(mAppContext.getDBSession());
        } catch (DbException e) {
            Logger.e(mAppContext,"service-GEA", "Db exception:" + e.getMessage());
            return failedResponse(DbConstants.ERROR, e.getMessage());
        } catch (IOException e) {
            Logger.e(mAppContext,"service-GEA", "Error zipping:" + e.getMessage());
            return failedResponse("PROCESSING_ERROR", e.getMessage());
        }/* catch (SQLiteException e) {
            return failedResponse("NETWORK_ERROR", e.getMessage());
        }*/
    }

    // TODO Refactor this; make processEvents an explicit task
    public GenieResponse exportWithoutProcessEvents(IExport exportingStrategy) {
        try {
            Logger.i(mAppContext,"service-GEA", "About to export");
            return exportingStrategy.send(mAppContext.getDBSession());
        } catch (DbException e) {
            Logger.e(mAppContext,"service-GEA", "Db exception:" + e.getMessage());
            return failedResponse(DbConstants.ERROR, e.getMessage());
        }/* catch (SQLiteException e) {
            return failedResponse("NETWORK_ERROR", e.getMessage());
        }*/
    }


    private GenieResponse failedResponse(String error, String errorMessage) {
        return GenieResponse.getErrorResponse(mAppContext,"failed", error, errorMessage);
    }

    private void processEvents() throws IOException {

       final  IDBSession dbSession=mAppContext.getDBSession();
       final  IDeviceInfo deviceInfo=mAppContext.getDeviceInfo();
        while (!events.isEmpty()) {
            dbSession.executeInTransaction(new IDBTransaction() {
                @Override
                public Void perform(IDBSession dbSession) throws IOException {
                    EventsModel events=EventsModel.find(dbSession,mAppContext.getDeviceInfo());
                    List<IProcessEvent> eventProcessors=new EventProcessorFactory().getProcessors(dbSession,events,deviceInfo);
                    ProcessedEventModel processedEvent =ProcessedEventModel.build(dbSession);
                    for (IProcessEvent processor : eventProcessors) {
                        processedEvent = processor.process(processedEvent);
                    }
                    events.clean();
                    events=EventsModel.find(dbSession,mAppContext.getDeviceInfo());
                    return null;
                }
            });

        }
    }

}
