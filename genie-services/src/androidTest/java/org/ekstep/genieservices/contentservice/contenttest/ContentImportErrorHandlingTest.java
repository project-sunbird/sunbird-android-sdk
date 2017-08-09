package org.ekstep.genieservices.contentservice.contenttest;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.Assert;

import org.ekstep.genieservices.EcarCopyUtil;
import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.EcarImportRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

/**
 * Created by Sneha on 5/29/2017.
 */
@RunWith(AndroidJUnit4.class)
public class ContentImportErrorHandlingTest extends GenieServiceTestBase {
    private static final String TAG = ContentImportErrorHandlingTest.class.getSimpleName();

    private static final String ECAR_NO_MANIFEST_PATH = Environment.getExternalStorageDirectory().toString() + "/Download/hawa_no_manifest.ecar";
    private final String FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/Multiplication2.ecar";
    private final String EXPIRED_CONTENT_FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/lesson_expired.ecar";
    private final String OUTDATED_CONTENT_FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/hawa_v1.0.ecar";

    private final String NO_MANIFEST_ASSET_PATH = "Download/hawa_no_manifest.ecar";
    private final String SINGLE_FILEPATH_ASSET_PATH = "Download/Multiplication2.ecar";
    private final String EXPIRED_CONTENT_ASSET_PATH = "Download/lesson_expired.ecar";
    private final String OUTDATED_CONTENT_ASSET_PATH = "Download/hawa_v1.0.ecar";


    @Before
    public void setup() throws IOException {
        super.setup();
        activity = rule.getActivity();
        EcarCopyUtil.createFileFromAsset(activity.getApplicationContext(), NO_MANIFEST_ASSET_PATH, DESTINATION);
        EcarCopyUtil.createFileFromAsset(activity.getApplicationContext(), SINGLE_FILEPATH_ASSET_PATH, DESTINATION);
        EcarCopyUtil.createFileFromAsset(activity.getApplicationContext(), EXPIRED_CONTENT_ASSET_PATH, DESTINATION);
        EcarCopyUtil.createFileFromAsset(activity.getApplicationContext(), OUTDATED_CONTENT_ASSET_PATH, DESTINATION);
        GenieServiceDBHelper.clearEcarEntryFromDB();
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }

    @Test
    public void importContentFromEcarValidation() {

        try {
            EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder().fromFilePath("").toFolder(activity.getExternalFilesDir(null).toString());
            GenieResponse<Void> response = activity.importEcar(ecarImportRequest.build());
            Assert.assertFalse("false", response.getStatus());
            Assert.assertEquals("INVALID_FILE", response.getError());
            Assert.assertEquals("content import failed, file doesn't exists", response.getErrorMessages().get(0));

        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(true);
        }
    }

    @Test
    public void shouldNotImportExpiredEcar() {

        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder()
                .fromFilePath(EXPIRED_CONTENT_FILEPATH).toFolder(activity.getExternalFilesDir(null).toString());

        GenieResponse<Void> response = activity.importEcar(ecarImportRequest.build());

        Assert.assertFalse(response.getStatus());
        Assert.assertEquals("DRAFT_ECAR_FILE_EXPIRED", response.getError());
        Assert.assertEquals("The ECAR file is expired!!!", response.getErrorMessages().get(0));
    }

    @Test
    public void shouldShowOutdatedEcarError() {

        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder()
                .fromFilePath(OUTDATED_CONTENT_FILEPATH).toFolder(activity.getExternalFilesDir(null).toString());

        GenieResponse<Void> response = activity.importEcar(ecarImportRequest.build());

        Assert.assertFalse("false", response.getStatus());
        Assert.assertEquals("UNSUPPORTED_MANIFEST", response.getError());
        Assert.assertEquals("Cannot import outdated ECAR!", response.getErrorMessages().get(0));
    }

    /**
     * Note : Problem with import.
     */
    @Test
    public void shouldShowAlreadyImportedError() {

        EcarImportRequest.Builder contentImportRequest = new EcarImportRequest.Builder()
                .fromFilePath(FILEPATH).toFolder(activity.getExternalFilesDir(null).toString());

        GenieResponse<Void> response = activity.importEcar(contentImportRequest.build());
        Assert.assertTrue("true", response.getStatus());

        GenieResponse<Void> genieResponse = activity.importEcar(contentImportRequest.build());
        Assert.assertFalse("false", genieResponse.getStatus());
        Assert.assertEquals("The ECAR file is imported already!!!", genieResponse.getErrorMessages().get(0));
    }

    @Test
    public void test5ShouldNotImportNoManifestEcar() {

        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder()
                .fromFilePath(ECAR_NO_MANIFEST_PATH).toFolder(activity.getExternalFilesDir(null).toString());

        GenieResponse<Void> response = activity.importEcar(ecarImportRequest.build());

        Assert.assertFalse("false", response.getStatus());
        Assert.assertEquals("NO_CONTENT_TO_IMPORT", response.getError());
        Assert.assertEquals("Empty ecar, cannot import!", response.getErrorMessages().get(0));
    }
}
