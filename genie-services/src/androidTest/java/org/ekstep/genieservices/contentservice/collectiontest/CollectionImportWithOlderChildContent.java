package org.ekstep.genieservices.contentservice.collectiontest;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceTestBase;
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

    /**
     * Import Child Content C1(C1 v4.0 visibility "default")
     * Import Collection C (C1 v3.0)
     * Check Visibility for default and version for 4.0
     */

    @Test
    public void test1ShouldCheckImportAndVisibility() {
        shouldImportChildContentEcar();

        waitForGenieToBecomeIdle();

        shouldImportCollectionEcar();

    }

    private void shouldImportChildContentEcar() {

        GenieResponse<Void> genieResponse = activity.importContent(false, CHILD_CONTENT_FILE_PATH);
        Assert.assertEquals("true", genieResponse.getStatus());
        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_CONTENT_ECAR_ID, VISIBILITY_DEFAULT);

        AssertCollection.verifyContentVersionToBeUpdated(AssertCollection.CHILD_CONTENT_ECAR_ID, 4.0, 1);
    }

    private void shouldImportCollectionEcar() {

        GenieResponse<Void> genieResponse = activity.importContent(true, COLLECTION_FILE_PATH);
        Assert.assertEquals("true", genieResponse.getStatus());
        AssertCollection.verifyCollectionEntryAndVisibility(AssertCollection.COLLECTION_ECAR_ID, VISIBILITY_DEFAULT);
        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_C4_ID, VISIBILITY_DEFAULT);

        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_CONTENT_ECAR_ID, VISIBILITY_DEFAULT);
        AssertCollection.verifyContentVersionToBeUpdated(AssertCollection.CHILD_C4_ID, 4.0, 2);

    }
}
