package org.ekstep.genieservices.commons.db.migration;

import org.ekstep.genieservices.commons.AppContext;

/**
 * Created on 7/21/2016.
 *
 * @author anil
 */
public interface BeforeMigrations {

    void onCreate(AppContext appContext);

    void onUpgrade(AppContext appContext, int oldVersion, int newVersion);
}
