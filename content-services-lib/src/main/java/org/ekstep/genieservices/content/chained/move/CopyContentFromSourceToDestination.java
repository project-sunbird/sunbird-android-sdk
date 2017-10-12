package org.ekstep.genieservices.content.chained.move;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MoveContentProgress;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.content.bean.MoveContentContext;
import org.ekstep.genieservices.content.db.model.ContentModel;
import org.ekstep.genieservices.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

/**
 * Created on 9/25/2017.
 *
 * @author anil
 */
public class CopyContentFromSourceToDestination implements IChainable<Void, MoveContentContext> {

    private static final String TAG = CopyContentFromSourceToDestination.class.getSimpleName();

    private IChainable<Void, MoveContentContext> nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, MoveContentContext moveContentContext) {

        if (!CollectionUtil.isNullOrEmpty(moveContentContext.getContentsInSource())) {
            int currentCount = 0;
            EventBus.postEvent(new MoveContentProgress(currentCount, moveContentContext.getContentsInSource().size()));

            for (ContentModel contentModel : moveContentContext.getContentsInSource()) {
                File source = new File(contentModel.getPath());
                try {
                    File contentDestination = new File(moveContentContext.getContentRootFolder(), contentModel.getIdentifier());
                    FileUtil.copyFolder(source, contentDestination);
                    currentCount++;
                    EventBus.postEvent(new MoveContentProgress(currentCount, moveContentContext.getContentsInSource().size()));
                } catch (IOException e) {
                    Logger.e(TAG, "Move failed", e);
                    return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.MOVE_FAILED, e.getMessage(), TAG);
                }
            }

            return nextLink.execute(appContext, moveContentContext);
        }

        return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.MOVE_FAILED, ServiceConstants.ErrorMessage.NO_CONTENT_TO_MOVE, TAG);
    }

    @Override
    public IChainable<Void, MoveContentContext> then(IChainable<Void, MoveContentContext> link) {
        nextLink = link;
        return link;
    }
}
