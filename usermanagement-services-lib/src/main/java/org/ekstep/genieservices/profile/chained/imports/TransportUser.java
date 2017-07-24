package org.ekstep.genieservices.profile.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileImportResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.importexport.bean.ImportProfileContext;
import org.ekstep.genieservices.profile.db.model.UserModel;
import org.ekstep.genieservices.profile.db.model.UsersModel;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class TransportUser implements IChainable<ProfileImportResponse, ImportProfileContext> {

    private static final String TAG = TransportUser.class.getSimpleName();
    private IChainable<ProfileImportResponse, ImportProfileContext> nextLink;

    @Override
    public GenieResponse<ProfileImportResponse> execute(AppContext appContext, ImportProfileContext importContext) {
        IDBSession externalDBSession = appContext.getExternalDBSession(importContext.getSourceDBFilePath());
        // Read from imported DB
        UsersModel usersModel = UsersModel.findAll(externalDBSession);

        if (usersModel != null) {
            for (UserModel userModel : usersModel.getUserModelList()) {
                UserModel user = UserModel.build(appContext.getDBSession(), userModel.getUid());
                if (UserModel.findByUserId(appContext.getDBSession(), userModel.getUid()) == null) {
                    user.save();
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
    public IChainable<ProfileImportResponse, ImportProfileContext> then(IChainable<ProfileImportResponse, ImportProfileContext> link) {
        nextLink = link;
        return link;
    }
}
