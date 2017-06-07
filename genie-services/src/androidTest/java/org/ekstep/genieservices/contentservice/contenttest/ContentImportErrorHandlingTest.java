package org.ekstep.genieservices.contentservice.contenttest;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.ContentImportRequest;
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
    private final String OUTDATED_CONTENT_FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/hawa_v1.0.ecar.ecar";

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
    public void importContentFromEcarValidation() {

        ContentImportRequest.Builder contentImportRequest = new ContentImportRequest.Builder(true)
                .fromFilePath("").toFolder(activity.getExternalFilesDir(null));

        GenieResponse<Void> response = activity.importContent(contentImportRequest.build());

        Assert.assertFalse("false", response.getStatus());
        Assert.assertEquals("INVALID_FILE", response.getError());
        Assert.assertEquals("content import failed, file doesn't exists", response.getErrorMessages().get(0));
    }

    /**
     * Note : This test Throws java.lang.IllegalArgumentException: Invalid format: "2016-08-03T00:00:00.000+0530" is malformed at ".000+0530"
     */
    @Test
    public void shouldNotImportExpiredEcar() {

        ContentImportRequest.Builder contentImportRequest = new ContentImportRequest.Builder(false)
                .fromFilePath(EXPIRED_CONTENT_FILEPATH).toFolder(activity.getExternalFilesDir(null));

        GenieResponse<Void> response = activity.importContent(contentImportRequest.build());

        Log.v(TAG, "shouldNotImportExpiredEcar getError() :: " + response.getError() + "getErrorMessages()" + response.getErrorMessages().get(0));

        Assert.assertFalse("false", response.getStatus());
        Assert.assertEquals("DRAFT_ECAR_FILE_EXPIRED", response.getError());
        Assert.assertEquals("The ECAR file is expired!!!", response.getErrorMessages().get(0));
    }

    @Test
    public void shouldShowOutdatedEcarError() {

        ContentImportRequest.Builder contentImportRequest = new ContentImportRequest.Builder(false)
                .fromFilePath(OUTDATED_CONTENT_FILEPATH).toFolder(activity.getExternalFilesDir(null));

        GenieResponse<Void> response = activity.importContent(contentImportRequest.build());

        Assert.assertFalse("false", response.getStatus());
        Assert.assertEquals("INVALID_FILE", response.getError());
        Assert.assertEquals("content import failed, file doesn't exists", response.getErrorMessages().get(0));
    }

    /**
     * Note : Problem with import.
     */
    @Test
    public void shouldShowAlreadyImportedError() {

        ContentImportRequest.Builder contentImportRequest = new ContentImportRequest.Builder(false)
                .fromFilePath(FILEPATH).toFolder(activity.getExternalFilesDir(null));

        GenieResponse<Void> response = activity.importContent(contentImportRequest.build());
        Assert.assertTrue("true", response.getStatus());

        GenieResponse<Void> genieResponse = activity.importContent(contentImportRequest.build());
        Assert.assertFalse("false", genieResponse.getStatus());
        Assert.assertEquals("The ECAR file is imported already!!!", genieResponse.getErrorMessages().get(0));
    }

    @Test
    public void test5ShouldNotImportNoManifestEcar() {

        ContentImportRequest.Builder contentImportRequest = new ContentImportRequest.Builder(false)
                .fromFilePath(ECAR_NO_MANIFEST_PATH).toFolder(activity.getExternalFilesDir(null));

        GenieResponse<Void> response = activity.importContent(contentImportRequest.build());

        Assert.assertFalse("false", response.getStatus());
        Assert.assertEquals("NO_CONTENT_TO_IMPORT", response.getError());
        Assert.assertEquals("Empty ecar, cannot import!", response.getErrorMessages().get(0));
    }
}
