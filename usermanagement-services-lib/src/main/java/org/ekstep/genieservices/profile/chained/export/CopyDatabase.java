package org.ekstep.genieservices.profile.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileExportResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.db.operations.IDataSource;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.profile.bean.ExportProfileContext;

import java.io.IOException;

/**
 * Created on 6/10/2017.
 *
 * @author anil
 */
public class CopyDatabase implements IChainable<ProfileExportResponse, ExportProfileContext> {

    private static final String TAG = CopyDatabase.class.getSimpleName();
    private IChainable<ProfileExportResponse, ExportProfileContext> nextLink;

    private String sourceDB;
    private String destinationDB;
    private IDataSource dataSource;

    public CopyDatabase(String sourceDB, String destinationDB, IDataSource dataSource) {
        this.sourceDB = sourceDB;
        this.destinationDB = destinationDB;
        this.dataSource = dataSource;
    }

    @Override
    public GenieResponse<ProfileExportResponse> execute(AppContext appContext, ExportProfileContext exportContext) {
        try {
            FileUtil.cp(sourceDB, destinationDB);
        } catch (IOException e) {
            e.printStackTrace();
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, e.getMessage(), TAG);
        }

        // Set the external DB.
        exportContext.setDbSession(dataSource.getExportDataSource(destinationDB));

        if (nextLink != null) {
            return nextLink.execute(appContext, exportContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Import profile failed", TAG);
        }
    }

    @Override
    public IChainable<ProfileExportResponse, ExportProfileContext> then(IChainable<ProfileExportResponse, ExportProfileContext> link) {
        nextLink = link;
        return link;
    }
}
