package org.ekstep.genieservices.content.chained.imports;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.content.ContentConstants;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.bean.ImportContentContext;

import java.util.List;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class UpdateSizeOnDevice implements IChainable<List<ContentImportResponse>, ImportContentContext> {

    private static final String TAG = UpdateSizeOnDevice.class.getSimpleName();

    private IChainable<List<ContentImportResponse>, ImportContentContext> nextLink;

    @Override
    public GenieResponse<List<ContentImportResponse>> execute(final AppContext appContext, ImportContentContext importContext) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentHandler.updateSizeOnDevice(appContext);
            }
        }).start();


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
