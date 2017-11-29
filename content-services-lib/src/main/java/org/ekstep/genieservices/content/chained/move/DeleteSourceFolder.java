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
public class DeleteSourceFolder implements IChainable<List<MoveContentResponse>, MoveContentContext> {

    private IChainable<List<MoveContentResponse>, MoveContentContext> nextLink;

    @Override
    public GenieResponse<List<MoveContentResponse>> execute(AppContext appContext, MoveContentContext moveContentContext) {

        ExistingContentAction existingContentAction = moveContentContext.getExistingContentAction();

        for (ContentModel contentModel : moveContentContext.getContentsInSource()) {
            if (existingContentAction == null) {
                FileUtil.rm(new File(contentModel.getPath()));
            }else{
                if (existingContentAction == ExistingContentAction.KEEP_SOURCE && moveContentContext.getDuplicateContents().size() > 0){
                    for (MoveContentResponse contentResponse : moveContentContext.getDuplicateContents()){
                        if (contentResponse.getIdentifier().equalsIgnoreCase(contentModel.getIdentifier())){
                            // do not remove this folder, it is needed
                        }else {
                            FileUtil.rm(new File(contentModel.getPath()));
                        }
                    }
                }else{
                    FileUtil.rm(new File(contentModel.getPath()));
                }
            }
        }

        return nextLink.execute(appContext, moveContentContext);
    }

    @Override
    public IChainable<List<MoveContentResponse>, MoveContentContext> then(IChainable<List<MoveContentResponse>, MoveContentContext> link) {
        nextLink = link;
        return link;
    }
}
