package org.ekstep.genieservices.content.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentExportResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.content.bean.ExportContentContext;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created on 6/19/2017.
 *
 * @author anil
 */
public class CleanTempLoc implements IChainable<ContentExportResponse, ExportContentContext> {

    private static final String TAG = CleanTempLoc.class.getSimpleName();

    private IChainable<ContentExportResponse, ExportContentContext> nextLink;

    @Override
    public GenieResponse<ContentExportResponse> execute(AppContext appContext, ExportContentContext exportContext) {

        File[] oldEcarFiles = exportContext.getDestinationFolder().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(ServiceConstants.FileExtension.CONTENT);
            }
        });

        try {
            long yesterday = System.currentTimeMillis() - (24 * 60 * 60 * 1000);

            if (oldEcarFiles != null) {
                for (File file : oldEcarFiles) {
                    if (file.lastModified() < yesterday) {
                        Logger.i(TAG, "One Day Old ecar files has been deleted: " + file.getName());

                        if (!file.delete()) {
                            Logger.i(TAG, "Could not delete old ecar files :" + file.getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.i(TAG, "Error during Old Ecar file deletion." + e.getMessage());
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
