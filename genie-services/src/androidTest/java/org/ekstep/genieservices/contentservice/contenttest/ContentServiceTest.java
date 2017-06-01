package org.ekstep.genieservices.contentservice.contenttest;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchResult;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.RelatedContentResult;
import org.ekstep.genieservices.commons.bean.enums.ContentType;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Created by Sneha on 5/31/2017.
 */
@RunWith(AndroidJUnit4.class)
public class ContentServiceTest extends GenieServiceTestBase {
    private static final String TAG = ContentServiceTest.class.getSimpleName();

    final String CONTENT_ID = "do_30013486";
    final String CONTENT_ID_WITH_CHILD = "do_30019820";
    private final String CONTENT_FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/Multiplication2.ecar";
    private final String CONTENT_WITH_CHILD_FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/Times_Tables_2_to_10.ecar";


    @Test
    public void shouldImportContentHavingNoChild() {

        String ext = FileUtil.getFileExtension(CONTENT_FILEPATH);

        GenieResponse<Void> response = activity.importContent(false, CONTENT_FILEPATH);
        Log.v(TAG, "genieresponse :: " + response.getStatus() + "errormsg :: " + response.getErrorMessages().get(0));

        Assert.assertNotNull(response.getResult());
        Assert.assertTrue("true", response.getStatus());
        Assert.assertEquals(ServiceConstants.FileExtension.CONTENT, ext);

        //TODO : uncomment when telemetry events are implemented in the content service.
//        AssertImportTelemetryEvent.verifyGEInteractIsLoggedForContentImportInitiated("GE_INTERACT");
//        AssertImportTelemetryEvent.verifyGeTransferIsLoggedForContentImport("GE_TRANSFER");
//        AssertImportTelemetryEvent.verifyGEInteractIsLoggedForContentImportSuccess("GE_INTERACT");
        GenieServiceDBHelper.findContentDBEntry(CONTENT_ID);

    }

    @Test
    public void shouldImportContentHavingChild() {

        String ext = FileUtil.getFileExtension(CONTENT_WITH_CHILD_FILEPATH);

        GenieResponse<Void> genieResponse = activity.importContent(true, CONTENT_WITH_CHILD_FILEPATH);
        Log.v(TAG, "genieresponse :: " + genieResponse.getStatus());

        Assert.assertNotNull(genieResponse.getResult());
        Assert.assertTrue("true", genieResponse.getStatus());
        Assert.assertEquals(ServiceConstants.FileExtension.CONTENT, ext);

        //TODO : uncomment when telemetry events are implemented in the content service.
//        AssertImportTelemetryEvent.verifyGEInteractIsLoggedForContentImportInitiated("GE_INTERACT");
//        AssertImportTelemetryEvent.verifyGeTransferIsLoggedForContentImport("GE_TRANSFER");
//        AssertImportTelemetryEvent.verifyGEInteractIsLoggedForContentImportSuccess("GE_INTERACT");

        GenieServiceDBHelper.findContentDBEntry(CONTENT_ID_WITH_CHILD);
    }

    @Test
    public void shouldGetContentDetails() {

        GenieResponse<Content> genieResponseDetails = activity.getContentDetails(CONTENT_ID);

        Assert.assertNotNull(genieResponseDetails.getResult());
        Assert.assertTrue(genieResponseDetails.getResult().isAvailableLocally());
        Assert.assertEquals("worksheet", genieResponseDetails.getResult().getContentType());
        Assert.assertEquals(CONTENT_ID, genieResponseDetails.getResult().getIdentifier());
        Assert.assertEquals(1, genieResponseDetails.getResult().getReferenceCount());
    }

    /**
     * When the ContentCriteria is null.
     * Note :: Though collection is imported the result comes as null for get local contents.
     */
    @Test
    public void shouldGetAllLocalContent() {

        ContentCriteria contentCriteria = new ContentCriteria();
        contentCriteria.setContentTypes(new ContentType[]{ContentType.COLLECTION});

        GenieResponse<List<Content>> genieResponse = activity.getAllLocalContent(contentCriteria);
        Assert.assertTrue(genieResponse.getStatus());
    }

    /**
     * When the ContentCriteria is not passed
     * Note :: Though collection is imported the result comes as null for get local contents.
     */
    @Test
    public void shouldGetAllLocalContentForNoContentCriteria() {
        GenieResponse<List<Content>> genieResponse = activity.getAllLocalContent(null);
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(genieResponse.getResult());
    }

    /**
     * Note :: In the result getrecommended list comes as null.
     * Note :: Any other assertions.
     */
    @Test
    public void shouldGetAllRecommendedContent() {
        GenieResponse<ContentSearchResult> genieResponse = activity.getRecommendedContent("en");
        Assert.assertNotNull(genieResponse.getResult());
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertEquals("ekstep.analytics.recommendations", genieResponse.getResult().getId());
    }

    /**
     * Note :: Any other assertions.
     */
    @Test
    public void shouldGetRelatedContent() {
        GenieResponse<RelatedContentResult> genieResponse = activity.getRelatedContent(CONTENT_ID);
        Assert.assertNotNull(genieResponse.getResult());
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertEquals("ekstep.analytics.recommendations", genieResponse.getResult().getId());
    }
}
