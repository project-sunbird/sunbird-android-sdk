package org.ekstep.genieservices.content.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.content.ContentConstants;
import org.ekstep.genieservices.content.bean.ImportContentContext;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class DeviceMemoryCheck implements IChainable<Void, ImportContentContext> {
    private static final String TAG = DeviceMemoryCheck.class.getSimpleName();

    private IChainable<Void, ImportContentContext> nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContentContext importContext) {
        long deviceUsableSpace = FileUtil.getFreeUsableSpace(importContext.getDestinationFolder());
        long ecarFileSpace = importContext.getEcarFile().length();
        long bufferSize = calculateBufferSize(ecarFileSpace);
        if (deviceUsableSpace < (ecarFileSpace + bufferSize)) {
            Logger.e(TAG, "Import failed. Device memory full!!!");
            return GenieResponseBuilder.getErrorResponse(ContentConstants.IMPORT_FAILED_DEVICE_MEMORY_FULL, "Import failed. Device memory full.", TAG);
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Import content failed", TAG);
        }
    }

    @Override
    public IChainable<Void, ImportContentContext> then(IChainable<Void, ImportContentContext> link) {
        nextLink = link;
        return link;
    }

    private long calculateBufferSize(long ecarFileSize) {
        long bufferSize = 0;
        if (ecarFileSize > 0) {
            bufferSize = ecarFileSize / 5;
        }
        return bufferSize;
    }
}
