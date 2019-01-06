package org.ekstep.genieservices.profile.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Group;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.ProfileExportResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.db.contract.GroupEntry;
import org.ekstep.genieservices.commons.db.contract.GroupProfileEntry;
import org.ekstep.genieservices.commons.db.contract.LearnerAssessmentsEntry;
import org.ekstep.genieservices.commons.db.contract.LearnerSummaryEntry;
import org.ekstep.genieservices.commons.db.contract.MetaEntry;
import org.ekstep.genieservices.commons.db.contract.NoSqlEntry;
import org.ekstep.genieservices.commons.db.contract.ProfileEntry;
import org.ekstep.genieservices.commons.db.contract.UserEntry;
import org.ekstep.genieservices.commons.db.model.CustomReadersModel;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.db.model.NoSqlModelListModel;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.importexport.bean.ExportProfileContext;
import org.ekstep.genieservices.profile.db.model.GroupModel;
import org.ekstep.genieservices.profile.db.model.GroupProfileModel;
import org.ekstep.genieservices.profile.db.model.GroupProfilesModel;
import org.ekstep.genieservices.profile.db.model.UserModel;
import org.ekstep.genieservices.profile.db.model.UserProfileModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created on 6/10/2017.
 *
 * @author anil
 */
public class CleanupExportedFile implements IChainable<ProfileExportResponse, ExportProfileContext> {

    private static final String TAG = CleanupExportedFile.class.getSimpleName();
    private IChainable<ProfileExportResponse, ExportProfileContext> nextLink;

    @Override
    public GenieResponse<ProfileExportResponse> execute(AppContext appContext, ExportProfileContext exportContext) {
        IDBSession destinationDBSession = appContext.getExternalDBSession(exportContext.getDestinationDBFilePath());
        List<String> allTables = getAllTables(destinationDBSession);
        List<String> allTableToExclude = getAllTableToExclude();

        removeTables(destinationDBSession, allTables, allTableToExclude);

        deleteUnwantedProfilesAndUsers(destinationDBSession, exportContext.getUserIds());
        deleteUnwantedProfileSummary(destinationDBSession, exportContext.getUserIds());
        deleteUnwantedGroup(destinationDBSession, exportContext.getGroupIds());
        deleteUnwantedGroupProfileMapping(destinationDBSession, exportContext.getGroupIds());
        keepAllFrameworkAndChannel(destinationDBSession);

        try {
            removeJournalFile(exportContext.getDestinationDBFilePath());
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(TAG, e.getMessage());

            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, e.getMessage(), TAG);
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, exportContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Export profile failed", TAG);
        }
    }

    @Override
    public IChainable<ProfileExportResponse, ExportProfileContext> then(IChainable<ProfileExportResponse, ExportProfileContext> link) {
        nextLink = link;
        return link;
    }

    private List<String> getAllTables(IDBSession dbSession) {
        List<String> tables;

        String allTablesQuery = String.format(Locale.US, "select name from sqlite_master where type='%s'", "table");
        CustomReadersModel customReadersModel = CustomReadersModel.find(dbSession, allTablesQuery);
        if (customReadersModel != null) {
            tables = customReadersModel.getDataList();
        } else {
            tables = new ArrayList<>();
        }

        return tables;
    }

    private List<String> getAllTableToExclude() {
        List<String> tablesToExclude = new ArrayList<>();

        tablesToExclude.add(MetaEntry.TABLE_NAME);
        tablesToExclude.add(UserEntry.TABLE_NAME);
        tablesToExclude.add(ProfileEntry.TABLE_NAME);
        tablesToExclude.add(LearnerAssessmentsEntry.TABLE_NAME);
        tablesToExclude.add(LearnerSummaryEntry.TABLE_NAME);
        tablesToExclude.add(GroupEntry.TABLE_NAME);
        tablesToExclude.add(GroupProfileEntry.TABLE_NAME);
        tablesToExclude.add(NoSqlEntry.TABLE_NAME);

        return tablesToExclude;
    }

    private void removeTables(IDBSession dbSession, List<String> allTables, List<String> allTableToExclude) {
        for (String table : allTables) {
            if (allTableToExclude.contains(table)) {
                continue;
            }
            String dropTableQuery = String.format(Locale.US, "DROP TABLE IF EXISTS %s", table);
            dbSession.execute(dropTableQuery);
        }
    }

    private void deleteUnwantedProfilesAndUsers(IDBSession dbSession, List<String> userIds) {
        List<Profile> profilesToRetain = new ArrayList<>();
        List<UserModel> userToRetain = new ArrayList<>();

        for (String uid : userIds) {
            UserProfileModel userProfileModel = UserProfileModel.find(dbSession, uid);
            if (userProfileModel != null) {
                profilesToRetain.add(userProfileModel.getProfile());
            }

            UserModel userModel = UserModel.findByUserId(dbSession, uid);
            if (userModel != null) {
                userToRetain.add(userModel);
            }
        }

        cleanTable(dbSession, ProfileEntry.TABLE_NAME);
        cleanTable(dbSession, UserEntry.TABLE_NAME);

        for (Profile profile : profilesToRetain) {
            UserProfileModel userProfileModel = UserProfileModel.build(dbSession, profile);
            userProfileModel.save();
        }

        for (UserModel user : userToRetain) {
            UserModel userModel = UserModel.build(dbSession, user.getUid());
            userModel.save();
        }
    }

    private void cleanTable(IDBSession dbSession, String tableName) {
        String deleteProfilesTable = String.format(Locale.US, "Delete from %s", tableName);
        dbSession.execute(deleteProfilesTable);
    }

    private void deleteUnwantedProfileSummary(IDBSession dbSession, final List<String> userIds) {
        dbSession.executeInTransaction(new IDBTransaction() {
            @Override
            public Void perform(IDBSession dbSession) {
                String commaSeparatedUids = "'" + StringUtil.join("','", userIds) + "'";
                String delLearnerAssesmentQuery = "DELETE FROM " + LearnerAssessmentsEntry.TABLE_NAME + " WHERE " + LearnerAssessmentsEntry.COLUMN_NAME_UID + " NOT IN(" + commaSeparatedUids + ")";
                String delLearnerSummaryQuery = "DELETE FROM " + LearnerSummaryEntry.TABLE_NAME + " WHERE " + LearnerAssessmentsEntry.COLUMN_NAME_UID + " NOT IN(" + commaSeparatedUids + ")";

                dbSession.execute(delLearnerAssesmentQuery);
                dbSession.execute(delLearnerSummaryQuery);
                return null;
            }
        });
    }

    private void deleteUnwantedGroup(IDBSession dbSession, List<String> groupIds) {

        if (!CollectionUtil.isNullOrEmpty(groupIds)) {
            List<Group> groupsToRetain = new ArrayList<>();
            for (String gid : groupIds) {
                GroupModel groupModel = GroupModel.findGroupById(dbSession, gid);
                if (groupModel != null) {
                    groupsToRetain.add(groupModel.getGroup());
                }
            }

            cleanTable(dbSession, GroupEntry.TABLE_NAME);

            for (Group group : groupsToRetain) {
                GroupModel groupModel = GroupModel.build(dbSession, group);
                groupModel.save();
            }
        }
    }

    private void deleteUnwantedGroupProfileMapping(IDBSession dbSession, List<String> groupIds) {

        if (!CollectionUtil.isNullOrEmpty(groupIds)) {
            List<GroupProfileModel> groupProfileMappingsToRetain = new ArrayList<>();
            for (String gid : groupIds) {
                GroupProfilesModel groupProfilesModelModel = GroupProfilesModel.findByGid(dbSession, gid);
                if (groupProfilesModelModel != null) {
                    groupProfileMappingsToRetain.addAll(groupProfilesModelModel.getGroupProfileModelList());
                }
            }

            cleanTable(dbSession, GroupProfileEntry.TABLE_NAME);

            for (GroupProfileModel groupProfileModel : groupProfileMappingsToRetain) {
                GroupProfileModel model = GroupProfileModel.build(dbSession, groupProfileModel.getGid(), groupProfileModel.getUid());
                model.save();
            }
        }
    }

    private void keepAllFrameworkAndChannel(IDBSession dbSession) {
        String query = "SELECT *  FROM  no_sql WHERE key LIKE 'channel_details_key-%' OR key LIKE 'framework_details_key-%' OR key LIKE 'form-%' ;";

        NoSqlModelListModel noSqlModelListModel = NoSqlModelListModel.findWithCustomQuery(dbSession, query);

        cleanTable(dbSession, NoSqlEntry.TABLE_NAME);

        if (noSqlModelListModel != null) {
            for (NoSqlModel noSqlModel : noSqlModelListModel.getNoSqlModelList()) {
                NoSqlModel model = NoSqlModel.build(dbSession, noSqlModel.getKey(), noSqlModel.getValue());
                model.save();
            }
        }
    }

    private void removeJournalFile(String destinationDBFilePath) throws Exception {
        File file = new File(destinationDBFilePath + "-journal");
        file.delete();
    }
}
