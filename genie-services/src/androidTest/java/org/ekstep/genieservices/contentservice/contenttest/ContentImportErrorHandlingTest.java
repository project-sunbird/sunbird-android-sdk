package org.ekstep.genieservices.contentservice.contenttest;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.junit.Test;
import org.junit.runner.RunWith;

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

    @Test
    public void importContentFromEcarValidation() {

        GenieResponse<Void> response = activity.importContent(true, "");

        Assert.assertFalse("false", response.getStatus());
        Assert.assertEquals("INVALID_FILE", response.getError());
        Assert.assertEquals("content import failed, file doesn't exists", response.getErrorMessages().get(0));
    }

    /**
     * Note : This test Throws java.lang.IllegalArgumentException: Invalid format: "2016-08-03T00:00:00.000+0530" is malformed at ".000+0530"
     */
    @Test
    public void shouldNotImportExpiredEcar() {

        GenieResponse<Void> response = activity.importContent(false, EXPIRED_CONTENT_FILEPATH);

        Log.v(TAG, "shouldNotImportExpiredEcar getError() :: " + response.getError() + "getErrorMessages()" + response.getErrorMessages().get(0));

        Assert.assertFalse("false", response.getStatus());
        Assert.assertEquals("INVALID_FILE", response.getError());
        Assert.assertEquals("The ECAR file is expired!!!", response.getErrorMessages().get(0));
    }

    @Test
    public void shouldShowOutdatedEcarError() {

        GenieResponse<Void> response = activity.importContent(false, OUTDATED_CONTENT_FILEPATH);

        Assert.assertFalse("false", response.getStatus());
        Assert.assertEquals("INVALID_FILE", response.getError());
        Assert.assertEquals("content import failed, file doesn't exists", response.getErrorMessages().get(0));
    }

    /**
     * Note : Problem with import.
     */
    @Test
    public void shouldShowAlreadyImportedError() {

        GenieResponse<Void> response = activity.importContent(false, FILEPATH);
        Assert.assertTrue("true", response.getStatus());

        GenieResponse<Void> genieResponse = activity.importContent(false, FILEPATH);

        Assert.assertFalse("false", genieResponse.getStatus());
        Assert.assertEquals("The ECAR file is imported already!!!", genieResponse.getErrorMessages().get(0));
    }

    @Test
    public void test5ShouldNotImportNoManifestEcar() {

        GenieResponse<Void> response = activity.importContent(false, ECAR_NO_MANIFEST_PATH);

        Assert.assertFalse("false", response.getStatus());
        Assert.assertEquals("NO_CONTENT_TO_IMPORT", response.getError());
        Assert.assertEquals("Empty ecar, cannot import!", response.getErrorMessages().get(0));
    }
}
