package org.ekstep.genieservices.profile.chained;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.profile.db.model.UserModel;
import org.ekstep.genieservices.profile.db.model.UsersModel;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class TransportUser implements IChainable {

    private static final String TAG = TransportUser.class.getSimpleName();
    private IChainable nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {
        // Read from imported DB
        UsersModel usersModel = UsersModel.findAll(importContext.getDBSession());

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
    public IChainable then(IChainable link) {
        nextLink = link;
        return link;
    }
}
