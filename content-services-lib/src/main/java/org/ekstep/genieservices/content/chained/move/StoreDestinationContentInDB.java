package org.ekstep.genieservices.content.chained.move;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MoveContentResponse;
import org.ekstep.genieservices.commons.bean.enums.ExistingContentAction;
import org.ekstep.genieservices.commons.bean.enums.MoveContentStatus;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.bean.MoveContentContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created on 9/25/2017.
 *
 * @author anil
 */
public class StoreDestinationContentInDB implements IChainable<List<MoveContentResponse>, MoveContentContext> {

    private static final String TAG = StoreDestinationContentInDB.class.getSimpleName();

    @Override
    public GenieResponse<List<MoveContentResponse>> execute(AppContext appContext, MoveContentContext moveContentContext) {
        // TODO: 29/11/17 Extract the content model from the folder which are different from duplicate contents and are not in DB
        List<String> addedContentIdentifiers = null;
        List<String> validContentIdsInDestn = moveContentContext.getValidContentIdsInDestination();
        List<String> duplicateContentIdsInDestn = null;
        ExistingContentAction existingContentAction = moveContentContext.getExistingContentAction();

        if (moveContentContext.getDuplicateContents() != null && moveContentContext.getDuplicateContents().size() > 0) {
            duplicateContentIdsInDestn = new ArrayList<>();
            for (MoveContentResponse duplicateContent : moveContentContext.getDuplicateContents()) {
                duplicateContentIdsInDestn.add(duplicateContent.getIdentifier());

                switch (existingContentAction) {
                    case KEEP_HIGHER_VERSION:
                        if (duplicateContent.getStatus() == MoveContentStatus.HIGHER_VERSION_IN_DESTINATION) {
                            ContentHandler.addContentToDb(appContext, duplicateContent.getIdentifier(), moveContentContext.getContentRootFolder(), false);
                        }
                        break;
                    case KEEP_LOWER_VERSION:
                        if (duplicateContent.getStatus() == MoveContentStatus.LOWER_VERSION_IN_DESTINATION) {
                            ContentHandler.addContentToDb(appContext, duplicateContent.getIdentifier(), moveContentContext.getContentRootFolder(), true);
                        }
                        break;
                    case KEEP_DESTINATION:
                    case IGNORE:
                        if (duplicateContent.getStatus() == MoveContentStatus.LOWER_VERSION_IN_DESTINATION) {
                            ContentHandler.addContentToDb(appContext, duplicateContent.getIdentifier(), moveContentContext.getContentRootFolder(), true);
                        } else {
                            ContentHandler.addContentToDb(appContext, duplicateContent.getIdentifier(), moveContentContext.getContentRootFolder(), false);
                        }
                        break;
                }
            }
        }

        if (validContentIdsInDestn != null && validContentIdsInDestn.size() > 0 &&
                duplicateContentIdsInDestn != null && duplicateContentIdsInDestn.size() > 0) {
            addedContentIdentifiers = getNewlyAddedContents(validContentIdsInDestn, duplicateContentIdsInDestn);
        } else if ((duplicateContentIdsInDestn == null || duplicateContentIdsInDestn.size() == 0)
                && (validContentIdsInDestn != null && validContentIdsInDestn.size() > 0)) {
            addedContentIdentifiers = validContentIdsInDestn;
        }

        if (!CollectionUtil.isNullOrEmpty(addedContentIdentifiers)) {
            // Read content in destination folder.
            for (String file : addedContentIdentifiers) {
                ContentHandler.addContentToDb(appContext, file, moveContentContext.getContentRootFolder(), false);
            }
        }

        return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
    }

    /**
     * This method gives all the identifiers of the contents that are newly added manually, by looking at the identifiers from the db
     *
     * @param foldersList
     * @param contentIdentifiers
     * @return
     */
    private List<String> getNewlyAddedContents(List<String> foldersList, List<String> contentIdentifiers) {
        Set<String> dbContents = new HashSet<>(contentIdentifiers);
        Set<String> folderContents = new HashSet<>(foldersList);
        folderContents.removeAll(dbContents);

        return new ArrayList<>(folderContents);
    }

    @Override
    public IChainable<List<MoveContentResponse>, MoveContentContext> then(IChainable<List<MoveContentResponse>, MoveContentContext> link) {
        return link;
    }
}
