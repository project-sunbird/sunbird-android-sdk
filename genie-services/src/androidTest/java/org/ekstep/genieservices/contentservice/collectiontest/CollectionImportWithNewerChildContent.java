package org.ekstep.genieservices.contentservice.collectiontest;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

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
public class CollectionImportWithNewerChildContent extends GenieServiceTestBase {

    private static final String VISIBILITY_PARENT = "parent";
    private static final String VISIBILITY_DEFAULT = "default";
    private static final String COLLECTION_FILE_PATH = Environment.getExternalStorageDirectory().toString() + "/Download/Times_Tables_2_to_10.ecar";
    private static final String CHILD_CONTENT2_FILE_PATH = Environment.getExternalStorageDirectory().toString() + "/Download/multiplication_2_v1.ecar";

    /**TODO In this test we first,
     * 1. Import older content C1(c1 v1.0) (child version is manually changed/downgraded from v2 to v1)
     * 2. Then Collection C (C1 v2.0)
     * 3. Older content version is not upgrading.
     */
    @Test
    public void testShouldImportContentAndCollection() {

        GenieServiceDBHelper.clearContentDBEntry();

        shouldImportChildContentEcar();

        shouldImportCollectionEcar();
    }

    private void shouldImportChildContentEcar() {

        ContentImportRequest.Builder importRequestBuilder = new ContentImportRequest.Builder(false)
                .fromFilePath(CHILD_CONTENT2_FILE_PATH).toFolder(activity.getExternalFilesDir(null));

        GenieResponse genieResponse = activity.importContent(importRequestBuilder.build());
        Assert.assertTrue("true", genieResponse.getStatus());
        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_CONTENT2_ECAR_ID, VISIBILITY_DEFAULT);
        AssertCollection.verifyContentVersionToBeUpdated(AssertCollection.CHILD_CONTENT2_ECAR_ID, 1.0, 1);

        //remove it later after testing for version
        ContentDetailsRequest.Builder contentId = new ContentDetailsRequest.Builder()
                .contentId(AssertCollection.CHILD_CONTENT2_ECAR_ID);
        GenieResponse<Content> contentDetailsResponse = activity.getContentDetails(contentId.build());
        Assert.assertTrue(contentDetailsResponse.getStatus());
        Log.e("TAG", "content status :: " + contentDetailsResponse.getStatus());
        String updatedVersion = contentDetailsResponse.getResult().getContentData().getPkgVersion();
        Log.e("TAG", "content version :: " + updatedVersion);
    }

    private void shouldImportCollectionEcar() {

        ContentImportRequest.Builder importRequestBuilder = new ContentImportRequest.Builder(false)
                .fromFilePath(COLLECTION_FILE_PATH).toFolder(activity.getExternalFilesDir(null));

        GenieResponse<Void> genieResponse = activity.importContent(importRequestBuilder.build());
        Assert.assertTrue("true", genieResponse.getStatus());
        AssertCollection.verifyCollectionEntryAndVisibility(AssertCollection.COLLECTION_ECAR_ID, VISIBILITY_DEFAULT);
        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_C2_ID, VISIBILITY_DEFAULT);
        AssertCollection.verifyContentVersionToBeUpdated(AssertCollection.CHILD_C2_ID, 2.0, 2);

        ContentDetailsRequest.Builder collectionRequest = new ContentDetailsRequest.Builder()
                .contentId(AssertCollection.COLLECTION_ECAR_ID);
        GenieResponse<Content> collectionDetailsResponse = activity.getContentDetails(collectionRequest.build());
        Assert.assertTrue(collectionDetailsResponse.getStatus());
        Log.e("TAG", "collection status " + collectionDetailsResponse.getStatus());
        String collectionVersion = collectionDetailsResponse.getResult().getContentData().getPkgVersion();
        Log.e("TAG", "collection version " + collectionVersion);

        //updated content check
        ContentDetailsRequest.Builder contentDetailsRequest = new ContentDetailsRequest.Builder()
                .contentId(AssertCollection.CHILD_CONTENT2_ECAR_ID);
        GenieResponse<Content> contentDetailsResponse = activity.getContentDetails(contentDetailsRequest.build());
        Assert.assertTrue(contentDetailsResponse.getStatus());
        Log.e("TAG", "updated content status " + contentDetailsResponse.getStatus());
        String updatedContentVersion = contentDetailsResponse.getResult().getContentData().getPkgVersion();
        Log.e("TAG", "updated content version " + updatedContentVersion);

        Assert.assertEquals(collectionVersion, updatedContentVersion);
    }
}
