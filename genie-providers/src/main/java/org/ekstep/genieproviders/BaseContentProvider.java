package org.ekstep.genieproviders;

import android.content.ContentProvider;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.commons.utils.Logger;

/**
 * Created on 22/5/17.
 * shriharsh
 */

public abstract class BaseContentProvider extends ContentProvider {

    public GenieService getService() {
        GenieService genieService = GenieService.getService();

        if (genieService == null) {
            genieService = GenieService.init(getContext(), getPackageName());
            Logger.i("BaseContentProvider", "GenieService is null!");
        }

        return genieService;
    }

    public abstract String getPackageName();

}
