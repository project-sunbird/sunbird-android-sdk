package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.commons.bean.HierarchyInfo;
import org.ekstep.genieservices.commons.bean.telemetry.Rollup;
import org.ekstep.genieservices.commons.utils.CollectionUtil;

import java.util.List;

/**
 * Created on 7/30/2018.
 *
 * @author anil
 */
public class TelemetryHandler {

    public static Rollup getRollup(String identifier, List<HierarchyInfo> hierarchyInfo) {
        String l1 = null, l2 = null, l3 = null, l4 = null;

        if (CollectionUtil.isNullOrEmpty(hierarchyInfo)) {
            l1 = identifier;
        } else {
            for (int i = 0; i < hierarchyInfo.size(); i++) {
                if (i == 0) {
                    l1 = hierarchyInfo.get(i).getIdentifier();
                } else if (i == 1) {
                    l2 = hierarchyInfo.get(i).getIdentifier();
                } else if (i == 2) {
                    l3 = hierarchyInfo.get(i).getIdentifier();
                } else if (i == 3) {
                    l4 = hierarchyInfo.get(i).getIdentifier();
                } else {
                    break;
                }
            }
        }

        Rollup rollup = new Rollup(l1, l2, l3, l4);
        return rollup;
    }
}
