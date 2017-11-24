package org.ekstep.genieservices.content.chained.move;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MoveContentErrorResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.bean.MoveContentContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 9/26/2017.
 *
 * @author anil
 */
public class ValidateDestinationContent implements IChainable<List<MoveContentErrorResponse>, MoveContentContext> {

    private static final String TAG = ValidateDestinationContent.class.getSimpleName();

    private IChainable<List<MoveContentErrorResponse>, MoveContentContext> nextLink;

    @Override
    public GenieResponse<List<MoveContentErrorResponse>> execute(AppContext appContext, MoveContentContext moveContentContext) {
        List<String> foldersList = new ArrayList<>();
        File storageFolder = FileUtil.getContentRootDir(moveContentContext.getDestinationFolder());

        if (storageFolder != null) {
            //get all the identifiers from the storage folder1
            String[] folders = storageFolder.list();

            if (folders != null && folders.length > 0) {
                foldersList = Arrays.asList(folders);

                List<String> validContentIdentifiers = ContentHandler.getValidIdentifiersFromPath(appContext, storageFolder, foldersList);
                if (validContentIdentifiers != null && validContentIdentifiers.size() > 0) {
                    moveContentContext.getValidContentIdsInDestination().addAll(validContentIdentifiers);
                }
            }
        }

        return nextLink.execute(appContext, moveContentContext);
    }

    @Override
    public IChainable<List<MoveContentErrorResponse>, MoveContentContext> then(IChainable<List<MoveContentErrorResponse>, MoveContentContext> link) {
        nextLink = link;
        return link;
    }
}
