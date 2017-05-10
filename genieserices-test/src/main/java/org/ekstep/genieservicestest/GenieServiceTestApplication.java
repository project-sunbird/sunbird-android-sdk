package org.ekstep.genieservicestest;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by swayangjit on 10/5/17.
 */

public class GenieServiceTestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
