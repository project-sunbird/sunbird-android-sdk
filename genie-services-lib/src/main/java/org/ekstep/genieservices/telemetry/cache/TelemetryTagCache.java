package org.ekstep.genieservices.telemetry.cache;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.cache.IKeyValueStore;
import org.ekstep.genieservices.telemetry.model.TelemetryTags;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by swayangjit on 26/4/17.
 */

public class TelemetryTagCache {
    public static Set<String> activeTags(AppContext appContext) {
        IKeyValueStore preference=appContext.getKeyValueStore();
        Set<String> genieTags = preference.getStringSet(ServiceConstants.Tags.KEY_GENIE_TAGS,new HashSet<String>());

        if (!genieTags.isEmpty()) {
            return genieTags;
        } else {
            TelemetryTags telemetryTags = TelemetryTags.find(appContext);

            preference.putStringSet(ServiceConstants.Tags.KEY_GENIE_TAGS, telemetryTags.activeTagHashes());
            return telemetryTags.activeTagHashes();
        }
    }

    public static void clearCache(AppContext appContext) {
        appContext.getKeyValueStore().remove(ServiceConstants.Tags.KEY_GENIE_TAGS);
    }
}
