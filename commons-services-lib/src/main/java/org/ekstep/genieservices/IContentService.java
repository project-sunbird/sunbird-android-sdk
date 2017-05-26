package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchResult;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.List;

/**
 * This is the interface with all the required APIs for performing content related
 * operations.
 */
public interface IContentService {

    /**
     * This api is used to get the content details about a specific content.
     * <p>
     * <p>
     * On successful fetching the content details, the response will return status as TRUE and with {@link Content} in the result.
     * <p>
     * <p>
     * On failing to fetch the content details, the response will return status as FALSE with the following error code
     * <p>NO_DATA_FOUND
     *
     * @param contentIdentifier -  identifier of a content
     * @return
     */
    GenieResponse<Content> getContentDetails(String contentIdentifier);

    /**
     * This api is used to get all the contents.
     * <p>
     * <p>
     * Response status will always be TRUE with {@link List<Content>} set in the result.
     *
     * @param criteria
     * @return
     */
    GenieResponse<List<Content>> getAllLocalContent(ContentCriteria criteria);

    /**
     * This api is used to get the child contents of a particular content, this is used in the case of COLLECTION/TEXTBOOK.
     * <p>
     * <p>
     * On successful fetching the content details, the response will return status as TRUE and with {@link List<Content>} in the result.
     * <p>
     * <p>
     * On failing to fetch the child content details, the response will return status as FALSE with the following error code
     * <p>NO_DATA_FOUND
     *
     * @param contentIdentifier - identifier of a content
     * @param levelAndState     - Below are the int flags to be used
     *                          <p>
     *                          <p>
     *                          0 - Downloaded or spine both
     *                          <p>
     *                          <p>
     *                          1 - All descendant downloaded contents
     *                          <p>
     *                          <p>
     *                          2 - All descendant spine contents
     *                          <p>
     *                          <p>
     * @return {@link List<Content>}
     */
    GenieResponse<List<Content>> getChildContents(String contentIdentifier, int levelAndState);

    /**
     * This api is used to delete a particular content.
     * <p>
     * <p>
     * On successful deleting the content, the response will return status as TRUE.
     * <p>
     * <p>
     * On failing to delete a content, the response will return status as FALSE with the following error code
     * <p>NO_DATA_FOUND
     *
     * @param contentIdentifier - identifier of a content
     * @param level
     * @return
     */
    GenieResponse<Void> deleteContent(String contentIdentifier, int level);

    /**
     * This api is used to search for contents with the search criterion mentioned in {@link ContentSearchCriteria}
     * <p>
     * <p>
     * On successful finding the contents, matching with the search criterion, the response will return status as TRUE and the result set with {@link ContentSearchResult}
     * <p>
     * <p>
     * On failing to find the contents, the response with return status as FALSE with one of the following errors
     * <p>CONNECTION_ERROR
     * <p>SERVER_ERROR
     * <p>NETWORK_ERROR
     *
     * @param contentSearchCriteria
     * @return
     */
    GenieResponse<ContentSearchResult> searchContent(ContentSearchCriteria contentSearchCriteria);

    /**
     * This api is used to get the related contents based on the language passed required
     * <p>
     * <p>
     * On successful finding the contents, matching with the language preferred, the response will return status as TRUE and the result set with {@link ContentSearchResult}
     * <p>
     * <p>
     * On failing to find the contents, the response with return status as FALSE with one of the following errors
     * <p>CONNECTION_ERROR
     * <p>SERVER_ERROR
     * <p>NETWORK_ERROR
     *
     * @param language
     * @return
     */
    GenieResponse<ContentSearchResult> getRecommendedContent(String language);

    /**
     * This api is used to get the related contents as similar to the identifier passed.
     * <p>
     * <p>
     * On successful finding the contents, matching with the language preferred, the response will return status as TRUE and the result set with {@link ContentSearchResult}
     * <p>
     * <p>
     * On failing to find the contents, the response with return status as FALSE with one of the following errors
     * <p>CONNECTION_ERROR
     * <p>SERVER_ERROR
     * <p>NETWORK_ERROR
     *
     * @param contentIdentifier
     * @return
     */
    GenieResponse<ContentSearchResult> getRelatedContent(String contentIdentifier);

    /**
     * This api is used to get all the next {@link List<Content>} based on the hierarchy of {@link List<String>} identifiers passed
     * <p>
     * <p>
     * On successful finding the next list of contents, the response will return status as TRUE and the result will be set with {@link List<Content>}
     *
     * @param contentIdentifiers
     * @return
     */
    GenieResponse<List<Content>> nextContent(List<String> contentIdentifiers);

    /**
     * This api is used to import the content from the specified Ecar file path
     * <p>
     * <p>
     * On successful importing the content, the response will return status as TRUE
     * <p>
     * <p>
     * On failing to import the content, the response will be with return status as FALSE and wih the following error
     * <p>INVALID_FILE
     *
     * @param isChildContent
     * @param ecarFilePath
     * @return
     */
    GenieResponse<Void> importContent(boolean isChildContent, String ecarFilePath);

    /**
     * This api is used to import the group of contents all specified with identifiers in {@link List<String>}
     * <p>
     * todo this doc is yet to be finished as the implementation part is not yet fully done
     *
     * @param isChildContent
     * @param contentIdentifiers
     * @return
     */
    GenieResponse<Void> importContent(boolean isChildContent, List<String> contentIdentifiers);
}
