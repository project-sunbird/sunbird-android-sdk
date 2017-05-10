package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.telemetry.SyncConfiguration;

import java.util.Map;

/**
 * Created on 10/5/17.
 *
 * @author swayangjit
 */
public interface ISyncService {

    GenieResponse<Map> sync();

    GenieResponse<SyncConfiguration> getConfiguration();

    GenieResponse<Void> setConfiguration(SyncConfiguration configuration);

    GenieResponse<String> getLastSyncTime();

    GenieResponse<Boolean> shouldShowSyncPrompt();
}
