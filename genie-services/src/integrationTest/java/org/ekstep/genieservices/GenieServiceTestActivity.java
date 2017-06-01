package org.ekstep.genieservices;

import android.app.Activity;
import android.os.Bundle;

import org.ekstep.genieservices.commons.AndroidAppContext;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchResult;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MasterData;
import org.ekstep.genieservices.commons.bean.PartnerData;
import org.ekstep.genieservices.commons.bean.Profile;
import org.ekstep.genieservices.commons.bean.RelatedContentResult;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;
import org.ekstep.genieservices.commons.bean.telemetry.Telemetry;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by Sneha on 4/26/2017.
 */

public class GenieServiceTestActivity extends Activity {

    private boolean idle = true;
    private GenieService mGenieService;
    private AppContext mAppContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genie_services_test);

        mGenieService = GenieService.init(getApplicationContext(), getPackageName());
        mAppContext = AndroidAppContext.buildAppContext(getApplicationContext(), getPackageName());
        GenieServiceDBHelper.init(mAppContext);
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
        GenieResponse<Profile> genieResponse = mGenieService.getUserProfileService().createUserProfile(userProfile);
        return genieResponse;
    }

    public GenieResponse deleteUserProfile(String uid) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getUserProfileService().deleteUser(uid);
        return genieResponse;
    }

    public GenieResponse<Profile> getAnonymousUser() {
        idle = false;
        GenieResponse<Profile> genieResponse = mGenieService.getUserProfileService().getAnonymousUser();
        return genieResponse;
    }

    public GenieResponse<String> setAnonymousUser() {
        idle = false;
        GenieResponse<String> genieResponse = mGenieService.getUserProfileService().setAnonymousUser();
        return genieResponse;
    }

    public GenieResponse<Profile> getCurrentUser() {
        idle = false;
        GenieResponse<Profile> genieResponse = mGenieService.getUserProfileService().getCurrentUser();
        return genieResponse;
    }

    public GenieResponse setCurrentUser(String uid) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getUserProfileService().setCurrentUser(uid);
        return genieResponse;
    }

    public GenieResponse<Profile> updateUserProfile(Profile profile) {
        idle = false;
        GenieResponse<Profile> genieResponse = mGenieService.getUserProfileService().updateUserProfile(profile);
        return genieResponse;
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

    public GenieResponse<Void> importContent(boolean hasChildContent, String filepath) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getContentService().importContent(hasChildContent, filepath);
        return genieResponse;
    }

    public GenieResponse<Void> deleteContent(String contentIdentifier, int level) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getContentService().deleteContent(contentIdentifier, level);
        return genieResponse;
    }

    public GenieResponse<List<Content>> getAllLocalContent(ContentCriteria contentCriteria) {
        idle = false;
        GenieResponse<List<Content>> genieResponse = mGenieService.getContentService().getAllLocalContent(contentCriteria);
        return genieResponse;
    }

    public GenieResponse<List<Content>> getChildContents(String contentIdentifier, int levelAndState) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getContentService().getChildContents(contentIdentifier, levelAndState);
        return genieResponse;
    }

    public GenieResponse<ContentSearchResult> getRecommendedContent(String language) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getContentService().getRecommendedContent(language);
        return genieResponse;
    }

    public GenieResponse<Content> getContentDetails(String contentIdentifier) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getContentService().getContentDetails(contentIdentifier);
        return genieResponse;
    }

    public GenieResponse<RelatedContentResult> getRelatedContent(String contentIdentifier) {
        idle = false;
        GenieResponse<RelatedContentResult> genieResponse = mGenieService.getContentService().getRelatedContent(contentIdentifier);
        return genieResponse;
    }

    public GenieResponse<ContentSearchResult> searchContent(ContentSearchCriteria contentSearchCriteria) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getContentService().searchContent(contentSearchCriteria);
        return genieResponse;
    }

    public GenieResponse importContent(boolean isChildContent, List<String> contentIdentifiers) {
        idle = false;
        GenieResponse genieResponse = mGenieService.getContentService().importContent(isChildContent, contentIdentifiers);
        return genieResponse;
    }

    public void setIdle() {
        idle = true;
    }

    public boolean isIdle() {
        return idle;
    }

}
