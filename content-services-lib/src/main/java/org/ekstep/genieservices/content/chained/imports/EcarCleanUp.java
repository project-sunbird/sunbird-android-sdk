package org.ekstep.genieservices.content.chained.imports;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.content.ContentConstants;
import org.ekstep.genieservices.content.bean.ImportContentContext;

import java.io.File;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class EcarCleanUp implements IChainable<Void, ImportContentContext> {

    private static final String TAG = EcarCleanUp.class.getSimpleName();
    private final File tmpLocation;

    private IChainable<Void, ImportContentContext> nextLink;

    public EcarCleanUp(File tmpLocation) {
        this.tmpLocation = tmpLocation;
    }

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContentContext importContext) {
        Logger.d(TAG, tmpLocation.getPath());
        FileUtil.rm(tmpLocation);

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ContentConstants.ECAR_CLEANUP_FAILED, "Ecar Cleanup failed", TAG);
        }
    }

    @Override
    public IChainable<Void, ImportContentContext> then(IChainable<Void, ImportContentContext> link) {
        nextLink = link;
        return link;
    }
}
