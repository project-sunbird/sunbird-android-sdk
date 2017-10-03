package org.ekstep.genieservices.async;

import android.os.AsyncTask;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IContentFeedbackService;
import org.ekstep.genieservices.IContentService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.ChildContentRequest;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentDeleteRequest;
import org.ekstep.genieservices.commons.bean.ContentDeleteResponse;
import org.ekstep.genieservices.commons.bean.ContentDetailsRequest;
import org.ekstep.genieservices.commons.bean.ContentExportRequest;
import org.ekstep.genieservices.commons.bean.ContentExportResponse;
import org.ekstep.genieservices.commons.bean.ContentFeedback;
import org.ekstep.genieservices.commons.bean.ContentFeedbackFilterCriteria;
import org.ekstep.genieservices.commons.bean.ContentFilterCriteria;
import org.ekstep.genieservices.commons.bean.ContentImportRequest;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.ContentListing;
import org.ekstep.genieservices.commons.bean.ContentListingCriteria;
import org.ekstep.genieservices.commons.bean.ContentMoveRequest;
import org.ekstep.genieservices.commons.bean.ContentSearchCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchResult;
import org.ekstep.genieservices.commons.bean.EcarImportRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.HierarchyInfo;
import org.ekstep.genieservices.commons.bean.RecommendedContentRequest;
import org.ekstep.genieservices.commons.bean.RecommendedContentResult;
import org.ekstep.genieservices.commons.bean.RelatedContentRequest;
import org.ekstep.genieservices.commons.bean.RelatedContentResult;

import java.util.List;

/**
 * This class provides APIs for performing {@link ContentService} related operations on a separate thread.
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
     * @param contentDetailsRequest - {@link ContentDetailsRequest}
     * @param responseHandler       - {@link IResponseHandler<Content>}
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
     * @param criteria        - {@link ContentFilterCriteria}
     * @param responseHandler - {@link IResponseHandler<List<Content>>}
     */
    public void getAllLocalContent(final ContentFilterCriteria criteria, IResponseHandler<List<Content>> responseHandler) {
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
     * @param childContentRequest - {@link ChildContentRequest}
     * @param responseHandler     - {@link IResponseHandler<Content>}
     */
    public void getChildContents(final ChildContentRequest childContentRequest, IResponseHandler<Content> responseHandler) {
        new AsyncHandler<Content>(responseHandler).execute(new IPerformable<Content>() {
            @Override
            public GenieResponse<Content> perform() {
                return contentService.getChildContents(childContentRequest);
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
     * @param contentDeleteRequest - {@link ContentDeleteRequest}
     * @param responseHandler      - {@link IResponseHandler<Void>}
     */
    public void deleteContent(final ContentDeleteRequest contentDeleteRequest, IResponseHandler<List<ContentDeleteResponse>> responseHandler) {
        new AsyncHandler<List<ContentDeleteResponse>>(responseHandler).execute(new IPerformable<List<ContentDeleteResponse>>() {
            @Override
            public GenieResponse<List<ContentDeleteResponse>> perform() {
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
     * @param contentSearchCriteria - {@link ContentSearchCriteria}
     * @param responseHandler       - {@link IResponseHandler<ContentSearchResult>}
     */
    public void searchContent(final ContentSearchCriteria contentSearchCriteria, IResponseHandler<ContentSearchResult> responseHandler) {
        new AsyncHandler<ContentSearchResult>(responseHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new IPerformable<ContentSearchResult>() {
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
     * @param recommendedContentRequest - {@link RecommendedContentRequest}
     * @param responseHandler           - {@link IResponseHandler<RecommendedContentResult>}
     */
    public void getRecommendedContent(final RecommendedContentRequest recommendedContentRequest, IResponseHandler<RecommendedContentResult> responseHandler) {
        new AsyncHandler<RecommendedContentResult>(responseHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new IPerformable<RecommendedContentResult>() {
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
     * @param relatedContentRequest - {@link RelatedContentRequest}
     * @param responseHandler       - {@link IResponseHandler<RelatedContentResult>}
     */
    public void getRelatedContent(final RelatedContentRequest relatedContentRequest, IResponseHandler<RelatedContentResult> responseHandler) {
        new AsyncHandler<RelatedContentResult>(responseHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new IPerformable<RelatedContentResult>() {
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
     * @param contentHierarchy         - {@link List<HierarchyInfo>}
     * @param currentContentIdentifier The current content identifier
     * @param responseHandler          - {@link IResponseHandler<Content>}
     */
    public void nextContent(final List<HierarchyInfo> contentHierarchy, final String currentContentIdentifier, IResponseHandler<Content> responseHandler) {
        new AsyncHandler<Content>(responseHandler).execute(new IPerformable<Content>() {
            @Override
            public GenieResponse<Content> perform() {
                return contentService.nextContent(contentHierarchy, currentContentIdentifier);
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
     * @param contentImportRequest - {@link ContentImportRequest}
     * @param responseHandler      - {@link IResponseHandler<List<ContentImportResponse>>}
     */
    public void importContent(final ContentImportRequest contentImportRequest, IResponseHandler<List<ContentImportResponse>> responseHandler) {
        new AsyncHandler<List<ContentImportResponse>>(responseHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new IPerformable<List<ContentImportResponse>>() {
            @Override
            public GenieResponse<List<ContentImportResponse>> perform() {
                return contentService.importContent(contentImportRequest);
            }
        });
    }

    /**
     * This api is used to import the ecar.
     * <p>
     * <p>
     * On successful importing the content, the response will return status as TRUE
     * <p>
     * <p>
     * On failing to import the content, the response will be with return status as FALSE and wih the following error
     * <p>INVALID_FILE
     *
     * @param ecarImportRequest - {@link ContentImportRequest}
     * @param responseHandler   - {@link IResponseHandler<List<ContentImportResponse>>}
     */
    public void importEcar(final EcarImportRequest ecarImportRequest, IResponseHandler<List<ContentImportResponse>> responseHandler) {
        new AsyncHandler<List<ContentImportResponse>>(responseHandler).execute(new IPerformable<List<ContentImportResponse>>() {
            @Override
            public GenieResponse<List<ContentImportResponse>> perform() {
                return contentService.importEcar(ecarImportRequest);
            }
        });
    }

    /**
     * This api is used get the complete content listing with criteria mentioned in {@link ContentListingCriteria}
     * <p>
     * <p>
     * On successful fetching, the response will return status as TRUE, with result set as {@link ContentListing}.
     * <p>
     * <p>
     * On failing to delete a content, the response will return status as FALSE
     *
     * @param contentListingCriteria - {@link ContentListingCriteria}
     * @param responseHandler        - {@link ContentListingCriteria}
     */
    public void getContentListing(final ContentListingCriteria contentListingCriteria, IResponseHandler<ContentListing> responseHandler) {
        new AsyncHandler<ContentListing>(responseHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new IPerformable<ContentListing>() {
            @Override
            public GenieResponse<ContentListing> perform() {
                return contentService.getContentListing(contentListingCriteria);
            }
        });
    }

    /**
     * This api is used to save the feedback about content.
     * <p>
     * <p>
     * On successful sending the feedback, the response will return status as TRUE.
     * <p>
     * <p>
     * On failing to delete a content, the response will return status as FALSE
     *
     * @param contentFeedback - {@link ContentFeedback}
     * @param responseHandler - {@link IResponseHandler<Void>}
     */
    public void sendFeedback(final ContentFeedback contentFeedback, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return contentFeedbackService.sendFeedback(contentFeedback);
            }
        });
    }

    /**
     * This api is used to get the feedback about a content.
     * <p>
     * <p>On successful fetching the data, the response will return status as TRUE and with result type as {@link ContentFeedback}, if content has any feedback then the result will not be null,
     * <p>
     * On failing to fetch the data, the response will return status as FALSE with the following error.
     *
     * @param contentFeedbackFilterCriteria - {@link ContentFeedbackFilterCriteria}
     * @param responseHandler               - {@link IResponseHandler<List<ContentFeedback>>}
     */
    public void getFeedback(final ContentFeedbackFilterCriteria contentFeedbackFilterCriteria, IResponseHandler<List<ContentFeedback>> responseHandler) {
        new AsyncHandler<List<ContentFeedback>>(responseHandler).execute(new IPerformable<List<ContentFeedback>>() {
            @Override
            public GenieResponse<List<ContentFeedback>> perform() {
                return contentFeedbackService.getFeedback(contentFeedbackFilterCriteria);
            }
        });
    }

    /**
     * This api is used to get the status of when importing a content
     * <p>
     * <p>
     * Response will always be status set TRUE, with {@link ContentImportResponse} set in result.
     *
     * @param contentId       Content id.
     * @param responseHandler - {@link IResponseHandler<ContentImportResponse>}
     */
    public void getImportStatus(final String contentId, IResponseHandler<ContentImportResponse> responseHandler) {
        new AsyncHandler<ContentImportResponse>(responseHandler).execute(new IPerformable<ContentImportResponse>() {
            @Override
            public GenieResponse<ContentImportResponse> perform() {
                return contentService.getImportStatus(contentId);
            }
        });
    }

    /**
     * This api is used to cancel the on-going download
     * <p>
     * <p>
     * Response will always be status set TRUE.
     *
     * @param contentId       Content id.
     * @param responseHandler - {@link IResponseHandler<Void>}
     */
    public void cancelDownload(final String contentId, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return contentService.cancelDownload(contentId);
            }
        });
    }

    /**
     * This api is used to export the list of contentId's needed.
     * <p>
     * <p>
     * On successful exporting the content, the response will return status as TRUE, with response set in result
     * <p>
     * <p>
     * On failing to export the content, the response will be with return status as FALSE and with the following error
     * <p>EXPORT_FAILED
     *
     * @param contentExportRequest - {@link ContentExportRequest}
     * @param responseHandler      - {@link IResponseHandler<ContentExportResponse>}
     */
    public void exportContent(final ContentExportRequest contentExportRequest, IResponseHandler<ContentExportResponse> responseHandler) {
        new AsyncHandler<ContentExportResponse>(responseHandler).execute(new IPerformable<ContentExportResponse>() {
            @Override
            public GenieResponse<ContentExportResponse> perform() {
                return contentService.exportContent(contentExportRequest);
            }
        });
    }

    /**
     * This api is used to move all the downloaded content from source to destination folder.
     * <p>
     * <p>
     * On successful moving the content, the response will return status as TRUE, with response set in result
     * <p>
     * <p>
     * On failing to move the content, the response will be with return status as FALSE and with the following error
     * <p>MOVE_FAILED
     *
     * @param contentMoveRequest - {@link ContentMoveRequest}
     * @param responseHandler    - {@link IResponseHandler<Void>}
     */
    public void moveContent(final ContentMoveRequest contentMoveRequest, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return contentService.moveContent(contentMoveRequest);
            }
        });
    }

}
