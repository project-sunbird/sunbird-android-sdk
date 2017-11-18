package org.ekstep.genieservices.profile.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.ProfileImportResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.importexport.bean.ImportProfileContext;
import org.ekstep.genieservices.profile.db.model.UserProfileModel;
import org.ekstep.genieservices.profile.db.model.UserProfilesModel;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class TransportProfiles implements IChainable<ProfileImportResponse, ImportProfileContext> {

    private static final String TAG = TransportProfiles.class.getSimpleName();
    private IChainable<ProfileImportResponse, ImportProfileContext> nextLink;

    @Override
    public GenieResponse<ProfileImportResponse> execute(AppContext appContext, ImportProfileContext importContext) {
        IDBSession externalDBSession = appContext.getExternalDBSession(importContext.getSourceDBFilePath());
        int imported = 0;
        int failed = 0;

        // Read from imported DB
        UserProfilesModel userProfilesModel = UserProfilesModel.find(externalDBSession);

        if (userProfilesModel != null) {
            for (Profile profile : userProfilesModel.getProfileList()) {
                UserProfileModel userProfileModel = UserProfileModel.find(appContext.getDBSession(), profile.getUid());
                if (userProfileModel == null) {
                    if (profile.getCreatedAt() == null) {
                        profile.setCreatedAt(DateUtil.now());
                    }

                    // TODO: 6/9/2017 Instead of exporting and importing user table read uid from profile and create entry.
//                    final UserModel userModel = UserModel.build(appContext.getDBSession(), profile.getUid());
//                    final GECreateUser geCreateUser = new GECreateUser(gameData, profile.getUid(), appContext.getLocationInfo().getLocation());

                    final UserProfileModel profileModel = UserProfileModel.build(appContext.getDBSession(), profile);
                    appContext.getDBSession().executeInTransaction(new IDBTransaction() {
                        @Override
                        public Void perform(IDBSession dbSession) {
//                            userModel.save();
//                            TelemetryLogger.log(geCreateUser);

                            profileModel.save();
                            return null;
                        }
                    });
                    imported++;
                } else {
                    failed++;
                }
            }
        }

        importContext.setImported(imported);
        importContext.setFailed(failed);

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Import profile failed.", TAG);
        }
    }

    @Override
    public IChainable<ProfileImportResponse, ImportProfileContext> then(IChainable<ProfileImportResponse, ImportProfileContext> link) {
        nextLink = link;
        return link;
    }
}
