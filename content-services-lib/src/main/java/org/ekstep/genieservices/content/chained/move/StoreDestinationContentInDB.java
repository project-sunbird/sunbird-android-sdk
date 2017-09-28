package org.ekstep.genieservices.content.chained.move;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.content.bean.MoveContentContext;
import org.ekstep.genieservices.content.db.model.ContentModel;

/**
 * Created on 9/25/2017.
 *
 * @author anil
 */
public class StoreDestinationContentInDB implements IChainable<Void, MoveContentContext> {

    @Override
    public GenieResponse<Void> execute(AppContext appContext, MoveContentContext moveContentContext) {
        for (ContentModel contentModel : moveContentContext.getContentsInDestination()) {
            contentModel.update();
        }

        return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
    }

    @Override
    public IChainable<Void, MoveContentContext> then(IChainable<Void, MoveContentContext> link) {
        return link;
    }
}
