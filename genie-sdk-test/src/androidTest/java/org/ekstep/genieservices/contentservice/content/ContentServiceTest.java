package org.ekstep.genieservices.contentservice.content;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.SampleResponse;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentDelete;
import org.ekstep.genieservices.commons.bean.ContentDeleteRequest;
import org.ekstep.genieservices.commons.bean.ContentDeleteResponse;
import org.ekstep.genieservices.commons.bean.ContentFilterCriteria;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.EcarImportRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.RecommendedContentRequest;
import org.ekstep.genieservices.commons.bean.RecommendedContentResult;
import org.ekstep.genieservices.commons.bean.RelatedContentRequest;
import org.ekstep.genieservices.commons.bean.RelatedContentResult;
import org.ekstep.genieservices.contentservice.collection.AssertCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.List;

/**
 * Created on 5/31/2017.
 *
 * @author Sneha
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class ContentServiceTest extends GenieServiceTestBase {

    private static final String VISIBILITY_DEFAULT = "default";
    private final String CONTENT_ID = "do_30013486";
    private final String CONTENT_ID_WITH_CHILD = "do_30019820";
    private final String CONTENT_FILEPATH = DESTINATION + "/Multiplication2.ecar";
    private final String CONTENT_WITH_CHILD_FILEPATH = DESTINATION + "/Times_Tables_2_to_10.ecar";

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


    /**
     * Import collection and import a content which is a part collection.
     * Content list size has to be 1.
     * As the content is a part of collection.
     */
    @Test
    public void test6shouldGetAllLocalContent() {
        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder().fromFilePath(CONTENT_WITH_CHILD_FILEPATH).toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse<List<ContentImportResponse>> response = activity.importEcar(ecarImportRequest.build());
        Assert.assertTrue("true", response.getStatus());
        AssertCollection.verifyCollectionEntryAndVisibility(CONTENT_ID_WITH_CHILD, VISIBILITY_DEFAULT);

        EcarImportRequest.Builder contentImportRequest = new EcarImportRequest.Builder().fromFilePath(CONTENT_FILEPATH).toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse<List<ContentImportResponse>> genieResponse = activity.importEcar(contentImportRequest.build());
        Assert.assertTrue("true", genieResponse.getStatus());
        AssertCollection.verifyContentEntryAndVisibility(CONTENT_ID, VISIBILITY_DEFAULT);

        ContentFilterCriteria.Builder contentFilterCriteria = new ContentFilterCriteria.Builder().contentTypes(new String[]{"Worksheet", "Collection"}).withContentAccess().withFeedback();
        GenieResponse<List<Content>> genieGetLocalResponse = activity.getAllLocalContent(contentFilterCriteria.build());
        Assert.assertTrue(genieGetLocalResponse.getStatus());
        Assert.assertNotNull(genieGetLocalResponse.getResult());
        Assert.assertEquals(2, genieGetLocalResponse.getResult().size());
    }

    /**
     * When the ContentCriteria is not passed
     * Note :: Though collection is imported the result comes as null for get local contents.
     */
    @Test
    public void test7shouldGetAllLocalContent() {
        GenieResponse<List<Content>> genieResponse = activity.getAllLocalContent(null);
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(genieResponse.getResult());
    }

    @Test
    public void test81shouldGetAllRecommendedContent() {

        startMockServer();

        mMockServer.mockHttpResponse(SampleResponse.getRecommendedContent(), 200);

        RecommendedContentRequest.Builder contentRequest = new RecommendedContentRequest.Builder().byLanguage("en");

        GenieResponse<RecommendedContentResult> genieResponse = activity.getRecommendedContent(contentRequest.build());
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertEquals("ekstep.analytics.recommendations", genieResponse.getResult().getId());

        shutDownMockServer();
    }

    @Test
    public void test82shouldReturnErrorResponseFor404ResponseInRecommendedContentAPI() {

        startMockServer();

        mMockServer.mockHttpResponse(SampleResponse.getRecommendedContent(), 400);

        RecommendedContentRequest.Builder contentRequest = new RecommendedContentRequest.Builder().byLanguage("en");

        GenieResponse<RecommendedContentResult> genieResponse = activity.getRecommendedContent(contentRequest.build());
        Assert.assertFalse(genieResponse.getStatus());

        shutDownMockServer();
    }

    @Test
    public void test91shouldGetRelatedContent() {

        startMockServer();

        mMockServer.mockHttpResponse(SampleResponse.getRecommendedContent(), 200);

        RelatedContentRequest.Builder contentRequest = new RelatedContentRequest.Builder().forContent(CONTENT_ID);

        GenieResponse<RelatedContentResult> genieResponse = activity.getRelatedContent(contentRequest.build());
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertEquals("ekstep.analytics.recommendations", genieResponse.getResult().getId());
        shutDownMockServer();
    }

    @Test
    public void test92shouldReturnErrorResponseFor404ResponseInRelatedContentAPI() {

        startMockServer();

        mMockServer.mockHttpResponse(SampleResponse.getRecommendedContent(), 400);

        RelatedContentRequest.Builder contentRequest = new RelatedContentRequest.Builder().forContent(CONTENT_ID);

        GenieResponse<RelatedContentResult> genieResponse = activity.getRelatedContent(contentRequest.build());
        Assert.assertFalse(genieResponse.getStatus());

        shutDownMockServer();
    }

    @Test
    public void test10shouldDeleteContent() {
        EcarImportRequest.Builder importRequest = new EcarImportRequest.Builder()
                .fromFilePath(CONTENT_FILEPATH).toFolder(activity.getExternalFilesDir(null).toString());

        GenieResponse<List<ContentImportResponse>> response = activity.importEcar(importRequest.build());
        Assert.assertTrue("true", response.getStatus());

        ContentDeleteRequest.Builder detailsRequest = new ContentDeleteRequest.Builder().add(new ContentDelete(CONTENT_ID, false));

        GenieResponse genieResponse = activity.deleteContent(detailsRequest.build());
        Assert.assertTrue(genieResponse.getStatus());
        AssertCollection.verifyContentIsDeleted(CONTENT_ID, activity, CONTENT_FILEPATH);
    }

    @Test
    public void test11shouldValidateDeleteContent() {

        GenieServiceDBHelper.findContentEntryInDB(CONTENT_ID);

        EcarImportRequest.Builder importRequest = new EcarImportRequest.Builder()
                .fromFilePath(CONTENT_FILEPATH)
                .toFolder(activity.getExternalFilesDir(null).toString());

        GenieResponse<List<ContentImportResponse>> response = activity.importEcar(importRequest.build());
        Assert.assertTrue(response.getStatus());

        ContentDeleteRequest.Builder detailsRequest = new ContentDeleteRequest.Builder().add(new ContentDelete(CONTENT_ID_WITH_CHILD, false));

        GenieResponse<List<ContentDeleteResponse>> genieResponse = activity.deleteContent(detailsRequest.build());
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertEquals(-1, genieResponse.getResult().get(0).getStatus().getValue());
    }


}
