package org.ekstep.genieservices.commons.bean;

import java.io.Serializable;

/**
 * "spine": {
 * "ecarUrl": "https: //ekstep-public-dev.s3-ap-south-1.amazonaws.com/ecar_files/org.ekstep.mar06.story.test02/marigold_1488808370449_org.ekstep.mar06.story.test02_4.0_spine.ecar",
 * "size": 2767.0
 * }
 * <p>
 * Created on 3/7/2017.
 *
 * @author anil
 */

public class Spine implements Serializable {

    private String ecarUrl;
    private String size;

    public String getEcarUrl() {
        return ecarUrl;
    }

    public String getSize() {
        return size;
    }

}
