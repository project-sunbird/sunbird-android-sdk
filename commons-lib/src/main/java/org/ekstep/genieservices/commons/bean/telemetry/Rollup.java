package org.ekstep.genieservices.commons.bean.telemetry;

/**
 * Created by swayangjit on 6/12/17.
 * This class contains information about rollups of the object. Only 4 levels are allowed
 */

public class Rollup {

    private String l1;
    private String l2;
    private String l3;
    private String l4;

    public Rollup(String l1, String l2, String l3, String l4) {
        this.l1 = l1;
        this.l2 = l2;
        this.l3 = l3;
        this.l4 = l4;
    }
}
