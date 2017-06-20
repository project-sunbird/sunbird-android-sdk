package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.ContentAccessEntry;
import org.ekstep.genieservices.commons.db.contract.ContentEntry;
import org.ekstep.genieservices.commons.db.contract.MasterDataEntry;
import org.ekstep.genieservices.commons.db.contract.NotificationEntry;
import org.ekstep.genieservices.commons.db.contract.PageEntry;
import org.ekstep.genieservices.commons.db.contract.ProfileEntry;
import org.ekstep.genieservices.commons.db.migration.Migration;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.db.model.ContentModel;
import org.ekstep.genieservices.content.db.model.ContentsModel;

import java.util.List;
import java.util.Map;

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
        ContentsModel contentsModel = ContentsModel.find(appContext.getDBSession(), "");

        if (contentsModel != null) {
            for (ContentModel contentModel : contentsModel.getContentModelList()) {
                String contentType = null;
                if (!StringUtil.isNullOrEmpty(contentModel.getLocalData())) {
                    contentType = ContentHandler.readContentType(GsonUtil.fromJson(contentModel.getLocalData(), Map.class));
                } else if (!StringUtil.isNullOrEmpty(contentModel.getServerData())) {
                    contentType = ContentHandler.readContentType(GsonUtil.fromJson(contentModel.getServerData(), Map.class));
                }

                if (!StringUtil.isNullOrEmpty(contentType)) {
                    contentModel.setContentType(contentType);
                    contentModel.update();
                }
            }
        }
    }
}
