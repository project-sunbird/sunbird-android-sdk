package org.ekstep.genieservices.telemetry.taggers;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.telemetry.model.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by swayangjit on 26/4/17.
 */

public class TelemetryTagger implements IEventTagger {

    private static final String TAG = TelemetryTagger.class.getName();
    private Set<String> tags;

    public TelemetryTagger(Set<String> tags) {
        this.tags = tags;
    }

    @Override
    public void tag(Event event, AppContext appContext) {
        List<String> tagList = new ArrayList<>();
        for (String tag : tags) {
            tagList.add(tag);
        }
        event.addTag(TAG, tagList);
    }
}
