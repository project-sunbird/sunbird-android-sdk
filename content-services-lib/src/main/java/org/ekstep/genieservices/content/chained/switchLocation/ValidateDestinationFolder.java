package org.ekstep.genieservices.content.chained.switchLocation;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.SwitchContentResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.content.bean.SwitchContentContext;

import java.io.File;
import java.util.List;

public class ValidateDestinationFolder implements IChainable<List<SwitchContentResponse>, SwitchContentContext> {

    private static final String TAG = ValidateDestinationFolder.class.getSimpleName();

    private IChainable<List<SwitchContentResponse>, SwitchContentContext> nextLink;

    @Override
    public GenieResponse<List<SwitchContentResponse>> execute(AppContext appContext, SwitchContentContext switchContentContext) {
        if (switchContentContext.getDestinationFolder().isDirectory() && switchContentContext.getDestinationFolder().canWrite()) {
            File contentRootFolder;
            if (switchContentContext.getDestinationFolder().getPath().endsWith(FileUtil.CONTENT_FOLDER)) {
                contentRootFolder = switchContentContext.getDestinationFolder();
            } else {
                // Make content folder if not exists in destination folder.
                contentRootFolder = FileUtil.getContentRootDir(switchContentContext.getDestinationFolder());
            }

            switchContentContext.setContentRootFolder(contentRootFolder);

            return nextLink.execute(appContext, switchContentContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.SWITCH_FAILED, ServiceConstants.ErrorMessage.NOT_WRITABLE, TAG);
        }
    }

    @Override
    public IChainable<List<SwitchContentResponse>, SwitchContentContext> then(IChainable<List<SwitchContentResponse>, SwitchContentContext> link) {
        nextLink = link;
        return link;
    }
}
