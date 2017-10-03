package org.ekstep.genieservices.contentservice.content;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.ekstep.genieservices.EcarCopyUtil;
import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentDetailsRequest;
import org.ekstep.genieservices.commons.bean.ContentFeedback;
import org.ekstep.genieservices.commons.bean.ContentFeedbackFilterCriteria;
import org.ekstep.genieservices.commons.bean.EcarImportRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sneha on 6/2/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class ContentFeedbackServiceTest extends GenieServiceTestBase {
    private static final String TAG = ContentFeedbackServiceTest.class.getSimpleName();
    final String CONTENT_ID = "do_30013486";
    private final String CONTENT_FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/Multiplication2.ecar";

    private final String CONTENT_ASSET_PATH = "Download/Multiplication2.ecar";

    @Before
    public void setup() throws IOException {
        super.setup();
        activity = rule.getActivity();
        GenieServiceDBHelper.clearContentEntryFromDB();
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }

    @Test
    public void _0ShouldCopyNeccesoryEcar() {
        EcarCopyUtil.createFileFromAsset(activity.getApplicationContext(), CONTENT_ASSET_PATH, DESTINATION);
    }

    @Test
    public void _1ShouldSendFeedbackForEcar() {

        GenieServiceDBHelper.clearContentEntryFromDB();

        //create profile
        String uid = createAndSetProfileForGetFeedback();

        //import content
        importContent();

        //send feedback
        ContentFeedback sendFeedbackData = new ContentFeedback();
        sendFeedbackData.setComments("Great content");
        sendFeedbackData.setContentId(CONTENT_ID);
        sendFeedbackData.setRating(3);
        GenieResponse genieResponseSendFeedback = activity.sendFeedback(sendFeedbackData);
        Assert.assertTrue(genieResponseSendFeedback.getStatus());

        //assert send feedback data
        shouldAssertSendFeedbackData(sendFeedbackData);

        //get feedback
        ContentFeedbackFilterCriteria.Builder data = new ContentFeedbackFilterCriteria.Builder()
                .byUser(uid).forContent(CONTENT_ID);
        GenieResponse<List<ContentFeedback>> genieResponseGetFeedback = activity.getFeedback(data.build());
        Assert.assertTrue(genieResponseGetFeedback.getStatus());
        ContentFeedback getFeedbackData = genieResponseGetFeedback.getResult().get(0);

        //assert get feedback data
        shouldAssertGetFeedbackData(sendFeedbackData, getFeedbackData);
    }

    @Test
    public void _2ShouldGettheFeedbackforagivenUid() {

        GenieServiceDBHelper.clearContentEntryFromDB();

        //create profile
        String uid = createAndSetProfileForGetFeedback();

        //import content
        importContent();

        //send feedback
        ContentFeedback sendFeedbackData = new ContentFeedback();
        sendFeedbackData.setComments("Great content");
        sendFeedbackData.setContentId(CONTENT_ID);
        sendFeedbackData.setRating(3);
        GenieResponse genieResponseSendFeedback = activity.sendFeedback(sendFeedbackData);
        Assert.assertTrue(genieResponseSendFeedback.getStatus());

    }

    @Test
    public void _3ShouldUpdatetheFeedbackforGivenUid() {

        //send feedback
        ContentFeedbackFilterCriteria.Builder feedbackCriteria = new ContentFeedbackFilterCriteria.Builder()
                .byUser("sampleuid");
        GenieResponse genieResponseSendFeedback = activity.getFeedback(feedbackCriteria.build());
        Assert.assertTrue(genieResponseSendFeedback.getStatus());


    }

    @Test
    public void _4ShouldThrowValidationErrorfirEmptyContentId() {

        //send feedback
        ContentFeedback sendFeedbackData = new ContentFeedback();
        sendFeedbackData.setComments("Great content Updated");
        sendFeedbackData.setRating(3);
        GenieResponse genieResponseSendFeedback = activity.sendFeedback(sendFeedbackData);
        Assert.assertFalse(genieResponseSendFeedback.getStatus());
        Assert.assertEquals(ServiceConstants.ErrorCode.VALIDATION_ERROR, genieResponseSendFeedback.getError());
        Assert.assertEquals(ServiceConstants.ErrorMessage.MANDATORY_FIELD_CONTENT_IDENTIFIER, genieResponseSendFeedback.getErrorMessages().get(0));


    }


    private void shouldAssertGetFeedbackData(ContentFeedback contentFeedback, ContentFeedback getFeedback) {
        Assert.assertEquals(contentFeedback.getContentId(), getFeedback.getContentId());
        Assert.assertEquals(contentFeedback.getRating(), getFeedback.getRating());
        Assert.assertEquals(contentFeedback.getComments(), getFeedback.getComments());
    }

    /**
     * Should assert the feedback data got in the content details API.
     *
     * @param contentFeedback
     */
    private void shouldAssertSendFeedbackData(ContentFeedback contentFeedback) {

        GenieServiceDBHelper.findContentEntryInDB(CONTENT_ID);

        ContentDetailsRequest.Builder detailsRequest = new ContentDetailsRequest.Builder().forContent(CONTENT_ID).withFeedback();
        GenieResponse<Content> genieResponseDetails = activity.getContentDetails(detailsRequest.build());

        Assert.assertTrue("true", genieResponseDetails.getStatus());
        Assert.assertEquals("worksheet", genieResponseDetails.getResult().getContentType());
        Assert.assertTrue("true", genieResponseDetails.getResult().isAvailableLocally());
        Assert.assertEquals(CONTENT_ID, genieResponseDetails.getResult().getIdentifier());
        Assert.assertNotNull(genieResponseDetails.getResult().getContentFeedback());

        Assert.assertEquals(contentFeedback.getComments(), genieResponseDetails.getResult().getContentFeedback().get(0).getComments());
        Assert.assertEquals(contentFeedback.getContentId(), genieResponseDetails.getResult().getContentFeedback().get(0).getContentId());
        Assert.assertEquals(contentFeedback.getRating(), genieResponseDetails.getResult().getContentFeedback().get(0).getRating());
        Assert.assertNotNull(genieResponseDetails.getResult().getContentFeedback().get(0).getCreatedAt());
    }

    private String createAndSetProfileForGetFeedback() {

        GenieServiceDBHelper.clearProfileTable();
        Profile profile = new Profile("Happy1", "@drawable/ic_avatar2", "en");
        profile.setUid("sampleuid");
        profile.setAge(4);
        profile.setDay(12);
        profile.setMonth(11);

        Profile createdProfile = createNewProfile(profile);

        GenieResponse responseSetCurrentUser = activity.setCurrentUser(createdProfile.getUid());
        Assert.assertTrue(responseSetCurrentUser.getStatus());

        GenieResponse<Profile> responseGetCurrentUser = activity.getCurrentUser();
        Assert.assertTrue(responseGetCurrentUser.getStatus());
        String uid = responseGetCurrentUser.getResult().getUid();

        return uid;
    }

    private void importContent() {
        EcarImportRequest.Builder importRequest = new EcarImportRequest.Builder().fromFilePath(CONTENT_FILEPATH)
                .toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse genieResponse = activity.importEcar(importRequest.build());
        Assert.assertTrue(genieResponse.getStatus());
    }

}
