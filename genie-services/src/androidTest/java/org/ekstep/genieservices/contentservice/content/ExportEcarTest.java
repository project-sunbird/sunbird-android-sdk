package org.ekstep.genieservices.contentservice.content;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.ekstep.genieservices.EcarCopyUtil;
import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.ContentDeleteRequest;
import org.ekstep.genieservices.commons.bean.ContentExportRequest;
import org.ekstep.genieservices.commons.bean.ContentExportResponse;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.EcarImportRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swayangjit on 8/9/17.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class ExportEcarTest extends GenieServiceTestBase {
    private final String CONTENT_ID_WITH_CHILD = "do_30019820";
    private final String COLLECTION_ASSET_PATH = "Download/Times_Tables_2_to_10.ecar";
    private final String COLLECTION_FILE_PATH = Environment.getExternalStorageDirectory().toString() + "/Download/Times_Tables_2_to_10.ecar";

    @Before
    public void setup() throws IOException {
        super.setup();
        activity = rule.getActivity();
        GenieServiceDBHelper.clearContentEntryFromDB();
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }

    @Test
    public void _00ShouldCopyAllNecessoryEcars() {
        EcarCopyUtil.createFileFromAsset(activity, COLLECTION_ASSET_PATH, DESTINATION);
    }

    @Test
    public void _11ShouldExportContent() {
        //import content
        List<String> contentIdList = new ArrayList<>();
        contentIdList.add(CONTENT_ID_WITH_CHILD);

        EcarImportRequest.Builder importRequest = new EcarImportRequest.Builder()
                .fromFilePath(COLLECTION_FILE_PATH).toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse<List<ContentImportResponse>> response = activity.importEcar(importRequest.build());
        Assert.assertTrue("true", response.getStatus());

        //export content
        ContentExportRequest.Builder contentExportRequest = new ContentExportRequest.Builder()
                .exportContents(contentIdList)
                .toFolder(Environment.getExternalStorageDirectory().toString());
        GenieResponse<ContentExportResponse> exportResponse = activity.exportContent(contentExportRequest.build());
        Assert.assertTrue(exportResponse.getStatus());

        //delete content
        ContentDeleteRequest.Builder deleteRequest = new ContentDeleteRequest.Builder().contentId(CONTENT_ID_WITH_CHILD);
        GenieResponse deleteResponse = activity.deleteContent(deleteRequest.build());
        Assert.assertTrue(deleteResponse.getStatus());

        //import the exported ecar(in Environment.getExternalStorageDirectory()) to ensure export has happened.
        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder()
                .fromFilePath(exportResponse.getResult().getExportedFilePath()).toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse importResponse = activity.importEcar(ecarImportRequest.build());
        Assert.assertTrue(importResponse.getStatus());
    }

    @Test
    public void _21ShouldShouwValidationErrorForEmptyContentId() {

        ArrayList contentIdList = new ArrayList();

        //try to export, when there is no content.
        ContentExportRequest.Builder contentExportRequest = new ContentExportRequest.Builder()
                .exportContents(contentIdList)
                .toFolder(Environment.getExternalStorageDirectory().toString());
        GenieResponse<ContentExportResponse> exportResponse = activity.exportContent(contentExportRequest.build());
        Assert.assertFalse(exportResponse.getStatus());
        Assert.assertEquals("Nothing to export!", exportResponse.getErrorMessages().get(0));
    }

}
