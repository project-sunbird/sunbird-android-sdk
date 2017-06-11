package org.ekstep.genieservices.telemetry.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.telemetry.model.ProcessedEventsModel;

/**
 * Created on 6/10/2017.
 *
 * @author anil
 */
public class CleanCurrentDatabase implements IChainable {

    private static final String TAG = CleanCurrentDatabase.class.getSimpleName();
    private IChainable nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {

        ProcessedEventsModel processedEventsModel = ProcessedEventsModel.build(appContext.getDBSession());
        processedEventsModel.deleteAll();

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Import profile failed", TAG);
        }
    }

    @Override
    public IChainable then(IChainable link) {
        nextLink = link;
        return link;
    }
}
