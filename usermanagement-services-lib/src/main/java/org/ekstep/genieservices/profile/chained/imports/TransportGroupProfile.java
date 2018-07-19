package org.ekstep.genieservices.profile.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileImportResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.importexport.bean.ImportProfileContext;
import org.ekstep.genieservices.profile.db.model.GroupProfileModel;
import org.ekstep.genieservices.profile.db.model.GroupProfilesModel;

/**
 * Created on 7/19/2018.
 *
 * @author swayangjit
 */
public class TransportGroupProfile implements IChainable<ProfileImportResponse, ImportProfileContext> {

    private static final String TAG = TransportGroupProfile.class.getSimpleName();
    private IChainable<ProfileImportResponse, ImportProfileContext> nextLink;

    @Override
    public GenieResponse<ProfileImportResponse> execute(AppContext appContext, ImportProfileContext importContext) {
        IDBSession externalDBSession = appContext.getExternalDBSession(importContext.getSourceDBFilePath());

        // Read from imported DB
        GroupProfilesModel groupProfilesModel = GroupProfilesModel.find(externalDBSession);

        if (groupProfilesModel != null) {
            for (GroupProfileModel groupProfileModel : groupProfilesModel.getGroupProfileModelList()) {
                final GroupProfileModel model = GroupProfileModel.build(appContext.getDBSession(), groupProfileModel.getGid(), groupProfileModel.getUid());
                appContext.getDBSession().executeInTransaction(new IDBTransaction() {
                    @Override
                    public Void perform(IDBSession dbSession) {
                        model.save();
                        return null;
                    }
                });
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
