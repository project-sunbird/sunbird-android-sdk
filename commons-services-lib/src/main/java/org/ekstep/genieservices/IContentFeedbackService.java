package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.ContentFeedback;
import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * Created on 5/10/2017.
 *
 * @author anil
 */
public interface IContentFeedbackService {

    GenieResponse<ContentFeedback> getFeedback(String uid, String contentIdentifier);

}
