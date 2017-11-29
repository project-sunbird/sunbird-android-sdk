package org.ekstep.genieservices.content.chained.move;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MoveContentResponse;
import org.ekstep.genieservices.commons.bean.enums.ExistingContentAction;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.content.bean.MoveContentContext;
import org.ekstep.genieservices.content.db.model.ContentModel;

import java.io.File;
import java.util.List;

/**
 * Created on 9/25/2017.
 *
 * @author anil
 */
public class UpdateSourceContentPathInDB implements IChainable<List<MoveContentResponse>, MoveContentContext> {

    private IChainable<List<MoveContentResponse>, MoveContentContext> nextLink;

    @Override
    public GenieResponse<List<MoveContentResponse>> execute(AppContext appContext, MoveContentContext moveContentContext) {

        ExistingContentAction existingContentAction = moveContentContext.getExistingContentAction();

        for (ContentModel contentModel : moveContentContext.getContentsInSource()) {
            if (existingContentAction == null) {
                updatePath(moveContentContext, contentModel);
            } else {
                if (existingContentAction == ExistingContentAction.KEEP_SOURCE && moveContentContext.getDuplicateContents().size() > 0) {
                    for (MoveContentResponse contentResponse : moveContentContext.getDuplicateContents()) {
                        if (contentResponse.getIdentifier().equalsIgnoreCase(contentModel.getIdentifier())) {
                            // do not update this folder path
                        } else {
                            updatePath(moveContentContext, contentModel);
                        }
                    }
                } else {
                    updatePath(moveContentContext, contentModel);
                }
            }
        }

        return nextLink.execute(appContext, moveContentContext);
    }

    private void updatePath(MoveContentContext moveContentContext, ContentModel contentModel) {
        String updatedPath = String.valueOf(moveContentContext.getContentRootFolder()) +
                "/" + contentModel.getIdentifier();
        contentModel.setPath(updatedPath);
        contentModel.update();
    }

    @Override
    public IChainable<List<MoveContentResponse>, MoveContentContext> then(IChainable<List<MoveContentResponse>, MoveContentContext> link) {
        nextLink = link;
        return link;
    }
}
