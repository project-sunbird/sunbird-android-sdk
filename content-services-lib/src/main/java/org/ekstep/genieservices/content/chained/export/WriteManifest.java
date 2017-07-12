package org.ekstep.genieservices.content.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentExportResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.content.bean.ExportContentContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created on 6/19/2017.
 *
 * @author anil
 */
public class WriteManifest implements IChainable<ContentExportResponse, ExportContentContext> {

    private static final String TAG = WriteManifest.class.getSimpleName();

    private IChainable<ContentExportResponse, ExportContentContext> nextLink;

    @Override
    public GenieResponse<ContentExportResponse> execute(AppContext appContext, ExportContentContext exportContext) {

        long deviceUsableSpace = FileUtil.getFreeUsableSpace(exportContext.getDestinationFolder());
        if (deviceUsableSpace > 0 && deviceUsableSpace < (1024 * 1024)) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Device memory full.", TAG);
        }

        try {
            File manifestFile = new File(exportContext.getTmpLocation(), "manifest.json");
            FileOutputStream fileOutputStream = new FileOutputStream(manifestFile);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            String json = GsonUtil.toJson(exportContext.getManifest());

            outputStreamWriter.write(json);
            outputStreamWriter.close();
            fileOutputStream.close();
        } catch (IOException e) {
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
