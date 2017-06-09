package org.ekstep.genieservices.telemetry.chained;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.chained.IChainable;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class TransportProcessedEventsImportEvent implements IChainable {

    private static final String TAG = TransportProcessedEventsImportEvent.class.getSimpleName();
    private IChainable nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Import telemetry failed.", TAG);
        }
    }

    @Override
    public IChainable then(IChainable link) {
        return null;
    }
}
