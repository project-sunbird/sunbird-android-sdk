package org.ekstep.genieservices.content.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created on 6/19/2017.
 *
 * @author anil
 */
public class DeviceMemoryCheck implements IChainable {

    private static final String TAG = DeviceMemoryCheck.class.getSimpleName();

    private IChainable nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {
        long deviceUsableSpace = FileUtil.getFreeUsableSpace(importContext.getDestinationFolder());
        long fileSize = 0;
        List<Map<String, Object>> items = importContext.getItems();

        //check for export files
        if (items != null) {
            try {
                for (Map item : items) {
                    try {
                        fileSize = fileSize + new BigDecimal((Double) item.get("size")).longValue();
                    } catch (Exception e) {
                        Logger.e(TAG, e.getMessage());
                        continue;
                    }
                }

                if (!isFreeSpaceAvailable(deviceUsableSpace, fileSize)) {
                    return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Device memory full.", TAG);
                }
            } catch (Exception e) {
                Logger.e(TAG, e.getMessage());
            }
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

    private boolean isFreeSpaceAvailable(long deviceUsableSpace, long fileSpace) {
        long BUFFER_SIZE = 0;
        if (deviceUsableSpace > 0 && deviceUsableSpace < (fileSpace + BUFFER_SIZE)) {
            Logger.e(TAG, "Device memory full!");
            return false;
        }
        return true;
    }

}
