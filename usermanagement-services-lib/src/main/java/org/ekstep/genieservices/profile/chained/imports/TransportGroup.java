package org.ekstep.genieservices.profile.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Group;
import org.ekstep.genieservices.commons.bean.ProfileImportResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.importexport.bean.ImportProfileContext;
import org.ekstep.genieservices.profile.db.model.GroupModel;
import org.ekstep.genieservices.profile.db.model.GroupsModel;

/**
 * Created on 7/19/2018.
 *
 * @author swayangjit
 */
public class TransportGroup implements IChainable<ProfileImportResponse, ImportProfileContext> {

    private static final String TAG = TransportGroup.class.getSimpleName();
    private IChainable<ProfileImportResponse, ImportProfileContext> nextLink;

    @Override
    public GenieResponse<ProfileImportResponse> execute(AppContext appContext, ImportProfileContext importContext) {
        IDBSession externalDBSession = appContext.getExternalDBSession(importContext.getSourceDBFilePath());
        int imported = importContext != null ? importContext.getImported() : 0;
        int failed = importContext != null ? importContext.getFailed() : 0;

        // Read from imported DB
        GroupsModel groupsModel = GroupsModel.find(externalDBSession);

        if (groupsModel != null) {
            for (Group group : groupsModel.getGroupList()) {
                GroupModel groupModel = GroupModel.findGroupById(appContext.getDBSession(), group.getGid());
                if (groupModel == null) {
                    if (group.getCreatedAt() == null) {
                        group.setCreatedAt(DateUtil.getEpochTime());
                    }

                    final GroupModel model = GroupModel.build(appContext.getDBSession(), group);
                    appContext.getDBSession().executeInTransaction(new IDBTransaction() {
                        @Override
                        public Void perform(IDBSession dbSession) {
                            model.save();
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
