package org.ekstep.genieservices.content.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentExportResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.Compress;
import org.ekstep.genieservices.content.bean.ExportContentContext;

import java.io.File;
import java.io.IOException;

/**
 * Created on 6/19/2017.
 *
 * @author anil
 */
public class EcarBundle implements IChainable<ContentExportResponse, ExportContentContext> {

    private static final String TAG = EcarBundle.class.getSimpleName();

    private IChainable<ContentExportResponse, ExportContentContext> nextLink;

    @Override
    public GenieResponse<ContentExportResponse> execute(AppContext appContext, ExportContentContext exportContext) {

        File source = exportContext.getTmpLocation();
        File destination = exportContext.getEcarFile();

        try {
            Compress.zip(source, destination, null, null);
            exportContext.getMetadata().put(ServiceConstants.FILE_SIZE, destination.length());
        } catch (IOException e) {
            e.printStackTrace();
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, e.getMessage(), TAG);
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, exportContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Export content failed", TAG);
        }
    }

    @Override
    public IChainable<ContentExportResponse, ExportContentContext> then(IChainable<ContentExportResponse, ExportContentContext> link) {
        nextLink = link;
        return link;
    }
}
