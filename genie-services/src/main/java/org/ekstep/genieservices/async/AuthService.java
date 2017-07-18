package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IAuthService;
import org.ekstep.genieservices.IUserService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.ContentAccess;
import org.ekstep.genieservices.commons.bean.ContentAccessFilterCriteria;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportRequest;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.ProfileExportRequest;
import org.ekstep.genieservices.commons.bean.ProfileExportResponse;
import org.ekstep.genieservices.commons.bean.ProfileImportResponse;
import org.ekstep.genieservices.commons.bean.UserSession;
import org.ekstep.genieservices.importexport.FileExporter;
import org.ekstep.genieservices.importexport.FileImporter;

import java.util.List;

/**
 * This class provides all the required APIs to interact with the API gateway
 */
public class AuthService {

    private IAuthService authService;

    public AuthService(GenieService genieService) {
        this.authService = genieService.getAuthService();
    }

    /**
     * This api is used to get the bearer token required for invoking other APIs.
     * <p>
     * <p>
     * On successful generation of the token, the response will return the bearer token as result
     * <p>
     * <p>
     * On failing to fetch the data, the response will return status as FALSE
     *
     * @param responseHandler - {@link IResponseHandler<String>}
     */
    public void getMobileDeviceBearerToken(IResponseHandler<String> responseHandler) {
        new AsyncHandler<String>(responseHandler).execute(new IPerformable<String>() {
            @Override
            public GenieResponse<String> perform() {
                return authService.getMobileDeviceBearerToken();
            }
        });
    }

}
