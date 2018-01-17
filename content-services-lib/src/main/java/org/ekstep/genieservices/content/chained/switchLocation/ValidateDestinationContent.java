package org.ekstep.genieservices.content.chained.switchLocation;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.SwitchContentResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.bean.SwitchContentContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValidateDestinationContent implements IChainable<List<SwitchContentResponse>, SwitchContentContext> {

    private static final String TAG = ValidateDestinationContent.class.getSimpleName();

    private IChainable<List<SwitchContentResponse>, SwitchContentContext> nextLink;

    @Override
    public GenieResponse<List<SwitchContentResponse>> execute(AppContext appContext, SwitchContentContext switchContentContext) {
        List<String> foldersList = new ArrayList<>();
        File storageFolder = FileUtil.getContentRootDir(switchContentContext.getDestinationFolder());

        if (storageFolder != null) {
            //get all the identifiers from the storage folder1
            String[] folders = storageFolder.list();

            if (folders != null && folders.length > 0) {
                foldersList = Arrays.asList(folders);

                List<String> validContentIdentifiers = ContentHandler.getValidIdentifiersFromPath(appContext, storageFolder, foldersList);
                if (validContentIdentifiers != null && validContentIdentifiers.size() > 0) {
                    switchContentContext.getValidContentIdsInDestination().addAll(validContentIdentifiers);
                }
            }
        }

        return nextLink.execute(appContext, switchContentContext);
    }

    @Override
    public IChainable<List<SwitchContentResponse>, SwitchContentContext> then(IChainable<List<SwitchContentResponse>, SwitchContentContext> link) {
        nextLink = link;
        return link;
    }
}
