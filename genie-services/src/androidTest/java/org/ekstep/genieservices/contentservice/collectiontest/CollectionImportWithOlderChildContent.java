package org.ekstep.genieservices.contentservice.collectiontest;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentDetailsRequest;
import org.ekstep.genieservices.commons.bean.ContentImportRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Sneha on 5/30/2017.
 */
@RunWith(AndroidJUnit4.class)
public class CollectionImportWithOlderChildContent extends GenieServiceTestBase {

    private static final String VISIBILITY_PARENT = "parent";
    private static final String VISIBILITY_DEFAULT = "default";
    private static final String COLLECTION_FILE_PATH = Environment.getExternalStorageDirectory().toString() + "/Download/Times_Tables_2_to_10.ecar";
    private static final String CHILD_CONTENT_FILE_PATH = Environment.getExternalStorageDirectory().toString() + "/Download/Multiplication4.ecar";
    private String CONTENT_VERSION;

    /**
     * Import Child Content C1(C1 v4.0 visibility "default")
     * Import Collection C (C1 v3.0)
     * Check Visibility for default and version for 4.0
     */

    @Test
    public void test1ShouldCheckImportAndVisibility() {

        GenieServiceDBHelper.clearContentDBEntry();

        shouldImportChildContentEcar();

        shouldImportCollectionEcar();

    }

    private void shouldImportChildContentEcar() {

        ContentImportRequest.Builder importRequestBuilder = new ContentImportRequest.Builder().isChildContent(false)
                .fromFilePath(CHILD_CONTENT_FILE_PATH).toFolder(activity.getExternalFilesDir(null));

        //import newer version content.
        GenieResponse<Void> genieResponse = activity.importContent(importRequestBuilder.build());
        Assert.assertTrue("true", genieResponse.getStatus());
        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_CONTENT_ECAR_ID, VISIBILITY_DEFAULT);
        AssertCollection.verifyContentVersionToBeUpdated(AssertCollection.CHILD_CONTENT_ECAR_ID, 4.0, 1);

        ContentDetailsRequest.Builder contentId = new ContentDetailsRequest.Builder()
                .contentId(AssertCollection.CHILD_CONTENT_ECAR_ID);
        GenieResponse<Content> contentDetailsResponse = activity.getContentDetails(contentId.build());
        Assert.assertTrue(contentDetailsResponse.getStatus());
        CONTENT_VERSION = contentDetailsResponse.getResult().getContentData().getPkgVersion();
    }

    private void shouldImportCollectionEcar() {

        ContentImportRequest.Builder importRequestBuilder = new ContentImportRequest.Builder().isChildContent(false)
                .fromFilePath(COLLECTION_FILE_PATH).toFolder(activity.getExternalFilesDir(null));

        //import collection
        GenieResponse<Void> genieResponse = activity.importContent(importRequestBuilder.build());
        Assert.assertTrue(genieResponse.getStatus());
        AssertCollection.verifyCollectionEntryAndVisibility(AssertCollection.COLLECTION_ECAR_ID, VISIBILITY_DEFAULT);
        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_C4_ID, VISIBILITY_DEFAULT);

        //check if content version is updated to 4.0
        ContentDetailsRequest.Builder contentDetailsRequest = new ContentDetailsRequest.Builder()
                .contentId(AssertCollection.CHILD_C4_ID);
        GenieResponse<Content> contentDetailsResponse = activity.getContentDetails(contentDetailsRequest.build());
        Assert.assertTrue(contentDetailsResponse.getStatus());
        String updatedContentVersion = contentDetailsResponse.getResult().getContentData().getPkgVersion();

        Assert.assertEquals(CONTENT_VERSION, updatedContentVersion);

    }
}
