package org.ekstep.genieservices.profile.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileImportResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.db.model.NoSqlModel;
import org.ekstep.genieservices.commons.db.model.NoSqlModelListModel;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IDBTransaction;
import org.ekstep.genieservices.importexport.bean.ImportProfileContext;

/**
 * Created on 7/19/2018.
 *
 * @author swayangjit
 */
public class TransportFrameworknChannel implements IChainable<ProfileImportResponse, ImportProfileContext> {

    private static final String TAG = TransportFrameworknChannel.class.getSimpleName();
    private IChainable<ProfileImportResponse, ImportProfileContext> nextLink;

    @Override
    public GenieResponse<ProfileImportResponse> execute(AppContext appContext, ImportProfileContext importContext) {
        IDBSession externalDBSession = appContext.getExternalDBSession(importContext.getSourceDBFilePath());

        // Read from imported DB
        NoSqlModelListModel noSqlModelListModel = NoSqlModelListModel.find(externalDBSession);

        if (noSqlModelListModel != null) {
            for (NoSqlModel noSqlModel : noSqlModelListModel.getNoSqlModelList()) {
                NoSqlModel model = NoSqlModel.findByKey(appContext.getDBSession(), noSqlModel.getKey());
                if (model == null) {
                    final NoSqlModel nsmodel = NoSqlModel.build(appContext.getDBSession(), noSqlModel.getKey(), noSqlModel.getValue());
                    appContext.getDBSession().executeInTransaction(new IDBTransaction() {
                        @Override
                        public Void perform(IDBSession dbSession) {
                            nsmodel.save();
                            return null;
                        }
                    });
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
