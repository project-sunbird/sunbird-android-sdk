package org.ekstep.genieservices.contentservice.collection;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.ekstep.genieservices.EcarCopyUtil;
import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentDetailsRequest;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.EcarImportRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sneha on 5/30/2017.
 */
@RunWith(AndroidJUnit4.class)
public class CollectionImportWithNewerChildContent extends GenieServiceTestBase {

    private static final String VISIBILITY_PARENT = "parent";
    private static final String VISIBILITY_DEFAULT = "default";
    private static final String COLLECTION_FILE_PATH = Environment.getExternalStorageDirectory().toString() + "/Download/Times_Tables_2_to_10.ecar";
    private static final String CHILD_CONTENT2_FILE_PATH = Environment.getExternalStorageDirectory().toString() + "/Download/multiplication_2_v1.ecar";

    private static final String COLLECTION_ASSET_PATH = "Download/Times_Tables_2_to_10.ecar";
    private static final String CHILD_CONTENT2_ASSET_PATH = "Download/multiplication_2_v1.ecar";

    @Before
    public void setup() throws IOException {
        super.setup();
        activity = rule.getActivity();
        EcarCopyUtil.createFileFromAsset(activity.getApplicationContext(), COLLECTION_ASSET_PATH, DESTINATION);
        EcarCopyUtil.createFileFromAsset(activity.getApplicationContext(), CHILD_CONTENT2_ASSET_PATH, DESTINATION);
        GenieServiceDBHelper.clearContentEntryFromDB();
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }

    /**
     * 1. Import older content C1(c1 v1.0) (child version is manually changed/downgraded from v2 to v1)
     * 2. Import a collection which has the same content with higher version i.e. 3.0
     * 3. Older content version should also get upgraded to 3.0.
     */
    @Test
    public void testShouldImportContentAndCollection() {

        GenieServiceDBHelper.clearContentEntryFromDB();

        shouldImportChildContentEcar();

        shouldImportCollectionEcar();
    }

    private void shouldImportChildContentEcar() {

        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder().isChildContent()
                .fromFilePath(CHILD_CONTENT2_FILE_PATH).toFolder(activity.getExternalFilesDir(null).toString());

        GenieResponse genieResponse = activity.importEcar(ecarImportRequest.build());
//        Assert.assertTrue("true", genieResponse.getStatus());
//        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_CONTENT2_ECAR_ID, VISIBILITY_DEFAULT);
//        AssertCollection.verifyContentVersionToBeUpdated(AssertCollection.CHILD_CONTENT2_ECAR_ID, 1.0, 1);
    }

    private void shouldImportCollectionEcar() {

        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder().isChildContent()
                .fromFilePath(COLLECTION_FILE_PATH).toFolder(activity.getExternalFilesDir(null).toString());

        GenieResponse<List<ContentImportResponse>> genieResponse = activity.importEcar(ecarImportRequest.build());
        Assert.assertTrue("true", genieResponse.getStatus());
        AssertCollection.verifyCollectionEntryAndVisibility(AssertCollection.COLLECTION_ECAR_ID, VISIBILITY_DEFAULT);
        AssertCollection.verifyContentEntryAndVisibility(AssertCollection.CHILD_C2_ID, VISIBILITY_DEFAULT);

        //updated content check
        ContentDetailsRequest.Builder contentDetailsRequest = new ContentDetailsRequest.Builder()
                .forContent(AssertCollection.CHILD_CONTENT2_ECAR_ID);
        GenieResponse<Content> contentDetailsResponse = activity.getContentDetails(contentDetailsRequest.build());
        Assert.assertTrue(contentDetailsResponse.getStatus());
        String updatedContentVersion = contentDetailsResponse.getResult().getContentData().getPkgVersion();

        Assert.assertEquals("3.0", updatedContentVersion.toString());
    }
}
