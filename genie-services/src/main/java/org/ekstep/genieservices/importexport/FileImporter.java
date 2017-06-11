package org.ekstep.genieservices.importexport;

import android.content.Context;

import org.ekstep.genieservices.ITelemetryService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileImportRequest;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.db.operations.IDataSource;
import org.ekstep.genieservices.commons.db.operations.impl.SQLiteDataSource;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.importexport.db.model.MetadataModel;

import java.util.Map;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class FileImporter {

    private static final String TAG = FileImporter.class.getSimpleName();

    private IUserService userService;
    private ITelemetryService telemetryService;
    private IDataSource dataSource;

    public FileImporter(AppContext<Context> appContext, IUserService userService, ITelemetryService telemetryService) {
        this.userService = userService;
        this.telemetryService = telemetryService;
        this.dataSource = new SQLiteDataSource(appContext);
    }

    public GenieResponse<Void> importFile(ProfileImportRequest importRequest) {
        GenieResponse<Void> response;
        if (!FileUtil.doesFileExists(importRequest.getSourceFilePath())) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_FILE, "Import failed, file doesn't exists", TAG);
            return response;
        }

        String ext = FileUtil.getFileExtension(importRequest.getSourceFilePath());
        if (ServiceConstants.FileExtension.PROFILE.equals(ext)) {
            IDBSession dbSession = dataSource.getImportDataSource(importRequest.getSourceFilePath());
            return userService.importProfile(dbSession, getMetadata(dbSession));
        } else if (ServiceConstants.FileExtension.TELEMETRY.equals(ext)) {
            IDBSession dbSession = dataSource.getImportDataSource(importRequest.getSourceFilePath());
            return telemetryService.importTelemetry(dbSession, getMetadata(dbSession));
        } else {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_FILE, "Profile import failed, unsupported file extension", TAG);
            return response;
        }
    }

    // TODO: 6/9/2017 Can be moved to validate metadata
    private Map<String, Object> getMetadata(IDBSession dbSession) {
        MetadataModel metadataModel = MetadataModel.findAll(dbSession);
        Map<String, Object> metadata = null;
        if (metadataModel != null) {
            metadata = metadataModel.getMetadata();
        }
        return metadata;
    }

}
