package org.ekstep.genieservices.telemetry.processors;

import org.ekstep.genieservices.telemetry.model.ProcessedEvent;

import java.io.IOException;

/**
 * Created by swayangjit on 26/4/17.
 */

public interface IProcessEvent {
    ProcessedEvent process(ProcessedEvent processedEvent) throws IOException;
}
