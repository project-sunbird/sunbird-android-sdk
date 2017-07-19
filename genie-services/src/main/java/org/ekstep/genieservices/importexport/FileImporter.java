package org.ekstep.genieservices.importexport;

import android.content.Context;

import org.ekstep.genieservices.ITelemetryService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileImportRequest;
import org.ekstep.genieservices.commons.bean.ProfileImportResponse;
import org.ekstep.genieservices.commons.bean.TelemetryImportRequest;
import org.ekstep.genieservices.commons.db.operations.IDataSource;
import org.ekstep.genieservices.commons.db.operations.impl.SQLiteDataSource;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.importexport.bean.ImportProfileContext;
import org.ekstep.genieservices.importexport.bean.ImportTelemetryContext;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class FileImporter {

    private static final String TAG = FileImporter.class.getSimpleName();

    private IDataSource dataSource;

    public FileImporter(AppContext<Context> appContext) {
        this.dataSource = new SQLiteDataSource(appContext);
    }

    public GenieResponse<Void> importTelemetry(TelemetryImportRequest telemetryImportRequest, ITelemetryService telemetryService) {
        GenieResponse<Void> response;
        if (!FileUtil.doesFileExists(telemetryImportRequest.getSourceFilePath())) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_FILE, "Telemetry event import failed, file doesn't exists", TAG);
            return response;
        }

        String ext = FileUtil.getFileExtension(telemetryImportRequest.getSourceFilePath());
        if (ServiceConstants.FileExtension.TELEMETRY.equals(ext)) {
            ImportTelemetryContext importTelemetryContext = new ImportTelemetryContext(dataSource, telemetryImportRequest.getSourceFilePath());
            return telemetryService.importTelemetry(importTelemetryContext);
        } else {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_FILE, "Telemetry event import failed, unsupported file extension", TAG);
            return response;
        }
    }

    public GenieResponse<ProfileImportResponse> importProfile(ProfileImportRequest profileImportRequest, IUserService userService) {
        GenieResponse<ProfileImportResponse> response;
        if (!FileUtil.doesFileExists(profileImportRequest.getSourceFilePath())) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_FILE, "Profile import failed, file doesn't exists", TAG);
            return response;
        }

        String ext = FileUtil.getFileExtension(profileImportRequest.getSourceFilePath());
        if (ServiceConstants.FileExtension.PROFILE.equals(ext)) {
            ImportProfileContext importContext = new ImportProfileContext(dataSource, profileImportRequest.getSourceFilePath());
            return userService.importProfile(importContext);
        } else {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_FILE, "Profile import failed, unsupported file extension", TAG);
            return response;
        }
    }

}
