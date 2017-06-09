package org.ekstep.genieservices.contentfeedback;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentDetailsRequest;
import org.ekstep.genieservices.commons.bean.ContentFeedback;
import org.ekstep.genieservices.commons.bean.ContentFeedbackCriteria;
import org.ekstep.genieservices.commons.bean.ContentImportRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Profile;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Sneha on 6/2/2017.
 */
@RunWith(AndroidJUnit4.class)
public class ContentFeedbackServiceTest extends GenieServiceTestBase {
    private static final String TAG = ContentFeedbackServiceTest.class.getSimpleName();
    final String CONTENT_ID = "do_30013486";
    private final String CONTENT_FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/Multiplication2.ecar";


    @Test
    public void shouldSendFeedbackForContent() {

        GenieServiceDBHelper.clearContentDBEntry();

        String uid = createAndSetProfileForGetFeedback();

        ContentImportRequest.Builder importRequest = new ContentImportRequest.Builder(false).fromFilePath(CONTENT_FILEPATH).toFolder(activity.getExternalFilesDir(null));

        GenieResponse genieResponse = activity.importContent(importRequest.build());
        Assert.assertTrue(genieResponse.getStatus());

        ContentFeedback contentFeedback = new ContentFeedback();
        contentFeedback.setComments("Great content");
        contentFeedback.setContentId(CONTENT_ID);
        contentFeedback.setRating(3);

        GenieResponse genieResponseSendFeedback = activity.sendFeedback(contentFeedback);
        Assert.assertTrue(genieResponseSendFeedback.getStatus());

        shouldAssertFeedbackData(contentFeedback);

        ContentFeedbackCriteria.Builder data = new ContentFeedbackCriteria.Builder(uid, CONTENT_ID);

        GenieResponse<ContentFeedback> genieResponseGetFeedback = activity.getFeedback(data.build());
        Assert.assertTrue(genieResponseGetFeedback.getStatus());
        ContentFeedback getFeedback = genieResponseGetFeedback.getResult();

        assertGetFeedbackData(contentFeedback, getFeedback);

    }

    private void assertGetFeedbackData(ContentFeedback contentFeedback, ContentFeedback getFeedback) {
        Assert.assertEquals(contentFeedback.getContentId(), getFeedback.getContentId());
        Assert.assertEquals(contentFeedback.getRating(), getFeedback.getRating());
        Assert.assertEquals(contentFeedback.getComments(), getFeedback.getComments());
    }

    /**
     * Should assert the feedback data got in the content details API.
     *
     * @param contentFeedback
     */
    private void shouldAssertFeedbackData(ContentFeedback contentFeedback) {

        ContentDetailsRequest.Builder detailsRequest = new ContentDetailsRequest.Builder().contentId(CONTENT_ID);

        GenieResponse<Content> genieResponseDetails = activity.getContentDetails(detailsRequest.build());

        Assert.assertTrue("true", genieResponseDetails.getStatus());
        Assert.assertEquals("worksheet", genieResponseDetails.getResult().getContentType());
        Assert.assertTrue("true", genieResponseDetails.getResult().isAvailableLocally());
        Assert.assertEquals(CONTENT_ID, genieResponseDetails.getResult().getIdentifier());
        Assert.assertNotNull(genieResponseDetails.getResult().getContentFeedback());

        Assert.assertEquals(contentFeedback.getComments(), genieResponseDetails.getResult().getContentFeedback().getComments());
        Assert.assertEquals(contentFeedback.getContentId(), genieResponseDetails.getResult().getContentFeedback().getContentId());
        Assert.assertEquals(contentFeedback.getRating(), genieResponseDetails.getResult().getContentFeedback().getRating());
        Assert.assertNotNull(genieResponseDetails.getResult().getContentFeedback().getCreatedAt());
    }

    private String createAndSetProfileForGetFeedback() {

        GenieServiceDBHelper.clearProfileTable();
        Profile profile = new Profile("Happy1", "@drawable/ic_avatar2", "en");
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

}
