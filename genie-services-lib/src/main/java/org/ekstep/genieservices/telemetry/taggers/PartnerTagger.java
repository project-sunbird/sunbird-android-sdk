package org.ekstep.genieservices.telemetry.taggers;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.utils.ArrayUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.telemetry.model.Event;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by swayangjit on 26/4/17.
 */

public class PartnerTagger implements IEventTagger {

    private static final String TAG = "tagger-PartnerTagger";

    @Override
    public void tag(Event event, AppContext appContext) {
        Set<String> values = appContext.getKeyValueStore().getStringSet(ServiceConstants.Partner.KEY_PARTNER_ID, null);
        List<Map<String, Object>> tags = (List<Map<String, Object>>) event.getEventMap().get("tags");
        if (values != null && !values.isEmpty()) {
            Logger.i(appContext, TAG, String.format("TAG %s:%s", ServiceConstants.Partner.KEY_PARTNER_ID, values));
            if (!ArrayUtil.containsMap(tags, ServiceConstants.Partner.KEY_PARTNER_ID))
                event.addTag(ServiceConstants.Partner.KEY_PARTNER_ID, values);
        }
    }
}
