package org.ekstep.genieservices.content.chained.move;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.content.bean.MoveContentContext;

/**
 * Created on 9/25/2017.
 *
 * @author anil
 */
public class DeleteSourceFolder implements IChainable<Void, MoveContentContext> {

    private static final String TAG = DeleteSourceFolder.class.getSimpleName();

    private IChainable<Void, MoveContentContext> nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, MoveContentContext moveContentContext) {


        //        return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.MOVE_FAILED, "", TAG);
        return nextLink.execute(appContext, moveContentContext);
    }

    @Override
    public IChainable<Void, MoveContentContext> then(IChainable<Void, MoveContentContext> link) {
        nextLink = link;
        return link;
    }
}
