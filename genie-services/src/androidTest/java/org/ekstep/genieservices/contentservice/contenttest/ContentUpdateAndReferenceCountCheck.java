package org.ekstep.genieservices.contentservice.contenttest;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.ekstep.genieservices.EcarCopyUtil;
import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentDeleteRequest;
import org.ekstep.genieservices.commons.bean.ContentDetailsRequest;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.EcarImportRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.contentservice.collectiontest.AssertCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sneha on 6/8/2017.
 */
@RunWith(AndroidJUnit4.class)
public class ContentUpdateAndReferenceCountCheck extends GenieServiceTestBase {

    private static final String COLLECTION_FILE_PATH = Environment.getExternalStorageDirectory().toString() + "/Download/Times_Tables_2_to_10.ecar";
    private static final String CHILD_CONTENT_FILE_PATH = Environment.getExternalStorageDirectory().toString() + "/Download/Multiplication4.ecar";

    private static final String COLLECTION_ASSET_PATH = "Download/Times_Tables_2_to_10.ecar";
    private static final String CHILD_CONTENT_ASSET_PATH = "Download/Multiplication4.ecar";

    /**
     * Import collection.
     * Import a content of collection as a separate entity. Reference of the content will be 2.
     * Delete the collection content.
     * Check the referenceCount of the content. It will be one
     */

    @Before
    public void setup() throws IOException {
        super.setup();
        activity = rule.getActivity();
        EcarCopyUtil.createFileFromAsset(activity.getApplicationContext(), COLLECTION_ASSET_PATH, DESTINATION);
        EcarCopyUtil.createFileFromAsset(activity.getApplicationContext(), CHILD_CONTENT_ASSET_PATH, DESTINATION);
        GenieServiceDBHelper.clearEcarEntryFromDB();
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }

    @Test
    public void test1ShouldCheckContentUpdate() {
        GenieServiceDBHelper.clearEcarEntryFromDB();

        //import collection
        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder().fromFilePath(COLLECTION_FILE_PATH).toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse<List<ContentImportResponse>> genieImportResponse = activity.importEcar(ecarImportRequest.build());
        Assert.assertTrue(genieImportResponse.getStatus());

        //check for reference count for c4 child
        ContentDetailsRequest.Builder contentDetailReuqest = new ContentDetailsRequest.Builder().forContent(AssertCollection.CHILD_C4_ID);
        GenieResponse<Content> detailsResponse = activity.getContentDetails(contentDetailReuqest.build());
        Assert.assertTrue(detailsResponse.getStatus());
        Assert.assertEquals(1, detailsResponse.getResult().getReferenceCount());

        //import newer version of c4 content.
        EcarImportRequest.Builder ecarImportRequest2 = new EcarImportRequest.Builder().fromFilePath(CHILD_CONTENT_FILE_PATH).toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse<List<ContentImportResponse>> genieResponse = activity.importEcar(ecarImportRequest2.build());
        Assert.assertTrue(genieResponse.getStatus());

        //check the reference count
        ContentDetailsRequest.Builder contentId = new ContentDetailsRequest.Builder().forContent(AssertCollection.CHILD_CONTENT_ECAR_ID);
        GenieResponse<Content> contentDetailsResponse = activity.getContentDetails(contentId.build());
        Assert.assertTrue(contentDetailsResponse.getStatus());
        Assert.assertEquals(2, contentDetailsResponse.getResult().getReferenceCount());

        //delete content
        ContentDeleteRequest.Builder deleteRequest = new ContentDeleteRequest.Builder().contentId(AssertCollection.CHILD_C4_ID).isChildContent();
        GenieResponse deleteResponse = activity.deleteContent(deleteRequest.build());
        Assert.assertTrue(deleteResponse.getStatus());

        //check the reference count after deleting
        ContentDetailsRequest.Builder contentDetails = new ContentDetailsRequest.Builder().forContent(AssertCollection.CHILD_CONTENT_ECAR_ID);
        GenieResponse<Content> detailResponse = activity.getContentDetails(contentDetails.build());
        Assert.assertTrue(detailResponse.getStatus());
        Assert.assertEquals(1, detailResponse.getResult().getReferenceCount());
    }
}
