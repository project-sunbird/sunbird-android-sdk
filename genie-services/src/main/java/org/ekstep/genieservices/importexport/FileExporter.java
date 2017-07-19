package org.ekstep.genieservices.importexport;

import android.content.Context;

import org.ekstep.genieservices.ITelemetryService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileExportRequest;
import org.ekstep.genieservices.commons.bean.ProfileExportResponse;
import org.ekstep.genieservices.commons.bean.TelemetryExportRequest;
import org.ekstep.genieservices.commons.bean.TelemetryExportResponse;
import org.ekstep.genieservices.commons.db.GSDBContext;
import org.ekstep.genieservices.commons.db.IDBContext;
import org.ekstep.genieservices.commons.db.operations.IDataSource;
import org.ekstep.genieservices.commons.db.operations.impl.SQLiteDataSource;
import org.ekstep.genieservices.importexport.bean.ExportProfileContext;
import org.ekstep.genieservices.importexport.bean.ExportTelemetryContext;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class FileExporter {

    private static final String TAG = FileExporter.class.getSimpleName();

    private IDataSource dataSource;
    private IDBContext dbContext;
    private String sourceDBFilePath;

    public FileExporter(AppContext<Context> appContext) {
        dataSource = new SQLiteDataSource(appContext);
        dbContext = new GSDBContext();
        sourceDBFilePath = appContext.getContext().getDatabasePath(dbContext.getDBName()).getPath();
    }

    public GenieResponse<TelemetryExportResponse> exportTelemetry(TelemetryExportRequest telemetryExportRequest, ITelemetryService telemetryService) {
        ExportTelemetryContext exportTelemetryContext = new ExportTelemetryContext(telemetryExportRequest.getDestinationFolder(), dataSource, sourceDBFilePath, dbContext.getDBVersion());
        return telemetryService.exportTelemetry(exportTelemetryContext);
    }

    public GenieResponse<ProfileExportResponse> exportProfile(ProfileExportRequest profileExportRequest, IUserService userService) {
        if (profileExportRequest.getUserIds() != null && profileExportRequest.getUserIds().size() > 0) {
            ExportProfileContext exportContext = new ExportProfileContext(profileExportRequest.getUserIds(), profileExportRequest.getDestinationFolder(), dataSource, sourceDBFilePath, dbContext.getDBVersion());
            return userService.exportProfile(exportContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "There are no profile to export.", TAG);
        }
    }

}
