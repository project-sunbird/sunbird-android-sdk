package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.ContentFeedback;
import org.ekstep.genieservices.commons.bean.ContentFeedbackFilterCriteria;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.List;

/**
 * This is the interface with the required API to get feedback about the content.
 */
public interface IContentFeedbackService {

    /**
     * This api is used to save the feedback about content.
     *
     * @param contentFeedback - {@link ContentFeedback}
     * @return {@link GenieResponse<Void>} On failing to fetch the data, the response will return status as FALSE with the following error.
     */
    GenieResponse<Void> sendFeedback(ContentFeedback contentFeedback);

    /**
     * This api is used to get the feedback about a content.
     * <p>
     * <p>On successful fetching the data, the response will return status as TRUE and with result type as {@link ContentFeedback}, if content has any feedback then the result will not be null,
     * <p>
     * On failing to fetch the data, the response will return status as FALSE with the following error.
     *
     * @param contentFeedbackFilterCriteria - {@link ContentFeedbackFilterCriteria}
     * @return {@link GenieResponse<List<ContentFeedback>>}
     */
    GenieResponse<List<ContentFeedback>> getFeedback(ContentFeedbackFilterCriteria contentFeedbackFilterCriteria);

}
