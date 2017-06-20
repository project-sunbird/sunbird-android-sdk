package org.ekstep.genieservices.telemetry.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.bean.TelemetryExportResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.db.operations.IDataSource;
import org.ekstep.genieservices.commons.utils.FileUtil;

import java.io.IOException;

/**
 * Created on 6/10/2017.
 *
 * @author anil
 */
public class CopyDatabase implements IChainable<TelemetryExportResponse> {

    private static final String TAG = CopyDatabase.class.getSimpleName();
    private IChainable<TelemetryExportResponse> nextLink;

    private String sourceDB;
    private String destinationDB;
    private IDataSource dataSource;

    public CopyDatabase(String sourceDB, String destinationDB, IDataSource dataSource) {
        this.sourceDB = sourceDB;
        this.destinationDB = destinationDB;
        this.dataSource = dataSource;
    }

    @Override
    public GenieResponse<TelemetryExportResponse> execute(AppContext appContext, ImportContext importContext) {
        try {
            FileUtil.cp(sourceDB, destinationDB);
        } catch (IOException e) {
            e.printStackTrace();
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, e.getMessage(), TAG);
        }

        // Set the external DB.
        importContext.setDbSession(dataSource.getExportDataSource(destinationDB));

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Import profile failed", TAG);
        }
    }

    @Override
    public IChainable<TelemetryExportResponse> then(IChainable<TelemetryExportResponse> link) {
        nextLink = link;
        return link;
    }
}
