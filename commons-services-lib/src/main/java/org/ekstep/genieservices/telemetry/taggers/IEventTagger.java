package org.ekstep.genieservices.telemetry.taggers;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.telemetry.model.EventModel;

/**
 * Created by swayangjit on 26/4/17.
 */

public interface IEventTagger {

    void tag(EventModel event, AppContext appContext);
}
