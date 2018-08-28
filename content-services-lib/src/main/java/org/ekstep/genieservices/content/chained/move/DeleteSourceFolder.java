package org.ekstep.genieservices.content.chained.move;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MoveContentResponse;
import org.ekstep.genieservices.commons.bean.enums.ExistingContentAction;
import org.ekstep.genieservices.commons.bean.enums.MoveContentStatus;
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
            } else {
                if (existingContentAction != null && moveContentContext.getDuplicateContents().size() > 0) {
                    for (MoveContentResponse duplicateContent : moveContentContext.getDuplicateContents()) {
                        if (duplicateContent.getIdentifier().equalsIgnoreCase(contentModel.getIdentifier())) {
                            if (duplicateContent.getStatus() == MoveContentStatus.HIGHER_VERSION_IN_DESTINATION && existingContentAction != ExistingContentAction.KEEP_HIGHER_VERSION) {
                                //Remove the renamed destination folder
                                FileUtil.rm(getRenamedFolder(moveContentContext, duplicateContent.getIdentifier()));

                                //Remove source folder as well
                                FileUtil.rm(new File(contentModel.getPath()));
                            } else if (duplicateContent.getStatus() == MoveContentStatus.LOWER_VERSION_IN_DESTINATION && existingContentAction != ExistingContentAction.KEEP_LOWER_VERSION) {
                                //Remove the renamed destination folder
                                FileUtil.rm(getRenamedFolder(moveContentContext, duplicateContent.getIdentifier()));

                                //Remove source folder as well
                                FileUtil.rm(new File(contentModel.getPath()));
                            } else {
                                FileUtil.rm(new File(contentModel.getPath()));
                            }
                        } else {
                            FileUtil.rm(new File(contentModel.getPath()));
                        }
                    }
                } else {
                    FileUtil.rm(new File(contentModel.getPath()));
                }
            }
        }

        return nextLink.execute(appContext, moveContentContext);
    }

    private File getRenamedFolder(MoveContentContext moveContentContext, String identifier) {
        return new File(moveContentContext.getContentRootFolder(), identifier + "_temp");
    }

    @Override
    public IChainable<List<MoveContentResponse>, MoveContentContext> then(IChainable<List<MoveContentResponse>, MoveContentContext> link) {
        nextLink = link;
        return link;
    }
}
