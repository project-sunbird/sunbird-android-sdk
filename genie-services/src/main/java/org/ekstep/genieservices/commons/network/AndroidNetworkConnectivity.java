package org.ekstep.genieservices.commons.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.ekstep.genieservices.commons.AndroidLogger;
import org.ekstep.genieservices.commons.AppContext;

/**
 * Created on 4/19/2017.
 *
 * @author anil
 */
public class AndroidNetworkConnectivity implements IConnectionInfo {

    private AppContext<Context, AndroidLogger> appContext;

    public AndroidNetworkConnectivity(AppContext<Context, AndroidLogger> appContext) {
        this.appContext = appContext;
    }

    @Override
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) appContext.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public boolean isConnectedOverWifi() {
        ConnectivityManager cm = (ConnectivityManager) appContext.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] allNetworkInfo = cm.getAllNetworkInfo();

        if (allNetworkInfo == null || allNetworkInfo.length == 0) {
            return false;
        }

        for (NetworkInfo networkInfo : allNetworkInfo) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return networkInfo.isConnected();
            }
        }

        return false;
    }

}
