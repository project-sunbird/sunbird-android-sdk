package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.bean.GameData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 5/29/2017.
 *
 * @author anil
 */
public class GEFeedback extends Telemetry {

    private static final String EID = "GE_FEEDBACK";

    public GEFeedback(GameData gameData, String feedbackType, String contentId, float rating, String comments, String contextType, String stageId) {
        super(gameData, EID);
        setEks(createEKS(feedbackType, contentId, rating, comments, contextType, stageId));
    }

    private Map<String, Object> createEKS(String feedbackType, String contentId, float rating, String comments, String contextType, String stageId) {
        Map<String, Object> contextMap = new HashMap<>();
        contextMap.put("type", contextType);
        contextMap.put("id", contentId);
        contextMap.put("stageid", stageId);

        HashMap<String, Object> map = new HashMap<>();
        map.put("type", feedbackType);
        map.put("rating", rating);
        map.put("comments", comments);
        map.put("context", contextMap);

        return map;
    }
}
