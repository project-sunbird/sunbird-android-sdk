package org.ekstep.genieservices.contentservice.collectiontest;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentImportRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sneha on 5/30/2017.
 */
@RunWith(AndroidJUnit4.class)
public class CollectionImportWithNChildTest extends GenieServiceTestBase {
    public static final String CHILD_C2_ID = "do_30013486";
    public static final String CHILD_C3_ID = "do_30013497";
    private static final String TAG = CollectionImportWithNChildTest.class.getSimpleName();
    private static final String VISIBILITY_PARENT = "parent";
    private static final String VISIBILITY_DEFAULT = "default";
    final String CONTENT_ID = "do_20045823";
    final String CONTENT_ID_WITH_CHILD = "do_30019820";
    private final String CONTENT_WITH_CHILD_FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/Times_Tables_2_to_10.ecar";
    private final String EMPTY_COLLECTION_FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/Empty_Collection.ecar";

    @Before
    public void setup() throws IOException {
        super.setup();
        activity = rule.getActivity();
        GenieServiceDBHelper.clearContentDBEntry();
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }

    @Test
    public void shouldImportEmptyCollection() {

        String ext = FileUtil.getFileExtension(EMPTY_COLLECTION_FILEPATH);

        ContentImportRequest.Builder contentImportRequest = new ContentImportRequest.Builder().isChildContent(true)
                .fromFilePath(EMPTY_COLLECTION_FILEPATH).toFolder(activity.getExternalFilesDir(null));

        GenieResponse<Void> response = activity.importContent(contentImportRequest.build());

        Assert.assertTrue("true", response.getStatus());
        Assert.assertEquals(ServiceConstants.FileExtension.CONTENT, ext);

        AssertCollection.verifyNoChildContentEntry(CONTENT_ID);
        GenieServiceDBHelper.findContentDBEntry(CONTENT_ID);
    }

    @Test
    public void shouldImportCollectionWithNChild() {

        GenieServiceDBHelper.clearContentDBEntry();

        ContentImportRequest.Builder contentImportRequest = new ContentImportRequest.Builder().isChildContent(true)
                .fromFilePath(CONTENT_WITH_CHILD_FILEPATH).toFolder(activity.getExternalFilesDir(null));

        GenieResponse<Void> response = activity.importContent(contentImportRequest.build());
        Assert.assertTrue(response.getStatus());

        AssertCollection.verifyCollectionEntryAndVisibility(CONTENT_ID_WITH_CHILD, VISIBILITY_DEFAULT);
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

    @Test
    public void shouldGetChildContents() {

        GenieServiceDBHelper.clearContentDBEntry();

        ContentImportRequest.Builder contentImportRequest = new ContentImportRequest.Builder().isChildContent(false)
                .fromFilePath(CONTENT_WITH_CHILD_FILEPATH).toFolder(activity.getExternalFilesDir(null));

        GenieResponse<Void> response = activity.importContent(contentImportRequest.build());

        Log.v(TAG, "status :: " + response.getStatus());
        Assert.assertTrue("true", response.getStatus());
        AssertCollection.verifyCollectionEntryAndVisibility(CONTENT_ID_WITH_CHILD, VISIBILITY_DEFAULT);
        GenieServiceDBHelper.findContentDBEntry(CONTENT_ID_WITH_CHILD);

        GenieResponse<Content> listGenieResponse = activity.getChildContents(CONTENT_ID_WITH_CHILD, 0);
        Assert.assertTrue(listGenieResponse.getStatus());
        Assert.assertNotNull(listGenieResponse.getResult());
        Assert.assertEquals(9, listGenieResponse.getResult().getChildren().size());

        Assert.assertEquals(AssertCollection.CHILD_C2_ID, listGenieResponse.getResult().getChildren().get(0).getIdentifier());
        Assert.assertEquals(AssertCollection.CHILD_C3_ID, listGenieResponse.getResult().getChildren().get(1).getIdentifier());
        Assert.assertEquals(AssertCollection.CHILD_C4_ID, listGenieResponse.getResult().getChildren().get(2).getIdentifier());
        Assert.assertEquals(AssertCollection.CHILD_C5_ID, listGenieResponse.getResult().getChildren().get(3).getIdentifier());
        Assert.assertEquals(AssertCollection.CHILD_C6_ID, listGenieResponse.getResult().getChildren().get(4).getIdentifier());
        Assert.assertEquals(AssertCollection.CHILD_C7_ID, listGenieResponse.getResult().getChildren().get(5).getIdentifier());
        Assert.assertEquals(AssertCollection.CHILD_C8_ID, listGenieResponse.getResult().getChildren().get(6).getIdentifier());
        Assert.assertEquals(AssertCollection.CHILD_C9_ID, listGenieResponse.getResult().getChildren().get(7).getIdentifier());
        Assert.assertEquals(AssertCollection.CHILD_C10_ID, listGenieResponse.getResult().getChildren().get(8).getIdentifier());

    }

    @Test
    public void shouldCheckForNextContent() {
        ContentImportRequest.Builder contentImportRequest = new ContentImportRequest.Builder().isChildContent(true)
                .fromFilePath(CONTENT_WITH_CHILD_FILEPATH).toFolder(activity.getExternalFilesDir(null));

        GenieResponse<Void> response = activity.importContent(contentImportRequest.build());
        Assert.assertTrue("true", response.getStatus());
        AssertCollection.verifyCollectionEntryAndVisibility(CONTENT_ID_WITH_CHILD, VISIBILITY_DEFAULT);

        List<String> identifiers = new ArrayList<>();
        identifiers.add(CONTENT_ID_WITH_CHILD);
        identifiers.add(CHILD_C2_ID);

        GenieResponse<List<Content>> genieResponse = activity.nextContent(identifiers);
        Assert.assertTrue("true", genieResponse.getStatus());
        Assert.assertEquals(2, genieResponse.getResult().size());

        String parentIdentifier = (genieResponse.getResult().get(0)).getIdentifier();
        String childContentIdentifier = (genieResponse.getResult().get(1)).getIdentifier();

        Assert.assertEquals(CONTENT_ID_WITH_CHILD, parentIdentifier);
        Assert.assertEquals(AssertCollection.CHILD_C3_ID, childContentIdentifier);
    }
}
