package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IContentFeedbackService;
import org.ekstep.genieservices.IContentService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentCriteria;
import org.ekstep.genieservices.commons.bean.ContentDeleteRequest;
import org.ekstep.genieservices.commons.bean.ContentDetailsRequest;
import org.ekstep.genieservices.commons.bean.ContentFeedback;
import org.ekstep.genieservices.commons.bean.ContentFeedbackCriteria;
import org.ekstep.genieservices.commons.bean.ContentImportRequest;
import org.ekstep.genieservices.commons.bean.ContentListingCriteria;
import org.ekstep.genieservices.commons.bean.ContentListingResult;
import org.ekstep.genieservices.commons.bean.ContentSearchCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchResult;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.RecommendedContentRequest;
import org.ekstep.genieservices.commons.bean.RecommendedContentResult;
import org.ekstep.genieservices.commons.bean.RelatedContentRequest;
import org.ekstep.genieservices.commons.bean.RelatedContentResult;

import java.util.List;

/**
 * Created on 30/5/17.
 *
 * @author swayangjit
 */
public class ContentService {

    private IContentService contentService;
    private IContentFeedbackService contentFeedbackService;

    public ContentService(GenieService genieService) {
        this.contentService = genieService.getContentService();
        this.contentFeedbackService = genieService.getContentFeedbackService();
    }

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
     * @param contentDetailsRequest
     * @return
     */
    public void getContentDetails(final ContentDetailsRequest contentDetailsRequest, IResponseHandler<Content> responseHandler) {
        new AsyncHandler<Content>(responseHandler).execute(new IPerformable<Content>() {
            @Override
            public GenieResponse<Content> perform() {
                return contentService.getContentDetails(contentDetailsRequest);
            }
        });
    }

    /**
     * This api is used to get all the contents.
     * <p>
     * <p>
     * Response status will always be TRUE with {@link List<Content>} set in the result.
     *
     * @param criteria
     * @return
     */
    public void getAllLocalContent(final ContentCriteria criteria, IResponseHandler<List<Content>> responseHandler) {
        new AsyncHandler<List<Content>>(responseHandler).execute(new IPerformable<List<Content>>() {
            @Override
            public GenieResponse<List<Content>> perform() {
                return contentService.getAllLocalContent(criteria);
            }
        });
    }

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
    public void getChildContents(final String contentIdentifier, final int levelAndState, IResponseHandler<Content> responseHandler) {
        new AsyncHandler<Content>(responseHandler).execute(new IPerformable<Content>() {
            @Override
            public GenieResponse<Content> perform() {
                return contentService.getChildContents(contentIdentifier, levelAndState);
            }
        });
    }

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
     * @param contentDeleteRequest
     * @return
     */
    public void deleteContent(final ContentDeleteRequest contentDeleteRequest, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return contentService.deleteContent(contentDeleteRequest);
            }
        });
    }

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
    public void searchContent(final ContentSearchCriteria contentSearchCriteria, IResponseHandler<ContentSearchResult> responseHandler) {
        new AsyncHandler<ContentSearchResult>(responseHandler).execute(new IPerformable<ContentSearchResult>() {
            @Override
            public GenieResponse<ContentSearchResult> perform() {
                return contentService.searchContent(contentSearchCriteria);
            }
        });
    }

    /**
     * This api is used to get the related contents based on the language
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
     * @param recommendedContentRequest
     * @return
     */
    public void getRecommendedContent(final RecommendedContentRequest recommendedContentRequest, IResponseHandler<RecommendedContentResult> responseHandler) {
        new AsyncHandler<RecommendedContentResult>(responseHandler).execute(new IPerformable<RecommendedContentResult>() {
            @Override
            public GenieResponse<RecommendedContentResult> perform() {
                return contentService.getRecommendedContent(recommendedContentRequest);
            }
        });
    }


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
     * @param relatedContentRequest
     * @return
     */
    public void getRelatedContent(final RelatedContentRequest relatedContentRequest, IResponseHandler<RelatedContentResult> responseHandler) {
        new AsyncHandler<RelatedContentResult>(responseHandler).execute(new IPerformable<RelatedContentResult>() {
            @Override
            public GenieResponse<RelatedContentResult> perform() {
                return contentService.getRelatedContent(relatedContentRequest);
            }
        });
    }

    /**
     * This api is used to get all the next {@link List<Content>} based on the hierarchy of {@link List<String>} identifiers passed
     * <p>
     * <p>
     * On successful finding the next list of contents, the response will return status as TRUE and the result will be set with {@link List<Content>}
     *
     * @param contentIdentifiers
     * @return
     */
    public void nextContent(final List<String> contentIdentifiers, IResponseHandler<List<Content>> responseHandler) {
        new AsyncHandler<List<Content>>(responseHandler).execute(new IPerformable<List<Content>>() {
            @Override
            public GenieResponse<List<Content>> perform() {
                return contentService.nextContent(contentIdentifiers);
            }
        });
    }

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
     * @param contentImportRequest
     * @return
     */
    public void importContent(final ContentImportRequest contentImportRequest, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return contentService.importContent(contentImportRequest);
            }
        });
    }

    public void getContentListing(final ContentListingCriteria contentListingCriteria, IResponseHandler<ContentListingResult> responseHandler) {
        new AsyncHandler<ContentListingResult>(responseHandler).execute(new IPerformable<ContentListingResult>() {
            @Override
            public GenieResponse<ContentListingResult> perform() {
                return contentService.getContentListing(contentListingCriteria);
            }
        });
    }

    public void sendFeedback(final ContentFeedback contentFeedback, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return contentFeedbackService.sendFeedback(contentFeedback);
            }
        });
    }

    public void getFeedback(final ContentFeedbackCriteria contentFeedbackCriteria, IResponseHandler<ContentFeedback> responseHandler) {
        new AsyncHandler<ContentFeedback>(responseHandler).execute(new IPerformable<ContentFeedback>() {
            @Override
            public GenieResponse<ContentFeedback> perform() {
                return contentFeedbackService.getFeedback(contentFeedbackCriteria);
            }
        });
    }

}
