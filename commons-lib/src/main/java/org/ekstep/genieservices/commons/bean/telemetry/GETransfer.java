package org.ekstep.genieservices.commons.bean.telemetry;

/**
 * Created on 2/5/17.
 *
 * @author swayangjit
 */
public class GETransfer extends Telemetry {

    private static final String EID = "GE_TRANSFER";

    public GETransfer(GETransferEventKnowStructure eks) {
        super(EID);
        setEks(eks);
    }

}
