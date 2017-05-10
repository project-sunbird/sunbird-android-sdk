package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.ContentFeedback;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.List;

/**
 * Created on 5/10/2017.
 *
 * @author anil
 */
public interface IContentFeedbackService {

    GenieResponse<List<ContentFeedback>> getFeedbacksByContentIdentifier(String contentIdentifier);

}
