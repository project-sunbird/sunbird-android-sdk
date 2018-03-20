package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.GsonUtil;

/**
 * Created on 13/3/18.
 *
 * @author anil
 */
public class Framework {

    private String framework;

    public Framework(String framework) {
        this.framework = framework;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public String getFramework() {
        return framework;
    }

}
