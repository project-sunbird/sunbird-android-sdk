package org.ekstep.genieservices.telemetry.processors;

import org.ekstep.genieservices.telemetry.model.ProcessedEventModel;

/**
 * Created on 26/4/17.
 *
 * @author swayangjit
 */
public interface IProcessEvent {
    ProcessedEventModel process(ProcessedEventModel processedEvent);
}
