package org.ekstep.genieservices;

import android.app.Activity;
import android.os.Bundle;

import org.ekstep.genieservices.commons.AndroidAppContext;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.ChildContentRequest;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentAccess;
import org.ekstep.genieservices.commons.bean.ContentAccessFilterCriteria;
import org.ekstep.genieservices.commons.bean.ContentDeleteRequest;
import org.ekstep.genieservices.commons.bean.ContentDeleteResponse;
import org.ekstep.genieservices.commons.bean.ContentDetailsRequest;
import org.ekstep.genieservices.commons.bean.ContentExportRequest;
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
import org.ekstep.genieservices.commons.bean.LearnerAssessmentDetails;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentSummary;
import org.ekstep.genieservices.commons.bean.MasterData;
import org.ekstep.genieservices.commons.bean.Notification;
import org.ekstep.genieservices.commons.bean.NotificationFilterCriteria;
import org.ekstep.genieservices.commons.bean.PartnerData;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.ProfileExportRequest;
import org.ekstep.genieservices.commons.bean.ProfileExportResponse;
import org.ekstep.genieservices.commons.bean.ProfileImportRequest;
import org.ekstep.genieservices.commons.bean.ProfileImportResponse;
import org.ekstep.genieservices.commons.bean.RecommendedContentRequest;
import org.ekstep.genieservices.commons.bean.RecommendedContentResult;
import org.ekstep.genieservices.commons.bean.RelatedContentRequest;
import org.ekstep.genieservices.commons.bean.RelatedContentResult;
import org.ekstep.genieservices.commons.bean.SummaryRequest;
import org.ekstep.genieservices.commons.bean.SyncStat;
import org.ekstep.genieservices.commons.bean.Tag;
import org.ekstep.genieservices.commons.bean.TelemetryExportRequest;
import org.ekstep.genieservices.commons.bean.TelemetryExportResponse;
import org.ekstep.genieservices.commons.bean.TelemetryImportRequest;
import org.ekstep.genieservices.commons.bean.TelemetryStat;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by Sneha on 4/26/2017.
 */

public class GenieServiceTestActivity extends Activity {
    private static final String TAG = GenieServiceTestActivity.class.getSimpleName();

    private boolean idle = true;
    private GenieService mGenieService;
    private AppContext mAppContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genie_services_test);

        mGenieService = GenieService.init(getApplicationContext(), "org.ekstep.genieservices");
        mAppContext = AndroidAppContext.buildAppContext(getApplicationContext(), "org.ekstep.genieservices");
        GenieServiceDBHelper.init(mAppContext);
        GenieSdkEventListener.init();

    }

    public boolean isFilePresent(String filePath) {
        String[] fileList = new File(filePath).list();
        if (fileList == null) {
            return false;
        } else if (fileList.length == 1) {
            return false;
        } else {
            return true;
        }
    }

    public GenieResponse<MasterData> getMasterData(MasterDataType masterDataType) {
        idle = false;
        GenieResponse<MasterData> genieResponse = mGenieService.getConfigService().getMasterData(masterDataType);
        return genieResponse;
    }

    /**
     * TODO GetAllMasterData implementation missing in the GenieServices.
     */
    public GenieResponse<List<MasterData>> getAllMasterData() {
        idle = false;
        GenieResponse<List<MasterData>> genieResponse = mGenieService.getConfigService().getAllMasterData();
        return genieResponse;
    }

    public GenieResponse<Map<String, Object>> getOrdinals() {
        idle = false;
        GenieResponse<Map<String, Object>> genieResponse = mGenieService.getConfigService().getOrdinals();
        return genieResponse;
    }

    public GenieResponse<Map<String, Object>> getResourceBundle(String languageIdentifier) {
        idle = false;
        GenieResponse<Map<String, Object>> genieResponse = mGenieService.getConfigService().getResourceBundle(languageIdentifier);
        return genieResponse;
    }

    public GenieResponse<Profile> createUserProfile(Profile userProfile) {
        idle = false;
        GenieResponse<Profile> genieResponse = mGenieService.getUserService().createUserProfile(userProfile);
        return genieResponse;
    }

    public GenieResponse deleteUserProfile(String uid) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getUserService().deleteUser(uid);
        return genieResponse;
    }

    public GenieResponse<Profile> getAnonymousUser() {
        idle = false;
        GenieResponse<Profile> genieResponse = mGenieService.getUserService().getAnonymousUser();
        return genieResponse;
    }

    public GenieResponse<String> setAnonymousUser() {
        idle = false;
        GenieResponse<String> genieResponse = mGenieService.getUserService().setAnonymousUser();
        return genieResponse;
    }

    public GenieResponse<Profile> getCurrentUser() {
        idle = false;
        GenieResponse<Profile> genieResponse = mGenieService.getUserService().getCurrentUser();
        return genieResponse;
    }

    public GenieResponse setCurrentUser(String uid) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getUserService().setCurrentUser(uid);
        return genieResponse;
    }

    public GenieResponse<Profile> updateUserProfile(Profile profile) {
        idle = false;
        GenieResponse<Profile> genieResponse = mGenieService.getUserService().updateUserProfile(profile);
        return genieResponse;
    }

    public GenieResponse<List<Profile>> getAllUserProfile() {
        idle = false;
        GenieResponse<List<Profile>> genieResponse = mGenieService.getUserService().getAllUserProfile();
        return genieResponse;
    }

    public void importProfile(final ProfileImportRequest profileImportRequest, IResponseHandler<ProfileImportResponse> responseHandler) {
        GenieService.getAsyncService().getUserService().importProfile(profileImportRequest, responseHandler);
    }

    public GenieResponse<ProfileImportResponse> importProfile(final ProfileImportRequest profileImportRequest) {
        return mGenieService.getUserService().importProfile(profileImportRequest);
    }


    public GenieResponse<ProfileExportResponse> exportProfile(final ProfileExportRequest profileExportRequest) {
        return mGenieService.getUserService().exportProfile(profileExportRequest);
    }

    public GenieResponse saveTelemetry(Telemetry event) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getTelemetryService().saveTelemetry(event);
        return genieResponse;
    }

    public GenieResponse saveTelemetry(String eventString) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getTelemetryService().saveTelemetry(eventString);
        return genieResponse;
    }

    public GenieResponse registerPartner(PartnerData partnerData) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getPartnerService().registerPartner(partnerData);
        return genieResponse;
    }

    public GenieResponse<Void> importTelemetry(TelemetryImportRequest telemetryImportRequest) {
        idle = false;
        GenieResponse<Void> genieResponse = mGenieService.getTelemetryService().importTelemetry(telemetryImportRequest);
        return genieResponse;
    }

    public GenieResponse<TelemetryExportResponse> exportTelemetry(TelemetryExportRequest telemetryExportRequest) {
        idle = false;
        GenieResponse<TelemetryExportResponse> genieResponse = mGenieService.getTelemetryService().exportTelemetry(telemetryExportRequest);
        return genieResponse;
    }

    public GenieResponse isPartnerRegistered(String partnerID) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getPartnerService().isRegistered(partnerID);
        return genieResponse;
    }

    public GenieResponse startPartnerSession(PartnerData partnerData) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getPartnerService().startPartnerSession(partnerData);
        return genieResponse;
    }

    public GenieResponse terminatePartnerSession(PartnerData partnerData) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getPartnerService().terminatePartnerSession(partnerData);
        return genieResponse;
    }

    public GenieResponse sendData(PartnerData partnerData) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getPartnerService().sendData(partnerData);
        return genieResponse;
    }

    public GenieResponse<List<ContentImportResponse>> importEcar(EcarImportRequest ecarImportRequest) {
        idle = false;
        GenieResponse<List<ContentImportResponse>> genieResponse = mGenieService.getContentService().importEcar(ecarImportRequest);
        return genieResponse;
    }

    //to be done
    public GenieResponse<List<ContentImportResponse>> importContent(ContentImportRequest contentImportRequest) {
        idle = false;
        GenieResponse<List<ContentImportResponse>> genieResponse = mGenieService.getContentService().importContent(contentImportRequest);
        return genieResponse;
    }

    public GenieResponse<Void> cancelDownload(String identifier) {
        idle = false;
        GenieResponse<Void> genieResponse = mGenieService.getContentService().cancelDownload(identifier);
        return genieResponse;
    }

    public GenieResponse exportContent(ContentExportRequest contentExportRequest) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getContentService().exportContent(contentExportRequest);
        return genieResponse;
    }

    public GenieResponse<List<ContentDeleteResponse>> deleteContent(ContentDeleteRequest contentDeleteCriteria) {
        idle = false;
        GenieResponse<List<ContentDeleteResponse>> genieResponse = mGenieService.getContentService().deleteContent(contentDeleteCriteria);
        return genieResponse;
    }

    public GenieResponse<List<Content>> getAllLocalContent(ContentFilterCriteria contentCriteria) {
        idle = false;
        GenieResponse<List<Content>> genieResponse = mGenieService.getContentService().getAllLocalContent(contentCriteria);
        return genieResponse;
    }

    public GenieResponse<Content> getChildContents(ChildContentRequest childContentRequest) {
        idle = false;
        GenieResponse<Content> genieResponse = mGenieService.getContentService().getChildContents(childContentRequest);
        return genieResponse;
    }

    public GenieResponse<Content> nextContent(List<HierarchyInfo> contentHierarchy, String currentContentIdentifier) {
        idle = false;
        GenieResponse<Content> genieResponse = mGenieService.getContentService().nextContent(contentHierarchy, currentContentIdentifier);
        return genieResponse;
    }

    public GenieResponse<ContentSearchResult> searchContent(ContentSearchCriteria contentSearchCriteria) {
        idle = false;
        GenieResponse<ContentSearchResult> genieResponse = mGenieService.getContentService().searchContent(contentSearchCriteria);
        return genieResponse;
    }

    public GenieResponse<ContentListing> getContentListing(ContentListingCriteria contentListingCriteria) {
        idle = false;
        GenieResponse<ContentListing> genieResponse = mGenieService.getContentService().getContentListing(contentListingCriteria);
        return genieResponse;
    }

    public GenieResponse<RecommendedContentResult> getRecommendedContent(RecommendedContentRequest recommendedContentRequest) {
        idle = false;
        GenieResponse<RecommendedContentResult> genieResponse = mGenieService.getContentService().getRecommendedContent(recommendedContentRequest);
        return genieResponse;
    }

    //to be done
    public GenieResponse<ContentImportResponse> getImportStatus(String identifier) {
        idle = false;
        GenieResponse<ContentImportResponse> genieResponse = mGenieService.getContentService().getImportStatus(identifier);
        return genieResponse;
    }

    public GenieResponse<Content> getContentDetails(ContentDetailsRequest contentDetailsRequest) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getContentService().getContentDetails(contentDetailsRequest);
        return genieResponse;
    }

    public GenieResponse<RelatedContentResult> getRelatedContent(RelatedContentRequest contentRequest) {
        idle = false;
        GenieResponse<RelatedContentResult> genieResponse = mGenieService.getContentService().getRelatedContent(contentRequest);
        return genieResponse;
    }

    public GenieResponse sendFeedback(ContentFeedback contentFeedback) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getContentFeedbackService().sendFeedback(contentFeedback);
        return genieResponse;
    }

    public GenieResponse<List<ContentFeedback>> getFeedback(ContentFeedbackFilterCriteria contentFeedbackCriteria) {
        idle = false;
        GenieResponse<List<ContentFeedback>> genieResponse = mGenieService.getContentFeedbackService().getFeedback(contentFeedbackCriteria);
        return genieResponse;
    }

    public GenieResponse<TelemetryStat> getTelemetryStat() {
        idle = false;
        GenieResponse<TelemetryStat> genieResponse = mGenieService.getTelemetryService().getTelemetryStat();
        return genieResponse;
    }

    public GenieResponse<SyncStat> sync() {
        idle = false;
        GenieResponse<SyncStat> genieResponse = mGenieService.getSyncService().sync();
        return genieResponse;
    }

    public GenieResponse<String> getLanguageTraversalRule(String languageId) {
        idle = false;
        GenieResponse<String> genieResponse = mGenieService.getLanguageService().getLanguageTraversalRule(languageId);
        return genieResponse;
    }

    public GenieResponse getLanguageSearch(String requestData) {
        idle = false;
        GenieResponse<String> genieResponse = mGenieService.getLanguageService().getLanguageSearch(requestData);
        return genieResponse;
    }

    public GenieResponse addContentAccess(ContentAccess contentAccess) {
        idle = false;
        GenieResponse<Void> genieResponse = mGenieService.getUserService().addContentAccess(contentAccess);
        return genieResponse;
    }


    public GenieResponse getAllContentAccess(ContentAccessFilterCriteria filterCriteria) {
        idle = false;
        GenieResponse<List<ContentAccess>> genieResponse = mGenieService.getUserService().getAllContentAccess(filterCriteria);
        return genieResponse;
    }

    public GenieResponse<List<LearnerAssessmentSummary>> getSummary(SummaryRequest summaryRequest) {
        idle = false;
        return mGenieService.getSummarizerService().getSummary(summaryRequest);
    }

    public GenieResponse<List<LearnerAssessmentDetails>> getLearnerAssessmentDetails(SummaryRequest summaryRequest) {
        idle = false;
        return mGenieService.getSummarizerService().getLearnerAssessmentDetails(summaryRequest);
    }

    public GenieResponse setTag(Tag tag) {
        idle = false;
        GenieResponse<Void> genieResponse = mGenieService.getTagService().setTag(tag);
        return genieResponse;
    }

    public GenieResponse<List<Tag>> getTag() {
        idle = false;
        GenieResponse<List<Tag>> genieResponse = mGenieService.getTagService().getTags();
        return genieResponse;
    }

    public GenieResponse<Void> deleteTag(String name) {
        idle = false;
        GenieResponse<Void> genieResponse = mGenieService.getTagService().deleteTag(name);
        return genieResponse;
    }

    public GenieResponse<Void> updateTag(Tag tag) {
        idle = false;
        GenieResponse<Void> genieResponse = mGenieService.getTagService().updateTag(tag);
        return genieResponse;
    }

    public GenieResponse<Void> addNotification(Notification notification) {
        idle = false;
        GenieResponse<Void> genieResponse = mGenieService.getNotificationService().addNotification(notification);
        return genieResponse;
    }

    public GenieResponse<Notification> updateNotification(Notification notification) {
        idle = false;
        GenieResponse<Notification> genieResponse = mGenieService.getNotificationService().updateNotification(notification);
        return genieResponse;
    }

    public GenieResponse<Void> deleteNotification(int msgId) {
        idle = false;
        GenieResponse<Void> genieResponse = mGenieService.getNotificationService().deleteNotification(msgId);
        return genieResponse;
    }

    public GenieResponse<List<Notification>> getAllNotifications(NotificationFilterCriteria filterCriteria) {
        idle = false;
        GenieResponse<List<Notification>> genieResponse = mGenieService.getNotificationService().getAllNotifications(filterCriteria);
        return genieResponse;
    }

    public GenieResponse<Void> moveContent(ContentMoveRequest.Builder contentMoveRequest) {
        idle = false;
        GenieResponse<Void> genieResponse = mGenieService.getContentService().moveContent(contentMoveRequest.build());
        return genieResponse;
    }

    public void setIdle() {
        idle = true;
    }

    public boolean isIdle() {
        return idle;
    }

}
