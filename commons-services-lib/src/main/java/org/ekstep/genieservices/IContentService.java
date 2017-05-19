package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchResult;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.List;

/**
 * Created on 5/10/2017.
 *
 * @author anil
 */
public interface IContentService {

    GenieResponse<Content> getContentDetails(String contentIdentifier);

    GenieResponse<List<Content>> getAllLocalContent(ContentCriteria criteria);

    GenieResponse<List<Content>> getChildContents(String contentIdentifier, int levelAndState);

    GenieResponse<Void> deleteContent(String contentIdentifier, int level);

    GenieResponse<ContentSearchResult> searchContent(ContentSearchCriteria contentSearchCriteria);

    GenieResponse<ContentSearchResult> getRecommendedContent(String language);

    GenieResponse<ContentSearchResult> getRelatedContent(String contentIdentifier);

    GenieResponse<Void> importContent(boolean isChildContent, String ecarFilePath);
}
