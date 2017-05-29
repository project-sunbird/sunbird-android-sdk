package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.ContentFeedback;
import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * This is the interface with the required API to get feedback about the content.
 */
public interface IContentFeedbackService {

    /**
     * This api is used to get the feedback about a content.
     * <p>
     * <p>Response status always be True, with result type as {@link ContentFeedback}, if content has any feedback then the result will not be null,
     * else it will be null
     *
     * @param uid
     * @param contentIdentifier
     * @return {@link GenieResponse<ContentFeedback>}
     */
    GenieResponse<ContentFeedback> getFeedback(String uid, String contentIdentifier);

}
