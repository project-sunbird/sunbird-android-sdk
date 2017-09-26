package org.ekstep.genieservices.content.chained.move;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.bean.MoveContentContext;
import org.ekstep.genieservices.content.db.model.ContentModel;

import java.util.List;

/**
 * Created on 9/25/2017.
 *
 * @author anil
 */
public class DuplicateContentCheck implements IChainable<Void, MoveContentContext> {

    private static final String TAG = DuplicateContentCheck.class.getSimpleName();

    private IChainable<Void, MoveContentContext> nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, MoveContentContext moveContentContext) {
        List<ContentModel> contentsInSource = ContentHandler.getAllContentModelToMove(appContext.getDBSession(), moveContentContext.getContentIds());
        moveContentContext.setContentsInSource(contentsInSource);

        // Read content in destination folder.
        // TODO: 9/25/2017 - Add implementation to check duplicate.

//        return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.MOVE_FAILED, "", TAG);
        return nextLink.execute(appContext, moveContentContext);
    }

    @Override
    public IChainable<Void, MoveContentContext> then(IChainable<Void, MoveContentContext> link) {
        nextLink = link;
        return link;
    }
}
