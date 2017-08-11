package org.ekstep.genieservices.contentservice.collectiontest;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.ekstep.genieservices.EcarCopyUtil;
import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.ChildContentRequest;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.EcarImportRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.HierarchyInfo;
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
    private static final String COLLECTION_ASSET_PATH = "Download/Times_Tables_2_to_10.ecar";
    private static final String EMPTY_COLLECTION_ASSET_PATH = "Download/Empty_Collection.ecar";
    final String CONTENT_ID = "do_20045823";
    final String CONTENT_ID_WITH_CHILD = "do_30019820";
    private final String CONTENT_WITH_CHILD_FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/Times_Tables_2_to_10.ecar";
    private final String EMPTY_COLLECTION_FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/Empty_Collection.ecar";

    @Before
    public void setup() throws IOException {
        super.setup();
        activity = rule.getActivity();
        EcarCopyUtil.createFileFromAsset(activity.getApplicationContext(), COLLECTION_ASSET_PATH, DESTINATION);
        EcarCopyUtil.createFileFromAsset(activity.getApplicationContext(), EMPTY_COLLECTION_ASSET_PATH, DESTINATION);
        GenieServiceDBHelper.clearEcarEntryFromDB();
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }

    @Test
    public void shouldImportEmptyCollection() {

        String ext = FileUtil.getFileExtension(EMPTY_COLLECTION_FILEPATH);

        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder()
                .fromFilePath(EMPTY_COLLECTION_FILEPATH).toFolder(activity.getExternalFilesDir(null).toString());

        GenieResponse<List<ContentImportResponse>> response = activity.importEcar(ecarImportRequest.build());

        Assert.assertTrue(response.getStatus());
        Assert.assertEquals(ServiceConstants.FileExtension.CONTENT, ext);

        AssertCollection.verifyNoChildContentEntry(CONTENT_ID);
        GenieServiceDBHelper.findEcarDBEntry(CONTENT_ID);
    }

    @Test
    public void shouldImportCollectionWithNChild() {

        GenieServiceDBHelper.clearEcarEntryFromDB();

        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder().isChildContent()
                .fromFilePath(CONTENT_WITH_CHILD_FILEPATH).toFolder(activity.getExternalFilesDir(null).toString());

        GenieResponse<List<ContentImportResponse>> response = activity.importEcar(ecarImportRequest.build());
        Assert.assertTrue(response.getStatus());

        AssertCollection.verifyCollectionEntryAndVisibility(CONTENT_ID_WITH_CHILD, VISIBILITY_DEFAULT);
        GenieServiceDBHelper.findEcarDBEntry(CONTENT_ID_WITH_CHILD);

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

        GenieServiceDBHelper.clearEcarEntryFromDB();

        EcarImportRequest.Builder contentImportRequest = new EcarImportRequest.Builder()
                .fromFilePath(CONTENT_WITH_CHILD_FILEPATH).toFolder(activity.getExternalFilesDir(null).toString());

        GenieResponse<List<ContentImportResponse>> response = activity.importEcar(contentImportRequest.build());

        Assert.assertTrue("true", response.getStatus());
        AssertCollection.verifyCollectionEntryAndVisibility(CONTENT_ID_WITH_CHILD, VISIBILITY_DEFAULT);
        GenieServiceDBHelper.findEcarDBEntry(CONTENT_ID_WITH_CHILD);

        ChildContentRequest.Builder childContentRequest = new ChildContentRequest.Builder().forContent(CONTENT_ID_WITH_CHILD);
        GenieResponse<Content> content = activity.getChildContents(childContentRequest.build());
        Assert.assertTrue(content.getStatus());
        Assert.assertNotNull(content.getResult());
        Assert.assertEquals(9, content.getResult().getChildren().size());

        List<Content> childList = content.getResult().getChildren();
        Assert.assertEquals(AssertCollection.CHILD_C2_ID, childList.get(0).getIdentifier());
        Assert.assertEquals(AssertCollection.CHILD_C3_ID, childList.get(1).getIdentifier());
        Assert.assertEquals(AssertCollection.CHILD_C4_ID, childList.get(2).getIdentifier());
        Assert.assertEquals(AssertCollection.CHILD_C5_ID, childList.get(3).getIdentifier());
        Assert.assertEquals(AssertCollection.CHILD_C6_ID, childList.get(4).getIdentifier());
        Assert.assertEquals(AssertCollection.CHILD_C7_ID, childList.get(5).getIdentifier());
        Assert.assertEquals(AssertCollection.CHILD_C8_ID, childList.get(6).getIdentifier());
        Assert.assertEquals(AssertCollection.CHILD_C9_ID, childList.get(7).getIdentifier());
        Assert.assertEquals(AssertCollection.CHILD_C10_ID, childList.get(8).getIdentifier());

        //assert for the parent in hierarcy
        List<HierarchyInfo> hierarchyInfoList = content.getResult().getChildren().get(0).getHierarchyInfo();
        Assert.assertEquals(1, hierarchyInfoList.size());
        Assert.assertEquals(CONTENT_ID_WITH_CHILD, hierarchyInfoList.get(0).getIdentifier());
        Assert.assertEquals(content.getResult().getContentType(), hierarchyInfoList.get(0).getContentType());
    }

    @Test
    public void shouldCheckForNextContent() {
        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder().isChildContent()
                .fromFilePath(CONTENT_WITH_CHILD_FILEPATH).toFolder(activity.getExternalFilesDir(null).toString());

        GenieResponse<List<ContentImportResponse>> response = activity.importEcar(ecarImportRequest.build());
        Assert.assertTrue("true", response.getStatus());
        AssertCollection.verifyCollectionEntryAndVisibility(CONTENT_ID_WITH_CHILD, VISIBILITY_DEFAULT);

        List<String> identifiers = new ArrayList<>();
        identifiers.add(CONTENT_ID_WITH_CHILD);
        identifiers.add(CHILD_C2_ID);

        GenieResponse<Content> genieResponse = activity.nextContent(new ArrayList<HierarchyInfo>(), CHILD_C2_ID);
        Assert.assertTrue("true", genieResponse.getStatus());
        // TODO: 7/12/2017 - needs to revisit
//        Assert.assertEquals(2, genieResponse.getResult().size());

//        String parentIdentifier = (genieResponse.getResult().get(0)).getIdentifier();
//        String childContentIdentifier = (genieResponse.getResult().get(1)).getIdentifier();

//        Assert.assertEquals(CONTENT_ID_WITH_CHILD, parentIdentifier);
//        Assert.assertEquals(AssertCollection.CHILD_C3_ID, childContentIdentifier);
    }
}
