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
public class CollectionImportWithNChildTest extends GenieServiceTestBase {
    private static final String TAG = CollectionImportWithNChildTest.class.getSimpleName();

    private static final String VISIBILITY_PARENT = "parent";
    private static final String VISIBILITY_DEFAULT = "default";
    final String CONTENT_ID = "do_20045823";
    final String CONTENT_ID_WITH_CHILD = "do_30019820";
    private final String CONTENT_WITH_CHILD_FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/Times_Tables_2_to_10.ecar";
    private final String EMPTY_COLLECTION_FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/Empty_Collection.ecar";

    @Test
    public void shouldImportEmptyCollection() {

        //check clearContentDBEntry() is correct.
        GenieServiceDBHelper.clearContentDBEntry();

        String ext = FileUtil.getFileExtension(EMPTY_COLLECTION_FILEPATH);

        GenieResponse<Void> response = activity.importContent(true, EMPTY_COLLECTION_FILEPATH,activity.getExternalFilesDir(null));

        Assert.assertNotNull(response.getResult());
        Assert.assertTrue("true", response.getStatus());
        Assert.assertEquals(ServiceConstants.FileExtension.CONTENT, ext);

        AssertCollection.verifyEmptyCollectionEntry(CONTENT_ID);
        GenieServiceDBHelper.findContentDBEntry(CONTENT_ID);
    }

    @Test
    public void shouldImportCollectionWithNChild() {

        GenieServiceDBHelper.clearContentDBEntry();

        GenieResponse<Void> response = activity.importContent(true, CONTENT_WITH_CHILD_FILEPATH,activity.getExternalFilesDir(null));
        Assert.assertEquals("true", response.getStatus());

        GenieServiceDBHelper.findContentDBEntry(CONTENT_ID_WITH_CHILD);

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

    }
}
