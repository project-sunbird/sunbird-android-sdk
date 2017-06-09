package org.ekstep.genieservices.importexport;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileImportRequest;
import org.ekstep.genieservices.commons.db.operations.IDataSource;
import org.ekstep.genieservices.commons.utils.FileUtil;

import java.util.Map;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class FileImporter {

    private static final String TAG = FileImporter.class.getSimpleName();

    public GenieResponse<Void> importFile(ProfileImportRequest importRequest, IDataSource dataSource) {
        GenieResponse<Void> response;
        if (!FileUtil.doesFileExists(importRequest.getSourceFilePath())) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_FILE, "Profile import failed, file doesn't exists", TAG);
            return response;
        }

        String ext = FileUtil.getFileExtension(importRequest.getSourceFilePath());
        if (!ServiceConstants.FileExtension.PROFILE.equals(ext)) {
            response = GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.INVALID_FILE, "Profile import failed, unsupported file extension", TAG);
            return response;
        } else {
            Map<String, Object> metadata;
            return userService.importProfile(dataSource.getImportDataSource(importRequest.getSourceFilePath()), metadata);
        }
    }
}
