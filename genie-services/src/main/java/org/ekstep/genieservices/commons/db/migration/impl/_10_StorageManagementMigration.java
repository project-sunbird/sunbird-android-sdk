package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.contract.ContentEntry;
import org.ekstep.genieservices.commons.db.migration.Migration;
import org.ekstep.genieservices.content.db.model.ContentModel;
import org.ekstep.genieservices.content.db.model.ContentsModel;

import java.io.File;

public class _10_StorageManagementMigration extends Migration {

    //DON'T CHANGE THESE VALUES
    private static final int MIGRATION_NUMBER = 10;
    private static final int TARGET_DB_VERSION = 15;

    public _10_StorageManagementMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {
        // Create size col in content table
        appContext.getDBSession().execute(ContentEntry.getAlterEntryForContentSize());

        updateContentPath(appContext);
    }

    private void updateContentPath(AppContext appContext) {
        ContentsModel contentsModel = ContentsModel.find(appContext.getDBSession(), "");
        if (contentsModel != null) {
            for (ContentModel contentModel : contentsModel.getContentModelList()) {
                String path = contentModel.getPath();

                if (!path.endsWith(contentModel.getIdentifier())) {
                    String contentRootPath = getFirstPartOfThePathName(path);

                    if (contentRootPath != null) {
                        StringBuilder stringBuilder = new StringBuilder(contentRootPath);
                        stringBuilder.append("/");
                        stringBuilder.append(contentModel.getIdentifier());

                        //Rename the folder
                        if (renameOldFolder(path, stringBuilder.toString())) {

                            // TODO: 25/9/17 Check how can the multiple rows can be updated at one shot
                            //set the path
                            contentModel.setPath(stringBuilder.toString());

                            //update the content path
                            contentModel.update();
                        }
                    }
                }
            }
        }
    }

    private boolean renameOldFolder(String oldName, String newName) {
        File oldPath = new File(oldName);
        File newPath = new File(newName);

        return oldPath.renameTo(newPath);
    }

    /**
     * This method gets you the first part of the string that is divided after last index of "/"
     *
     * @param contentFolderName
     * @return
     */
    private String getFirstPartOfThePathName(String contentFolderName) {
        int lastIndexOfDelimiter = contentFolderName.lastIndexOf("/");

        if (lastIndexOfDelimiter > 0 && lastIndexOfDelimiter < contentFolderName.length()) {
            return contentFolderName.substring(0, lastIndexOfDelimiter);
        }

        return null;
    }

}
