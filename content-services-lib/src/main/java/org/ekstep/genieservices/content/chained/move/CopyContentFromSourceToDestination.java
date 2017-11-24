package org.ekstep.genieservices.content.chained.move;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MoveContentErrorResponse;
import org.ekstep.genieservices.commons.bean.MoveContentProgress;
import org.ekstep.genieservices.commons.bean.enums.ExistingContentAction;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.content.bean.MoveContentContext;
import org.ekstep.genieservices.content.db.model.ContentModel;
import org.ekstep.genieservices.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created on 9/25/2017.
 *
 * @author anil
 */
public class CopyContentFromSourceToDestination implements IChainable<List<MoveContentErrorResponse>, MoveContentContext> {

    private static final String TAG = CopyContentFromSourceToDestination.class.getSimpleName();

    private IChainable<List<MoveContentErrorResponse>, MoveContentContext> nextLink;

    @Override
    public GenieResponse<List<MoveContentErrorResponse>> execute(AppContext appContext, MoveContentContext moveContentContext) {

        if (!CollectionUtil.isNullOrEmpty(moveContentContext.getContentsInSource())) {
            int currentCount = 0;
            EventBus.postEvent(new MoveContentProgress(currentCount, moveContentContext.getContentsInSource().size()));

            ExistingContentAction existingContentAction = moveContentContext.getExistingContentAction();

            for (ContentModel contentModelInSource : moveContentContext.getContentsInSource()) {
                File source = new File(contentModelInSource.getPath());
                try {
                    if (moveContentContext.getValidContentIdsInDestination().contains(contentModelInSource.getIdentifier())) {
                        File contentDestination = new File(moveContentContext.getContentRootFolder(), contentModelInSource.getIdentifier());

                        switch (existingContentAction) {
                            case KEEP_HIGHER_VERSION:
                                break;
                            case KEEP_LOWER_VERSION:
                                break;
                            case KEEP_SOURCE:
                                // TODO: 24/11/17
                                //Rename the destination folder to identifier_temp
                                //Delete of these temp folders will happen only on successful completion of copying the files
                                //Else rollback of temp folders will happen when cancel is initiated
                                FileUtil.copyFolder(source, contentDestination);
                                currentCount++;
                                break;
                            case IGNORE:
                            case KEEP_DESTINATION:
                                if (moveContentContext.getValidContentIdsInDestination().contains(contentModelInSource.getIdentifier())) {
                                    currentCount++;
                                } else {
                                    FileUtil.copyFolder(source, contentDestination);
                                    currentCount++;
                                }
                                break;
                        }
                    }
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
    public IChainable<List<MoveContentErrorResponse>, MoveContentContext> then(IChainable<List<MoveContentErrorResponse>, MoveContentContext> link) {
        nextLink = link;
        return link;
    }
}
