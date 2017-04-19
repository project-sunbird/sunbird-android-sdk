package org.ekstep.genieservices.commons.db.migration;

import org.ekstep.genieservices.commons.AppContext;

/**
 * Created on 4/19/2017.
 *
 * @author anil
 */
public interface IMigrate {

    void apply(AppContext appContext);

    boolean shouldBeApplied(int oldVersion, int newVersion);

    void revert();

}
