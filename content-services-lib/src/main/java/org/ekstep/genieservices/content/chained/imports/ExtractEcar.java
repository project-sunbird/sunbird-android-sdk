package org.ekstep.genieservices.content.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.Decompress;
import org.ekstep.genieservices.content.bean.ImportContentContext;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class ExtractEcar implements IChainable<Void, ImportContentContext> {

    private static final String TAG = ExtractEcar.class.getSimpleName();

    private IChainable<Void, ImportContentContext> nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContentContext importContext) {
        importContext.getTmpLocation().mkdirs();

        if (Decompress.unzip(importContext.getEcarFile(), importContext.getTmpLocation()) && nextLink != null) {
            importContext.getMetadata().put(ServiceConstants.GeTransferEvent.FILE_SIZE, importContext.getEcarFile().length());
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Import content failed while extracting ecar.", TAG);
        }
    }

    @Override
    public IChainable<Void, ImportContentContext> then(IChainable<Void, ImportContentContext> link) {
        nextLink = link;
        return link;
    }
}
