package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.commons.GenieResponse;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

/**
 * Created by swayangjit on 7/5/17.
 */

public interface IExport {
    GenieResponse send(IDBSession dbSession);
}
