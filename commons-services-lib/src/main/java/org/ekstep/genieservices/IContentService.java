package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.ChildContentRequest;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentDeleteRequest;
import org.ekstep.genieservices.commons.bean.ContentDeleteResponse;
import org.ekstep.genieservices.commons.bean.ContentDetailsRequest;
import org.ekstep.genieservices.commons.bean.ContentExportRequest;
import org.ekstep.genieservices.commons.bean.ContentExportResponse;
import org.ekstep.genieservices.commons.bean.ContentFilterCriteria;
import org.ekstep.genieservices.commons.bean.ContentImportRequest;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.ContentListing;
import org.ekstep.genieservices.commons.bean.ContentListingCriteria;
import org.ekstep.genieservices.commons.bean.ContentMarkerRequest;
import org.ekstep.genieservices.commons.bean.ContentMoveRequest;
import org.ekstep.genieservices.commons.bean.ContentSearchCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchResult;
import org.ekstep.genieservices.commons.bean.ContentSpaceUsageSummaryRequest;
import org.ekstep.genieservices.commons.bean.ContentSpaceUsageSummaryResponse;
import org.ekstep.genieservices.commons.bean.ContentSwitchRequest;
import org.ekstep.genieservices.commons.bean.EcarImportRequest;
import org.ekstep.genieservices.commons.bean.FlagContentRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.HierarchyInfo;
import org.ekstep.genieservices.commons.bean.MoveContentResponse;
import org.ekstep.genieservices.commons.bean.RecommendedContentRequest;
import org.ekstep.genieservices.commons.bean.RecommendedContentResult;
import org.ekstep.genieservices.commons.bean.RelatedContentRequest;
import org.ekstep.genieservices.commons.bean.RelatedContentResult;
import org.ekstep.genieservices.commons.bean.ScanStorageRequest;
import org.ekstep.genieservices.commons.bean.ScanStorageResponse;
import org.ekstep.genieservices.commons.bean.SummarizerContentFilterCriteria;
import org.ekstep.genieservices.commons.bean.SunbirdContentSearchCriteria;
import org.ekstep.genieservices.commons.bean.SunbirdContentSearchResult;
import org.ekstep.genieservices.commons.bean.SwitchContentResponse;
import org.ekstep.genieservices.commons.bean.enums.DownloadAction;

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
     * @param contentDetailsRequest - {@link ContentDetailsRequest}
     * @return {@link GenieResponse<Content>}
     */
    GenieResponse<Content> getContentDetails(ContentDetailsRequest contentDetailsRequest);

    /**
     * This api is used to get all the contents.
     * <p>
     * <p>
     * Response status will always be TRUE with {@link List<Content>} set in the result.
     *
     * @param criteria - {@link ContentFilterCriteria}
     * @return {@link GenieResponse<List<Content>>}
     */
    GenieResponse<List<Content>> getAllLocalContent(ContentFilterCriteria criteria);

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
     * @return {@link GenieResponse<Content>}
     */
    GenieResponse<Content> getChildContents(ChildContentRequest childContentRequest);

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
     * @return - {@link GenieResponse<List<ContentDeleteResponse>>}
     */
    GenieResponse<List<ContentDeleteResponse>> deleteContent(ContentDeleteRequest contentDeleteRequest);

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
     * @return {@link GenieResponse<ContentListing>}
     */
    GenieResponse<ContentListing> getContentListing(ContentListingCriteria contentListingCriteria);

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
     * @return {@link GenieResponse<ContentSearchResult>}
     */
    GenieResponse<ContentSearchResult> searchContent(ContentSearchCriteria contentSearchCriteria);

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
     * @return {@link GenieResponse<RecommendedContentResult>}
     */
    GenieResponse<RecommendedContentResult> getRecommendedContent(RecommendedContentRequest recommendedContentRequest);

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
     * @param relatedContentRequest - {@link RelatedContentResult}
     * @return - {@link GenieResponse<RelatedContentResult>}
     */
    GenieResponse<RelatedContentResult> getRelatedContent(RelatedContentRequest relatedContentRequest);

    /**
     * This api is used to get all the previous {@link List<Content>} based on the hierarchy of {@link List<String>} identifiers passed
     * <p>
     * <p>
     * On successful finding the previous list of contents, the response will return status as TRUE and the result will be set with {@link List<Content>}
     *
     * @param contentHierarchy         - {@link List<HierarchyInfo>}
     * @param currentContentIdentifier The current content identifier
     * @return - {@link GenieResponse<Content>}
     */
    GenieResponse<Content> prevContent(List<HierarchyInfo> contentHierarchy, String currentContentIdentifier);

    /**
     * This api is used to get all the next {@link List<Content>} based on the hierarchy of {@link List<String>} identifiers passed
     * <p>
     * <p>
     * On successful finding the next list of contents, the response will return status as TRUE and the result will be set with {@link List<Content>}
     *
     * @param contentHierarchy         - {@link List<HierarchyInfo>}
     * @param currentContentIdentifier The current content identifier
     * @return - {@link GenieResponse<Content>}
     */
    GenieResponse<Content> nextContent(List<HierarchyInfo> contentHierarchy, String currentContentIdentifier);

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
     * @return - {@link GenieResponse<List<ContentImportResponse>>}
     */
    GenieResponse<List<ContentImportResponse>> importEcar(EcarImportRequest ecarImportRequest);

    /**
     * This api is used to import the content of specified contentId's from server.
     * <p>
     * <p>
     * On successful importing the content, the response will return status as TRUE
     *
     * @param contentImportRequest - {@link ContentImportRequest}
     * @return - {@link GenieResponse<List<ContentImportResponse>>}
     */
    GenieResponse<List<ContentImportResponse>> importContent(ContentImportRequest contentImportRequest);

    /**
     * This api is used to get the status of when importing a content
     * <p>
     * <p>
     * Response will always be status set TRUE, with {@link ContentImportResponse} set in result.
     *
     * @param contentId Content id.
     * @return {@link GenieResponse<ContentImportResponse>}
     */
    GenieResponse<ContentImportResponse> getImportStatus(String contentId);


    /**
     * This api is used to get the status of contents
     * <p>
     * <p>
     * Response will always be status set TRUE, with {@link ContentImportResponse} set in result.
     *
     * @param contentIdList Content idList.
     * @return {@link GenieResponse<ContentImportResponse>}
     */
    GenieResponse<List<ContentImportResponse>> getImportStatus(List<String> contentIdList);


    /**
     * This api is used to cancel the on-going download
     * <p>
     * <p>
     * Response will always be status set TRUE.
     *
     * @param contentId Content id.
     * @return {@link GenieResponse<Void>}
     */
    GenieResponse<Void> cancelDownload(String contentId);

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
     * @param contentExportRequest {@link ContentExportRequest}
     * @return {@link GenieResponse<ContentExportResponse>}
     */
    GenieResponse<ContentExportResponse> exportContent(ContentExportRequest contentExportRequest);

    /**
     * This api is used to pause / resume the  download queue.
     * <p>
     * <p>
     * On successful setting the status, the response will return status as TRUE, with response set in result
     * <p>
     * <p>
     * On failing tosetting the status, the response will be with return status as FALSE
     */
    GenieResponse<Void> setDownloadAction(DownloadAction action);

    /**
     * This api is used get Download queue state.
     * <p>
     * <p>
     * Response will always be status set TRUE, with {@link DownloadAction} set in result.
     * <p>
     */
    GenieResponse<DownloadAction> getDownloadState();

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
     */
    GenieResponse<List<MoveContentResponse>> moveContent(ContentMoveRequest contentMoveRequest);

    /**
     * This api is used to get the last modified time of the folder in the path passed to the api and then cross check it against
     * the stored last modified, and if their is a difference, then show the differences.
     * <p>
     * <p>
     * Response will be the list of {@link ScanStorageResponse} which contains added, deleted and updated contents
     *
     * @param scanStorageRequest {@link ScanStorageRequest}
     * @return
     */
    GenieResponse<List<ScanStorageResponse>> scanStorage(ScanStorageRequest scanStorageRequest);

    /**
     * This api is used to switch the location of the source folder, from where contents are shown
     * <p>
     * <p>
     * On successful switching, the response will return status as TRUE, with response set in result
     * <p>
     * <p>
     * On failing to move the content, the response will be with return status as FALSE and with the following error
     * <p>SWITCH_FAILED
     *
     * @param contentSwitchRequest - {@link ContentSwitchRequest}
     */
    GenieResponse<List<SwitchContentResponse>> switchContent(ContentSwitchRequest contentSwitchRequest);

    /**
     * This API is used to get the space used by contents.
     *
     * @param contentSpaceUsageSummaryRequest {@link ContentSpaceUsageSummaryRequest}
     * @return
     */
    GenieResponse<List<ContentSpaceUsageSummaryResponse>> getContentSpaceUsageSummary(ContentSpaceUsageSummaryRequest contentSpaceUsageSummaryRequest);

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
     * @param contentSearchCriteria - {@link SunbirdContentSearchCriteria}
     * @return {@link GenieResponse<SunbirdContentSearchResult>}
     */
    GenieResponse<SunbirdContentSearchResult> searchSunbirdContent(SunbirdContentSearchCriteria contentSearchCriteria);

    /**
     * This api is used to flag content with the content flag request mentioned in {@link FlagContentRequest}
     * <p>
     * <p>
     * On successful, the response will return status as TRUE and the result set with {@link Void}
     * <p>
     * <p>
     * On failure, the response with return status as FALSE with one of the following errors
     * <p>CONNECTION_ERROR
     * <p>SERVER_ERROR
     * <p>NETWORK_ERROR
     *
     * @param flagContentRequest - {@link FlagContentRequest}
     * @return {@link GenieResponse<Void>}
     */
    GenieResponse<Void> flagContent(FlagContentRequest flagContentRequest);

    /**
     * This api is used to get all the contents.
     * <p>
     * <p>
     * Response status will always be TRUE with {@link List<Content>} set in the result.
     *
     * @param criteria - {@link ContentFilterCriteria}
     * @return {@link GenieResponse<List<Content>>}
     */
    GenieResponse<List<Content>> getLocalContents(SummarizerContentFilterCriteria criteria);

    GenieResponse<Void> setContentMarker(ContentMarkerRequest contentMarkerRequest);
}
