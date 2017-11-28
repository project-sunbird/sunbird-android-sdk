package org.ekstep.genieservices.contentservice.content;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.ekstep.genieservices.EcarCopyUtil;
import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.MockServer;
import org.ekstep.genieservices.SampleResponse;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentDetailsRequest;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.EcarImportRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.List;

/**
 * Created by swayangjit on 8/9/17.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class ContentDetailsAPITest extends GenieServiceTestBase {

    private final String CONTENT_FILE_PATH = DESTINATION + "/Multiplication2.ecar";
    private final String CONTENT_ASSET_PATH = "Download/Multiplication2.ecar";
    private final String CONTENT_ID = "do_30013486";

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
    public void _0ShouldCopyUnneccessoryFoles() {
        EcarCopyUtil.createFileFromAsset(activity, CONTENT_ASSET_PATH, DESTINATION);
    }

    @Test
    public void _1ShouldInvokeCobtentDetialsAPI() {

        GenieServiceDBHelper.clearContentEntryFromDB();

        EcarImportRequest.Builder ecarImportReuqest = new EcarImportRequest.Builder().fromFilePath(CONTENT_FILE_PATH).toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse<List<ContentImportResponse>> response = activity.importEcar(ecarImportReuqest.build());
        Assert.assertTrue("true", response.getStatus());
        Assert.assertNull(response.getError());

        ContentDetailsRequest.Builder contentDetailsRequest = new ContentDetailsRequest.Builder().forContent(CONTENT_ID);

        GenieResponse<Content> genieResponseDetails = activity.getContentDetails(contentDetailsRequest.build());
        Assert.assertNotNull(genieResponseDetails.getResult());

        Assert.assertTrue("true", genieResponseDetails.getResult().isAvailableLocally());
        Assert.assertEquals("worksheet", genieResponseDetails.getResult().getContentType());
        Assert.assertEquals(CONTENT_ID, genieResponseDetails.getResult().getIdentifier());
        Assert.assertEquals(1, genieResponseDetails.getResult().getReferenceCount());
        shutDownMockServer();
    }


    /**
     * For the first time when there is no content details data in the DB,
     * content details is fetched from server
     * <p>
     * For the next time it's fetched from the db.
     */
    @Test
    public void _2ShouldInvokeServerAPIForContentDetails() {
        GenieServiceDBHelper.clearContentEntryFromDB();
        startMockServer();
        mMockServer.mockHttpResponse(SampleResponse.getContentDetailsResponse(), 200);
        ContentDetailsRequest.Builder contentDetailsRequest = new ContentDetailsRequest.Builder().forContent(CONTENT_ID).withContentAccess().withFeedback();
        GenieResponse<Content> genieResponseDetails = activity.getContentDetails(contentDetailsRequest.build());
        mMockServer.assertRequestCount(1);
        Assert.assertTrue(genieResponseDetails.getStatus());
        Assert.assertEquals(null, genieResponseDetails.getResult().getBasePath());
        Assert.assertEquals(CONTENT_ID, genieResponseDetails.getResult().getIdentifier());
        shutDownMockServer();
    }

    /**
     * After the content details is fetched from the server for the first time,
     * next calls will fetch the details from the DB.
     */
    @Test
    public void _3ShouldGetResultFromDB() {
        mMockServer = new MockServer();
        EcarImportRequest.Builder ecarImportReuqest = new EcarImportRequest.Builder().fromFilePath(CONTENT_FILE_PATH).toFolder(activity.getExternalFilesDir(null).toString());
        activity.importEcar(ecarImportReuqest.build());
        ContentDetailsRequest.Builder contentDetailsRequest = new ContentDetailsRequest.Builder().forContent(CONTENT_ID).withFeedback().withContentAccess();
        GenieResponse<Content> genieResponseDetails = activity.getContentDetails(contentDetailsRequest.build());
        Assert.assertTrue(genieResponseDetails.getStatus());
        mMockServer.assertRequestCount(0);

    }

    @Test
    public void _4ShouldInvokeServerAPIIfAttributeisSet() throws InterruptedException {
        startMockServer();
        mMockServer.mockHttpResponse(SampleResponse.getContentDetailsResponse(), 200);
        EcarImportRequest.Builder ecarImportReuqest = new EcarImportRequest.Builder().fromFilePath(CONTENT_FILE_PATH).toFolder(activity.getExternalFilesDir(null).toString());
        activity.importEcar(ecarImportReuqest.build());
        ContentDetailsRequest.Builder contentDetailsRequest = new ContentDetailsRequest.Builder().forContent(CONTENT_ID).refreshContentDetailsFromServer();
        GenieResponse<Content> genieResponseDetails = activity.getContentDetails(contentDetailsRequest.build());
        Assert.assertTrue(genieResponseDetails.getStatus());
        Thread.sleep(5000);
        mMockServer.assertRequestCount(1);
        shutDownMockServer();
    }

    @Test
    public void _5ShouldGiveNoDataFoundError() throws InterruptedException {
        startMockServer();
        mMockServer.mockHttpResponse(SampleResponse.getContentDetailsResponse(), 400);
        ContentDetailsRequest.Builder contentDetailsRequest = new ContentDetailsRequest.Builder().forContent(CONTENT_ID).refreshContentDetailsFromServer();
        GenieResponse<Content> genieResponseDetails = activity.getContentDetails(contentDetailsRequest.build());
        Assert.assertFalse(genieResponseDetails.getStatus());
        Assert.assertEquals(ServiceConstants.ErrorCode.NO_DATA_FOUND, genieResponseDetails.getError());
        Assert.assertEquals(ServiceConstants.ErrorMessage.CONTENT_NOT_FOUND + CONTENT_ID, genieResponseDetails.getErrorMessages().get(0));
        shutDownMockServer();
    }
}
