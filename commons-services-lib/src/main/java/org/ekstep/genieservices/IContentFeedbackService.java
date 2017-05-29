package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.ContentFeedback;
import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * This is the interface with the required API to get feedback about the content.
 */
public interface IContentFeedbackService {

    /**
     * This api is used to save the feedback about content.
     *
     * @param uid               User id.
     * @param contentIdentifier Content identifier
     * @param rating            Rating provided by user
     * @param comments          Comments provided by user
     * @return On failing to fetch the data, the response will return status as FALSE with the following error.
     */
    GenieResponse<Void> sendFeedback(String uid, String contentIdentifier, float rating, String comments);

    /**
     * This api is used to get the feedback about a content.
     * <p>
     * <p>On successful fetching the data, the response will return status as TRUE and with result type as {@link ContentFeedback}, if content has any feedback then the result will not be null,
     * <p>
     * On failing to fetch the data, the response will return status as FALSE with the following error.
     *
     * @param uid               User id
     * @param contentIdentifier Content identifier
     * @return {@link GenieResponse<ContentFeedback>}
     */
    GenieResponse<ContentFeedback> getFeedback(String uid, String contentIdentifier);

}
