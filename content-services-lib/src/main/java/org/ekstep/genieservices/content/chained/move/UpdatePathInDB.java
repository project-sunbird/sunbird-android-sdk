package org.ekstep.genieservices.content.chained.move;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.content.bean.MoveContentContext;
import org.ekstep.genieservices.content.db.model.ContentModel;

/**
 * Created on 9/25/2017.
 *
 * @author anil
 */
public class UpdatePathInDB implements IChainable<Void, MoveContentContext> {

    private IChainable<Void, MoveContentContext> nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, MoveContentContext moveContentContext) {

        for (ContentModel contentModel : moveContentContext.getContentsInSource()) {
            String updatedPath = String.valueOf(moveContentContext.getContentRootFolder()) +
                    "/" + contentModel.getIdentifier();
            contentModel.setPath(updatedPath);
            contentModel.update();
        }

        return nextLink.execute(appContext, moveContentContext);
    }

    @Override
    public IChainable<Void, MoveContentContext> then(IChainable<Void, MoveContentContext> link) {
        nextLink = link;
        return link;
    }
}
