package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.GsonUtil;

/**
 * Created on 13/3/18.
 *
 * @author anil
 */
public class Framework {

    private String framework;

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
