package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.GameData;

/**
 * Created by swayangjit on 2/5/17.
 */

public class GETransfer extends BaseTelemetry  {

    private final String eid = "GE_TRANSFER";

    public GETransfer(GameData gameData, GETransferEventKnowStructure eks) {
        super(gameData);
        setEks(eks);
    }

    @Override
    public String getEID() {
        return eid;
    }

}

