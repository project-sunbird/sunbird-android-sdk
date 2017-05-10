package org.ekstep.genieservicestest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.MasterData;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;
import org.ekstep.genieservices.config.ConfigService;

/**
 * Created by swayangjit on 10/5/17.
 */

public class MainActivity extends AppCompatActivity {

    private ConfigService mConfigService=null;
    private boolean idle = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GenieService genieService=GenieService.init(this,getPackageName(),"","org.ekstep");
        mConfigService=genieService.getConfigService();
        getMasterData(MasterDataType.AGE, new IResponseHandler<MasterData>() {
            @Override
            public void onSuccess(GenieResponse<MasterData> genieResponse) {
                Log.i("onSuccess",""+genieResponse.getResult());
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Log.e("onError",""+genieResponse.getResult());
            }
        });
    }


    public void getMasterData(MasterDataType type, IResponseHandler<MasterData> responseHandler) {
        idle = false;
        mConfigService.getMasterData(type, setIdleAndInvokeHandler(responseHandler));
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
