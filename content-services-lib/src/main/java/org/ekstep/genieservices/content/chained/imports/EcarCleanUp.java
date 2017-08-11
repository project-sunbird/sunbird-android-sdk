package org.ekstep.genieservices.content.chained.imports;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.content.ContentConstants;
import org.ekstep.genieservices.content.bean.ImportContentContext;

import java.io.File;
import java.util.List;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class EcarCleanUp implements IChainable<List<ContentImportResponse>, ImportContentContext> {

    private static final String TAG = EcarCleanUp.class.getSimpleName();
    private final File tmpLocation;

    private IChainable<List<ContentImportResponse>, ImportContentContext> nextLink;

    public EcarCleanUp(File tmpLocation) {
        this.tmpLocation = tmpLocation;
    }

    @Override
    public GenieResponse<List<ContentImportResponse>> execute(AppContext appContext, ImportContentContext importContext) {
        Logger.d(TAG, tmpLocation.getPath());
        FileUtil.rm(tmpLocation);

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ContentConstants.ECAR_CLEANUP_FAILED, "Ecar cleanup failed", TAG);
        }
    }

    @Override
    public IChainable<List<ContentImportResponse>, ImportContentContext> then(IChainable<List<ContentImportResponse>, ImportContentContext> link) {
        nextLink = link;
        return link;
    }
}
