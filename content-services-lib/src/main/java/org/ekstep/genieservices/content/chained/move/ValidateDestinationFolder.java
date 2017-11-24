package org.ekstep.genieservices.content.chained.move;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MoveContentResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.content.bean.MoveContentContext;

import java.io.File;
import java.util.List;

/**
 * Created on 9/25/2017.
 *
 * @author anil
 */
public class ValidateDestinationFolder implements IChainable<List<MoveContentResponse>, MoveContentContext> {

    private static final String TAG = ValidateDestinationFolder.class.getSimpleName();

    private IChainable<List<MoveContentResponse>, MoveContentContext> nextLink;

    @Override
    public GenieResponse<List<MoveContentResponse>> execute(AppContext appContext, MoveContentContext moveContentContext) {
        if (moveContentContext.getDestinationFolder().isDirectory() && moveContentContext.getDestinationFolder().canWrite()) {
            File contentRootFolder;
            if (moveContentContext.getDestinationFolder().getPath().endsWith(FileUtil.CONTENT_FOLDER)) {
                contentRootFolder = moveContentContext.getDestinationFolder();
            } else {
                // Make content folder if not exists in destination folder.
                contentRootFolder = FileUtil.getContentRootDir(moveContentContext.getDestinationFolder());
            }

            moveContentContext.setContentRootFolder(contentRootFolder);

            return nextLink.execute(appContext, moveContentContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.MOVE_FAILED, ServiceConstants.ErrorMessage.NOT_WRITABLE, TAG);
        }
    }

    @Override
    public IChainable<List<MoveContentResponse>, MoveContentContext> then(IChainable<List<MoveContentResponse>, MoveContentContext> link) {
        nextLink = link;
        return link;
    }
}
