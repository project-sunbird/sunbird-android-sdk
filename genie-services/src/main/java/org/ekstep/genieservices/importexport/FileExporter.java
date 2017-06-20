package org.ekstep.genieservices.importexport;

import android.content.Context;

import org.ekstep.genieservices.ITelemetryService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.ProfileExportRequest;
import org.ekstep.genieservices.commons.bean.ProfileExportResponse;
import org.ekstep.genieservices.commons.bean.TelemetryExportRequest;
import org.ekstep.genieservices.commons.db.GSDBContext;
import org.ekstep.genieservices.commons.db.IDBContext;
import org.ekstep.genieservices.commons.db.operations.IDataSource;
import org.ekstep.genieservices.commons.db.operations.impl.SQLiteDataSource;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.profile.db.model.UserProfileModel;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class FileExporter {

    private static final String TAG = FileExporter.class.getSimpleName();

    private AppContext<Context> appContext;
    private IDataSource dataSource;

    public FileExporter(AppContext<Context> appContext) {
        this.appContext = appContext;
        this.dataSource = new SQLiteDataSource(appContext);
    }

    public GenieResponse<Void> exportTelemetry(TelemetryExportRequest exportRequest, ITelemetryService telemetryService) {
        IDBContext dbContext = new GSDBContext();
        String sourceDBFilePath = appContext.getContext().getDatabasePath(dbContext.getDBName()).getPath();
        return telemetryService.exportTelemetry(new File(exportRequest.getDestinationFolder()), sourceDBFilePath,
                dataSource, getMetadata(dbContext, ServiceConstants.EXPORT_TYPE_TELEMETRY));
    }

    public GenieResponse<ProfileExportResponse> exportProfile(ProfileExportRequest exportRequest, IUserService userService) {
        if (exportRequest.getUserIds() != null && exportRequest.getUserIds().size() > 0) {
            File destinationFolder = new File(exportRequest.getDestinationFolder());

            // Read the first profile and get the temp location path
            String destinationDBFilePath = getEparFilePath(exportRequest.getUserIds(), destinationFolder);

            IDBContext dbContext = new GSDBContext();
            String sourceDBFilePath = appContext.getContext().getDatabasePath(dbContext.getDBName()).getPath();

            return userService.exportProfile(exportRequest.getUserIds(), destinationFolder, sourceDBFilePath,
                    destinationDBFilePath, dataSource, getMetadata(dbContext, ServiceConstants.EXPORT_TYPE_PROFILE));
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "There are no profile to export.", TAG);
        }
    }

    private String getEparFilePath(List<String> userIds, File destinationFolder) {
        String fileName = "profile." + ServiceConstants.FileExtension.PROFILE;
        UserProfileModel userProfileModel = UserProfileModel.find(appContext.getDBSession(), userIds.get(0));
        if (userProfileModel != null) {
            Profile firstProfile = userProfileModel.getProfile();
            String name = firstProfile.getHandle();
            String appendName = "";
            if (userIds.size() > 1) {
                appendName = String.format(Locale.US, "+%s", (userIds.size() - 1));
            }
            fileName = String.format(Locale.US, "%s%s." + ServiceConstants.FileExtension.PROFILE, name, appendName);
        }

        File eparFile = FileUtil.getTempLocation(destinationFolder, fileName);
        if (eparFile.exists()) {
            try {
                eparFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return eparFile.getAbsolutePath();
    }

    private Map<String, Object> getMetadata(IDBContext dbContext, String exportType) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ServiceConstants.VERSION, String.valueOf(dbContext.getDBVersion()));
        metadata.put(ServiceConstants.EXPORT_TYPES, GsonUtil.toJson(Collections.singletonList(exportType)));
        metadata.put(ServiceConstants.DID, appContext.getDeviceInfo().getDeviceID());

        return metadata;
    }
}
