package org.ekstep.genieservices.telemetry.taggers;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.telemetry.model.Event;

/**
 * Created by swayangjit on 26/4/17.
 */

public interface IEventTagger {

    void tag(Event event, AppContext appContext);
}
