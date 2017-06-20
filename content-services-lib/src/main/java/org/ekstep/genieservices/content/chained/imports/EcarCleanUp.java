package org.ekstep.genieservices.content.chained.imports;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.content.ContentConstants;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class EcarCleanUp implements IChainable {

    private static final String TAG = EcarCleanUp.class.getSimpleName();

    private IChainable nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {
        Logger.d(TAG, importContext.getTmpLocation().getPath());
        FileUtil.rm(importContext.getTmpLocation());

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ContentConstants.ECAR_CLEANUP_FAILED, "Ecar Cleanup failed", TAG);
        }
    }

    @Override
    public IChainable then(IChainable link) {
        nextLink = link;
        return link;
    }
}
