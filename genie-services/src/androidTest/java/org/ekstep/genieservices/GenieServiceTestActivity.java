package org.ekstep.genieservices;

import android.app.Activity;
import android.os.Bundle;

import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;

/**
 * Created by Sneha on 4/26/2017.
 */

public class GenieServiceTestActivity extends Activity {

    private boolean idle = true;
    private GenieService mGenieService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_genie_services_test);
        mGenieService = GenieService.init(getApplicationContext(), getPackageName(), "", "");
    }

    public void getMasterData(MasterDataType masterDataType, final IResponseHandler responseHandler) {
        idle = false;
        mGenieService.getConfigService().getMasterData(masterDataType);
    }

    public void getOrdinals(final IResponseHandler responseHandler) {
        idle = false;
        mGenieService.getConfigService().getOrdinals();
    }

    public void getResourceBundle(String languageIdentifier, final IResponseHandler responseHandler) {
        idle = false;
        mGenieService.getConfigService().getResourceBundle(languageIdentifier);
    }

    public void createUserProfile(Profile userProfile, final IResponseHandler responseHandler) {
        idle = false;
        mGenieService.getUserProfileService().createUserProfile(userProfile);
    }

    public void deleteUserProfile(String uid, final IResponseHandler responseHandler) {
        idle = false;
        mGenieService.getUserProfileService().deleteUser(uid);
    }

    public void getAnonymousUser(final IResponseHandler responseHandler) {
        idle = false;
        mGenieService.getUserProfileService().getAnonymousUser();
    }

    public void setAnonymousUser(final IResponseHandler responseHandler) {
        idle = false;
        mGenieService.getUserProfileService().setAnonymousUser();
    }

    public void getCurrentUser(final IResponseHandler responseHandler) {
        idle = false;
        mGenieService.getUserProfileService().getCurrentUser();
    }

    public void setCurrentUser(String uid, final IResponseHandler responseHandler) {
        idle = false;
        mGenieService.getUserProfileService().setCurrentUser(uid);
    }

    public void updateUserProfile(Profile profile, IResponseHandler responseHandler) {
        idle = false;
        mGenieService.getUserProfileService().updateUserProfile(profile);
    }

    private IResponseHandler setIdleAndInvokeHandler(final IResponseHandler handler) {
        return new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                handler.onSuccess(genieResponse);
                idle = true;
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                handler.onError(genieResponse);
                idle = true;
            }
        };
    }

    public void setIdle() {
        idle = true;
    }

    public boolean isIdle() {
        return idle;
    }

}
