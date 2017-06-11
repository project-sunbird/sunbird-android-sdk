package org.ekstep.genieservices.importexport;

import android.content.Context;

import org.ekstep.genieservices.ITelemetryService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
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
    private IUserService userService;
    private ITelemetryService telemetryService;
    private IDataSource dataSource;

    public FileExporter(AppContext<Context> appContext, IUserService userService, ITelemetryService telemetryService) {
        this.appContext = appContext;
        this.userService = userService;
        this.telemetryService = telemetryService;
        this.dataSource = new SQLiteDataSource(appContext);
    }

    public GenieResponse<Void> exportFile(List<String> userIds, File destinationFolder) {
        GenieResponse<Void> response;

        if (userIds != null && userIds.size() > 0) {
            // Read the first profile and get the temp location path
            String destinationDBFilePath = getEparFilePath(userIds, destinationFolder);

            IDBContext dbContext = new GSDBContext();
            String sourceDBFilePath = appContext.getContext().getDatabasePath(dbContext.getDBName()).getPath();

            return userService.exportProfile(userIds, destinationFolder, sourceDBFilePath, destinationDBFilePath, dataSource.getImportDataSource(destinationDBFilePath), getMetadata(dbContext));
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

    private Map<String, Object> getMetadata(IDBContext dbContext) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ServiceConstants.VERSION, String.valueOf(dbContext.getDBVersion()));
        metadata.put(ServiceConstants.EXPORT_TYPES, GsonUtil.toJson(Collections.singletonList(ServiceConstants.EXPORT_TYPE_PROFILE)));
        metadata.put(ServiceConstants.DID, appContext.getDeviceInfo().getDeviceID());

        return metadata;
    }
}
