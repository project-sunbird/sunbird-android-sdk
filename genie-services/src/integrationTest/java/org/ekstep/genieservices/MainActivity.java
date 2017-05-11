package org.ekstep.genieservices;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MasterData;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;
import org.ekstep.genieservices.config.ConfigServiceImpl;

/**
 * Created by swayangjit on 11/5/17.
 */

public class MainActivity extends Activity{
    private IConfigService mConfigService=null;
    private boolean idle = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GenieService genieService=GenieService.init(this,getPackageName(),"","org.ekstep");
        mConfigService=genieService.getConfigService();
        getMasterData(MasterDataType.AGE);

    }

    public void getMasterData(MasterDataType type) {
        idle = false;
        GenieResponse<MasterData> masterData=mConfigService.getMasterData(type);

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
}
