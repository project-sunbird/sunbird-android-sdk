package org.ekstep.genieservices.content.chained.move;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MoveContentResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.content.bean.MoveContentContext;

import java.io.File;
import java.util.List;

public class DeleteDestinationFolder implements IChainable<List<MoveContentResponse>, MoveContentContext> {
    private IChainable<List<MoveContentResponse>, MoveContentContext> nextLink;

    @Override
    public GenieResponse<List<MoveContentResponse>> execute(AppContext appContext, MoveContentContext moveContentContext) {
        if (moveContentContext.deleteDestination()) {
            File contentRootFolder;
            if (moveContentContext.getDestinationFolder().getPath().endsWith(FileUtil.CONTENT_FOLDER)) {
                contentRootFolder = moveContentContext.getDestinationFolder();
            } else {
                // Make content folder if not exists in destination folder.
                contentRootFolder = FileUtil.getContentRootDir(moveContentContext.getDestinationFolder());
            }

            FileUtil.rm(contentRootFolder);
        }

        return nextLink.execute(appContext, moveContentContext);
    }

    @Override
    public IChainable<List<MoveContentResponse>, MoveContentContext> then(IChainable<List<MoveContentResponse>, MoveContentContext> link) {
        nextLink = link;
        return link;
    }
}
