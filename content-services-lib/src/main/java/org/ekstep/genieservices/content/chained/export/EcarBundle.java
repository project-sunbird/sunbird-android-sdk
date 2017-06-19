package org.ekstep.genieservices.content.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentExportResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferEventKnowStructure;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.Compress;

import java.io.File;
import java.io.IOException;

/**
 * Created on 6/19/2017.
 *
 * @author anil
 */
public class EcarBundle implements IChainable<ContentExportResponse> {

    private static final String TAG = EcarBundle.class.getSimpleName();

    private IChainable<ContentExportResponse> nextLink;

    @Override
    public GenieResponse<ContentExportResponse> execute(AppContext appContext, ImportContext importContext) {

        File source = importContext.getTmpLocation();
        File destination = importContext.getEcarFile();

        Compress compressWorker = new Compress(source, destination);

        try {
            compressWorker.zip();
            importContext.getMetadata().put(GETransferEventKnowStructure.FILE_SIZE, destination.length());
        } catch (IOException e) {
            e.printStackTrace();
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, e.getMessage(), TAG);
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Export content failed", TAG);
        }
    }

    @Override
    public IChainable<ContentExportResponse> then(IChainable<ContentExportResponse> link) {
        nextLink = link;
        return link;
    }
}
