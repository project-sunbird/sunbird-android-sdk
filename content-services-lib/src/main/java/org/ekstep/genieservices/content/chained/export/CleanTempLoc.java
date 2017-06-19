package org.ekstep.genieservices.content.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.Logger;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created on 6/19/2017.
 *
 * @author anil
 */
public class CleanTempLoc implements IChainable {

    private static final String TAG = CleanTempLoc.class.getSimpleName();

    private IChainable nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {

        File[] oldEcarFiles = importContext.getDestinationFolder().listFiles(new FilenameFilter() {
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
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Export content failed", TAG);
        }
    }

    @Override
    public IChainable then(IChainable link) {
        nextLink = link;
        return link;
    }
}
