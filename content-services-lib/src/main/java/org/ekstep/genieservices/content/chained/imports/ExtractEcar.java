package org.ekstep.genieservices.content.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.Decompress;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class ExtractEcar implements IChainable {

    private static final String TAG = ExtractEcar.class.getSimpleName();

    private IChainable nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {
        importContext.getTmpLocation().mkdirs();
        Decompress stage1 = new Decompress(importContext.getEcarFile(), importContext.getTmpLocation());

        if (stage1.unzip() && nextLink != null) {
            importContext.getMetadata().put(ServiceConstants.GeTransferEvent.FILE_SIZE, importContext.getEcarFile().length());
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Import content failed while extracting ecar.", TAG);
        }
    }

    @Override
    public IChainable then(IChainable link) {
        nextLink = link;
        return link;
    }
}
