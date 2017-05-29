package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.ContentAccessEntry;
import org.ekstep.genieservices.commons.db.contract.ContentEntry;
import org.ekstep.genieservices.commons.db.contract.NotificationEntry;
import org.ekstep.genieservices.commons.db.contract.PageEntry;
import org.ekstep.genieservices.commons.db.contract.ProfileEntry;
import org.ekstep.genieservices.commons.db.migration.Migration;
import org.ekstep.genieservices.commons.db.contract.MasterDataEntry;

import java.util.List;

public class _07_UIBrandingMigration extends Migration {
    //Don't change these values
    private static final int MIGRATION_NUMBER = 7;
    private static final int TARGET_DB_VERSION = 12;

    public _07_UIBrandingMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {

        // Add medium and board in  profile table .
        List<String> alterEntryForMediumnBoard = ProfileEntry.getAlterEntryForMediumnBoard();
        for (String alertEntry : alterEntryForMediumnBoard) {
            appContext.getDBSession().execute(alertEntry);
        }

        // Create the content_access table.
        appContext.getDBSession().execute(ContentAccessEntry.getCreateEntry());

        // Create notifications table.
        appContext.getDBSession().execute(NotificationEntry.getCreateEntry());

        // Add age,standard,medium,board and expiry_time in pages  table.
        List<String> alterEntryPageTable = PageEntry.getAlterEntryForProfileAttributes();
        for (String alertEntry : alterEntryPageTable) {
            appContext.getDBSession().execute(alertEntry);
        }

        // Create terms table.
        appContext.getDBSession().execute(MasterDataEntry.getCreateEntry());

        // Create content_state col in content table
        appContext.getDBSession().execute(ContentEntry.getAlterEntryForContentState());

        // Create content_type col in content table
        appContext.getDBSession().execute(ContentEntry.getAlterEntryForContentType());
        migrateContentTypeCol(appContext);

        // Update the profile creation time after migrating the content_type in content table
        appContext.getDBSession().execute(ProfileEntry.getUpdateCreatedAtEntry());
    }

    private void migrateContentTypeCol(AppContext appContext) {
        // Get the list of all content and check if localData or serverData is available.
        // TODO: 4/19/2017 Anil - Uncomment after mode
//        List<Content> contents = getLocalContents(appContext);
//
//        updateContentType(appContext, contents);
    }

//    private void updateContentType(AppContext appContext, List<Content> contents) {
//        for (Content content : contents) {
//            Updater contentUpdater = new Updater(content);
//
//            contentUpdater.perform(db);
//        }
//    }

//    private List<Content> getLocalContents(AppContext appContext) {
//        Contents contents = new Contents("");
//
//        Reader contentReader = new Reader(contents);
//        contentReader.perform(db);
//
//        List<Map<String, Object>> contentList = contents.asMap();
//
//        List<Content> updatedContentList = new ArrayList<>();
//
//        for (Map<String, Object> content : contentList) {
//            String contentType = null;
//            LinkedTreeMap<String, Object> contentData = null;
//
//            if (content.get("localData") != null) {
//                contentData = (LinkedTreeMap<String, Object>) content.get("localData");
//
//                contentType = (String) contentData.get(KEY_CONTENT_TYPE);
//            } else if (content.get("serverData") != null) {
//                contentData = (LinkedTreeMap<String, Object>) content.get("serverData");
//
//                contentType = (String) contentData.get(KEY_CONTENT_TYPE);
//            }
//
//            if (contentType != null) {
//                Content existingContent = new Content(valueOf(content.get("identifier")));
//                Reader existingContentReader = new Reader(existingContent);
//                existingContentReader.perform(db);
//
//                Content updatedContent = new Content(contentData, null, true);
//                updatedContent.setVisibility(existingContent.getVisibility());
//                updatedContent.addOrUpdateRefCount(existingContent.getRefCount());
//                updatedContent.addOrUpdateContentState(existingContent.getContentState());
//
//                updatedContentList.add(updatedContent);
//            }
//        }
//
//        return updatedContentList;
//    }

}
