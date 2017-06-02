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
public class CollectionImportWithNewerChildContent extends GenieServiceTestBase {

    private static final String VISIBILITY_PARENT = "parent";
    private static final String VISIBILITY_DEFAULT = "default";
    private static final String COLLECTION_FILE_PATH = Environment.getExternalStorageDirectory().toString() + "/Download/Times_Tables_2_to_10.ecar";
    private static final String CHILD_CONTENT2_FILE_PATH = Environment.getExternalStorageDirectory().toString() + "/Download/Multiplication2.ecar";

    /**
     * In this test we first import older content C1(c1 v2.0) then Collection C (C1 v 3.0)
     */
    @Test
    public void testShouldImportContentAndCollection() {

        shouldImportChildContentEcar();

        shouldImportCollectionEcar();
    }

    private void shouldImportChildContentEcar() {

        GenieResponse genieResponse = activity.importContent(false, CHILD_CONTENT2_FILE_PATH,activity.getExternalFilesDir(null));
        Assert.assertEquals("true", genieResponse.getStatus());
        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_CONTENT2_ECAR_ID, VISIBILITY_DEFAULT);

        AssertCollection.verifyContentVersionToBeUpdated(AssertCollection.CHILD_CONTENT2_ECAR_ID, 2.0, 1);
    }

    private void shouldImportCollectionEcar() {

        GenieResponse<Void> genieResponse = activity.importContent(true, COLLECTION_FILE_PATH,activity.getExternalFilesDir(null));
        Assert.assertEquals("true", genieResponse.getStatus());
        AssertCollection.verifyCollectionEntryAndVisibility(AssertCollection.COLLECTION_ECAR_ID, VISIBILITY_DEFAULT);
        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_C2_ID, VISIBILITY_DEFAULT);

        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_CONTENT2_ECAR_ID, VISIBILITY_DEFAULT);
        AssertCollection.verifyContentVersionToBeUpdated(AssertCollection.CHILD_C2_ID, 3.0, 2);
    }
}
