package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * Created by swayangjit on 10/5/17.
 */

public interface ISyncService {

    GenieResponse<Void> sync();

    GenieResponse<SyncConfiguration> getConfiguration();

    GenieResponse<Void> setConfiguration(SyncConfiguration configuration);

    GenieResponse<String> getLastSyncTime();

    GenieResponse<Boolean> shouldShowSyncPrompt();
}
