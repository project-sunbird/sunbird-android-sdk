package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.db.contract.ContentEntry;
import org.ekstep.genieservices.commons.db.contract.PageEntry;
import org.ekstep.genieservices.commons.db.contract.ProfileEntry;
import org.ekstep.genieservices.commons.db.migration.Migration;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.db.model.ContentModel;
import org.ekstep.genieservices.content.db.model.ContentsModel;
import org.ekstep.genieservices.profile.db.model.UserProfileModel;
import org.ekstep.genieservices.profile.db.model.UserProfilesModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Adding pragma column in content table
 * <p>
 * Created on 1/31/2018.
 *
 * @author anil
 */
public class _11_PragmanProfileMigration extends Migration {

    //DON'T CHANGE THESE VALUES
    private static final int MIGRATION_NUMBER = 11;
    private static final int TARGET_DB_VERSION = 16;
    HashMap<String, String> standardMap = new LinkedHashMap<String, String>() {{
        put("0", "KG");
        put("1", "Grade 1");
        put("2", "Grade 2");
        put("3", "Grade 3");
        put("4", "Grade 4");
        put("5", "Grade 5");
        put("6", "Grade 6");
        put("7", "Grade 7");
        put("8", "Grade 8");
        put("9", "Grade 9");
        put("10", "Grade 10");
        put("11", "Grade 11");
        put("12", "Grade 12");
        put("-1", "Others");
    }};

    public _11_PragmanProfileMigration() {
        super(MIGRATION_NUMBER, TARGET_DB_VERSION);
    }

    @Override
    public void apply(AppContext appContext) {
        // Add pragma in content table.
        appContext.getDBSession().execute(ContentEntry.getAlterEntryForPragma());

        //Scan local content column and make entry in pragma column.
        updatePragma(appContext);

        // Add pragma in page table.
        appContext.getDBSession().execute(PageEntry.getAlterEntryForPragma());

        // Add Subject,profile type and grade
        List<String> alterSubjectnType = ProfileEntry.getAlterEntryForProfileSubjectnType();
        for (String alertEntry : alterSubjectnType) {
            appContext.getDBSession().execute(alertEntry);
        }
        updateGrade(appContext);
    }

    private void updatePragma(AppContext appContext) {
        String refCountFilter = String.format(Locale.US, "%s > '0'", ContentEntry.COLUMN_NAME_REF_COUNT);
        String filter = String.format(Locale.US, " where %s", refCountFilter);
        List<ContentModel> contentModelListInDB = null;
        ContentsModel contentsModel = ContentsModel.find(appContext.getDBSession(), filter);
        if (contentsModel != null) {
            contentModelListInDB = contentsModel.getContentModelList();
            for (ContentModel contentModel : contentModelListInDB) {
                Map localDataMap = GsonUtil.fromJson(contentModel.getLocalData(), Map.class);
                String pragma = ContentHandler.readPragma(localDataMap);
                if (pragma != null) {
                    contentModel.setPragma(pragma);
                    contentModel.update();
                }

            }
        }

    }

    private void updateGrade(AppContext appContext) {
        UserProfilesModel userProfilesModel = UserProfilesModel.find(appContext.getDBSession());
        if (userProfilesModel != null) {
            List<Profile> updatedProfileList = new ArrayList<>();
            for (Profile profile : userProfilesModel.getProfileList()) {
                String[] gradeArray = new String[]{standardMap.get(String.valueOf(profile.getStandard()))};
                profile.setGrade(gradeArray);
                updatedProfileList.add(profile);
            }

            for (Profile profile : updatedProfileList) {
                UserProfileModel userProfileModel = UserProfileModel.build(appContext.getDBSession(), profile);
                userProfileModel.update();
            }
        }
    }

}
