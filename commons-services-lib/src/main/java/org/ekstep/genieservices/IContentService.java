package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.List;

/**
 * Created on 5/10/2017.
 *
 * @author anil
 */
public interface IContentService {

    GenieResponse<Content> getContentDetails(String contentIdentifier);

    GenieResponse<List<Content>> getAllLocalContent();

    GenieResponse<List<Content>> getChildContents(String contentIdentifier, int levelAndState);

    GenieResponse<Void> deleteContent(String contentIdentifier, int level);
}
