package org.ekstep.genieservices.content.chained.move;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MoveContentErrorResponse;
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
public class DeleteSourceFolder implements IChainable<List<MoveContentErrorResponse>, MoveContentContext> {

    private IChainable<List<MoveContentErrorResponse>, MoveContentContext> nextLink;

    @Override
    public GenieResponse<List<MoveContentErrorResponse>> execute(AppContext appContext, MoveContentContext moveContentContext) {

        for (ContentModel contentModel : moveContentContext.getContentsInSource()) {
            FileUtil.rm(new File(contentModel.getPath()));
        }

        return nextLink.execute(appContext, moveContentContext);
    }

    @Override
    public IChainable<List<MoveContentErrorResponse>, MoveContentContext> then(IChainable<List<MoveContentErrorResponse>, MoveContentContext> link) {
        nextLink = link;
        return link;
    }
}
