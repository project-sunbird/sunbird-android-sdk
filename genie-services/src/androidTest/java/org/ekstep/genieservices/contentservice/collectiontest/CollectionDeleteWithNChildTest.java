package org.ekstep.genieservices.contentservice.collectiontest;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Sneha on 5/30/2017.
 */

@RunWith(AndroidJUnit4.class)
public class CollectionDeleteWithNChildTest extends GenieServiceTestBase {

    private static final String VISIBILITY_PARENT = "parent";
    private static final String VISIBILITY_DEFAULT = "default";
    private static final String CONTENT_ID = "do_30019820";
    private static final String COLLECTION_FILE_PATH = Environment.getExternalStorageDirectory().toString() + "/Download/Times_Tables_2_to_10.ecar";


    @Test
    public void shouldDeleteCollectionWithNChildTest() {

        GenieServiceDBHelper.clearContentDBEntry();
        String ext = FileUtil.getFileExtension(COLLECTION_FILE_PATH);

        GenieResponse response = activity.importContent(true, COLLECTION_FILE_PATH,activity.getExternalFilesDir(null));
        Assert.assertTrue("true", response.getStatus());
        Assert.assertEquals(ServiceConstants.FileExtension.CONTENT, ext);

        GenieServiceDBHelper.findContentDBEntry(CONTENT_ID);

        AssertCollection.verifyCollectionEntryAndVisibility(AssertCollection.COLLECTION_ECAR_ID, VISIBILITY_DEFAULT);
        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_C2_ID, VISIBILITY_PARENT);
        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_C3_ID, VISIBILITY_PARENT);
        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_C4_ID, VISIBILITY_PARENT);
        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_C5_ID, VISIBILITY_PARENT);
        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_C6_ID, VISIBILITY_PARENT);
        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_C7_ID, VISIBILITY_PARENT);
        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_C8_ID, VISIBILITY_PARENT);
        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_C9_ID, VISIBILITY_PARENT);
        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_C10_ID, VISIBILITY_PARENT);

        GenieResponse genieResponse = activity.deleteContent(CONTENT_ID, 1);
        Assert.assertTrue("true", genieResponse.getStatus());
        AssertCollection.verifyContentIsDeleted(CONTENT_ID, activity, COLLECTION_FILE_PATH);

    }
}
