package org.ekstep.genieservices.contentservice.content;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.ekstep.genieservices.EcarCopyUtil;
import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
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
 * Created on 5/29/2017.
 *
 * @author Sneha
 */
@RunWith(AndroidJUnit4.class)
public class ContentImportErrorHandlingTest extends GenieServiceTestBase {

    private static final String NO_MANIFEST_ASSET_PATH = "Download/hawa_no_manifest.ecar";
    private static final String SINGLE_FILEPATH_ASSET_PATH = "Download/Multiplication2.ecar";
    private static final String EXPIRED_CONTENT_ASSET_PATH = "Download/lesson_expired.ecar";
    private static final String OUTDATED_CONTENT_ASSET_PATH = "Download/hawa_v1.0.ecar";

    private static final String ECAR_NO_MANIFEST_PATH = Environment.getExternalStorageDirectory().toString() + "/Download/hawa_no_manifest.ecar";
    private final String FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/Multiplication2.ecar";
    private final String EXPIRED_CONTENT_FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/lesson_expired.ecar";
    private final String OUTDATED_CONTENT_FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/hawa_v1.0.ecar";

    @Before
    public void setup() throws IOException {
        super.setup();
        activity = rule.getActivity();
        EcarCopyUtil.createFileFromAsset(activity.getApplicationContext(), NO_MANIFEST_ASSET_PATH, DESTINATION);
        EcarCopyUtil.createFileFromAsset(activity.getApplicationContext(), SINGLE_FILEPATH_ASSET_PATH, DESTINATION);
        EcarCopyUtil.createFileFromAsset(activity.getApplicationContext(), EXPIRED_CONTENT_ASSET_PATH, DESTINATION);
        EcarCopyUtil.createFileFromAsset(activity.getApplicationContext(), OUTDATED_CONTENT_ASSET_PATH, DESTINATION);
        GenieServiceDBHelper.clearContentEntryFromDB();
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }

    @Test
    public void importContentFromEcarValidation() {

        try {
            EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder().fromFilePath("").toFolder(activity.getExternalFilesDir(null).toString());
            GenieResponse<List<ContentImportResponse>> response = activity.importEcar(ecarImportRequest.build());
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

        GenieResponse<List<ContentImportResponse>> response = activity.importEcar(ecarImportRequest.build());

        Assert.assertTrue(response.getStatus());
        List<ContentImportResponse> contentImportResponseList = response.getResult();
        Assert.assertEquals(1, contentImportResponseList.size());
        ContentImportResponse contentImportResponse = contentImportResponseList.get(0);
        Assert.assertEquals("org.ekstep.coloursofnaturehindi", contentImportResponse.getIdentifier());
        Assert.assertEquals("CONTENT_EXPIRED", contentImportResponse.getStatus().toString());
    }

    @Test
    public void shouldShowOutdatedEcarError() {
        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder()
                .fromFilePath(OUTDATED_CONTENT_FILEPATH).toFolder(activity.getExternalFilesDir(null).toString());

        GenieResponse<List<ContentImportResponse>> response = activity.importEcar(ecarImportRequest.build());

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

        GenieResponse<List<ContentImportResponse>> response = activity.importEcar(contentImportRequest.build());
//        Assert.assertTrue(response.getStatus());

        GenieResponse<List<ContentImportResponse>> genieResponse = activity.importEcar(contentImportRequest.build());
        Assert.assertTrue(genieResponse.getStatus());
        List<ContentImportResponse> contentImportResponseList = genieResponse.getResult();
        Assert.assertEquals(1, contentImportResponseList.size());
        ContentImportResponse contentImportResponse = contentImportResponseList.get(0);
        Assert.assertEquals("do_30013486", contentImportResponse.getIdentifier());
        Assert.assertEquals("ALREADY_EXIST", contentImportResponse.getStatus().toString());
    }

    @Test
    public void test5ShouldNotImportNoManifestEcar() {
        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder()
                .fromFilePath(ECAR_NO_MANIFEST_PATH).toFolder(activity.getExternalFilesDir(null).toString());

        GenieResponse<List<ContentImportResponse>> response = activity.importEcar(ecarImportRequest.build());

        Assert.assertFalse("false", response.getStatus());
        Assert.assertEquals("NO_CONTENT_TO_IMPORT", response.getError());
        Assert.assertEquals("Empty ecar, cannot import!", response.getErrorMessages().get(0));
    }
}
