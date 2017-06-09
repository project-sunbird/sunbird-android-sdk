package org.ekstep.genieservices.profile.chained;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GameData;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.telemetry.GECreateProfile;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.profile.db.model.UserProfileModel;
import org.ekstep.genieservices.profile.db.model.UserProfilesModel;
import org.ekstep.genieservices.telemetry.TelemetryLogger;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class TransportProfiles implements IChainable {

    private static final String TAG = TransportProfiles.class.getSimpleName();
    private IChainable nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {
        // TODO: 6/9/2017
        int imported = 0;
        int failed = 0;

        UserProfilesModel userProfilesModel = UserProfilesModel.find(importContext.getDBSession());

        if (userProfilesModel != null) {
            GameData gameData = new GameData(appContext.getParams().getGid(), appContext.getParams().getVersionName());

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
                    final GECreateProfile geCreateProfile = new GECreateProfile(gameData, profile, appContext.getLocationInfo().getLocation());
                    appContext.getDBSession().executeInTransaction(new IDBTransaction() {
                        @Override
                        public Void perform(IDBSession dbSession) {
//                            userModel.save();
//                            TelemetryLogger.log(geCreateUser);

                            profileModel.save();
                            TelemetryLogger.log(geCreateProfile);
                            return null;
                        }
                    });
                    imported++;
                } else {
                    failed++;
                }
            }
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Import profile failed.", TAG);
        }
    }

    @Override
    public IChainable then(IChainable link) {
        nextLink = link;
        return link;
    }
}
