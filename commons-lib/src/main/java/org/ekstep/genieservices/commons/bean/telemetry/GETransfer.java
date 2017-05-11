package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.GameData;

/**
 * Created by swayangjit on 2/5/17.
 */

public class GETransfer extends Telemetry {

    private static final String EID = "GE_TRANSFER";

    public GETransfer(GameData gameData, GETransferEventKnowStructure eks) {
        super(gameData, EID);
        setEks(eks);
    }

}

