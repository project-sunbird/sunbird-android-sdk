package org.ekstep.genieservices.content.chained.move;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MoveContentProgress;
import org.ekstep.genieservices.commons.bean.MoveContentResponse;
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
public class CopyContentFromSourceToDestination implements IChainable<List<MoveContentResponse>, MoveContentContext> {

    private static final String TAG = CopyContentFromSourceToDestination.class.getSimpleName();

    private IChainable<List<MoveContentResponse>, MoveContentContext> nextLink;

    @Override
    public GenieResponse<List<MoveContentResponse>> execute(AppContext appContext, MoveContentContext moveContentContext) {

        if (!CollectionUtil.isNullOrEmpty(moveContentContext.getContentsInSource())) {
            int currentCount = 0;
            EventBus.postEvent(new MoveContentProgress(currentCount, moveContentContext.getContentsInSource().size()));

            ExistingContentAction existingContentAction = moveContentContext.getExistingContentAction();
            List<MoveContentResponse> duplicateContents = moveContentContext.getDuplicateContents();

            for (ContentModel contentModelInSource : moveContentContext.getContentsInSource()) {
                try {
                    if (duplicateContents != null && duplicateContents.size() > 0) {
                        for (MoveContentResponse contentResponse : duplicateContents) {
                            if (contentResponse.getIdentifier().equalsIgnoreCase(contentModelInSource.getIdentifier())) {
                                //this means by default we keep contents in the destination
                                if (existingContentAction == null) {
                                    currentCount++;
                                } else {
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
                                            //FileUtil.copyFolder(source, contentDestination);
                                            // TODO: 29/11/17 Check if the destination file has to be removed
                                            currentCount++;
                                            break;
                                        case IGNORE:
                                        case KEEP_DESTINATION:
                                        default:
                                            currentCount++;
                                            break;
                                    }
                                }
                            } else {
                                File source = new File(contentModelInSource.getPath());
                                File contentDestination = new File(moveContentContext.getContentRootFolder(), contentModelInSource.getIdentifier());
                                FileUtil.copyFolder(source, contentDestination);
                                currentCount++;
                            }
                        }
                    } else {
                        File source = new File(contentModelInSource.getPath());
                        File contentDestination = new File(moveContentContext.getContentRootFolder(), contentModelInSource.getIdentifier());
                        FileUtil.copyFolder(source, contentDestination);
                        currentCount++;
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
    public IChainable<List<MoveContentResponse>, MoveContentContext> then(IChainable<List<MoveContentResponse>, MoveContentContext> link) {
        nextLink = link;
        return link;
    }
}
