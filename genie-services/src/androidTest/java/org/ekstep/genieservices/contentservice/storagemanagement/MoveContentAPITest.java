package org.ekstep.genieservices.contentservice.storagemanagement;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;

import org.ekstep.genieservices.EcarCopyUtil;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.ContentMoveRequest;
import org.ekstep.genieservices.commons.bean.EcarImportRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created on 13/10/17.
 * shriharsh
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class MoveContentAPITest extends GenieServiceTestBase {

    private static final String VISIBILITY_DEFAULT = "default";
    private final String CONTENT_ID = "do_30013486";
    private final String CONTENT_FILEPATH = "Download/Multiplication2.ecar";

    @Before
    public void setup() throws IOException {
        super.setup();
        activity = rule.getActivity();
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }

    @Test
    public void _0ShouldCopyNecessaryFiles() {
        EcarCopyUtil.createFileFromAsset(activity, CONTENT_FILEPATH, DESTINATION);
    }


    @Test
    public void _1importContentEcar() {
        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder().fromFilePath(DESTINATION + "/Multiplication2.ecar").toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse<List<ContentImportResponse>> response = activity.importEcar(ecarImportRequest.build());
    }

    /**
     * Test 1 - Move content from internal to external storage
     * <p>
     * a. Add any content to the CONTENT folder in the internal storage.
     * b. using move api copy the content to the CONTENT folder of the external storage
     * c. Check if the content_id folder exists in the SD Card
     * d. Check if the content_id folder does not exist in the Internal Memory
     */
    @Test
    public void _2moveContentFromInternalToExternalStorage() {
        moveContentFromInternalToExternalStorage();
    }

    private void moveContentFromInternalToExternalStorage() {
        String sdcardPath = getExternalSdcardPath(activity);

        ContentMoveRequest.Builder contentMoveRequest = new ContentMoveRequest.Builder();
        contentMoveRequest.toFolder(sdcardPath);

        GenieResponse<Void> response = activity.moveContent(contentMoveRequest);

        if (response != null) {
            String folderPathInSdCard = getExternalSdcardPath(activity) + "/content/" + CONTENT_ID;
            String folderPathInInternalMemory = activity.getExternalFilesDir(null).toString() + "/content/" + CONTENT_ID;

            Assert.assertFalse(FileUtil.doesFileExists(folderPathInInternalMemory));
            Assert.assertTrue(FileUtil.doesFileExists(folderPathInSdCard));
        }
    }

    /**
     * Test 2 - Move content from external to internal storage
     * <p>
     * a. Add any content to the CONTENT folder in the internal storage.
     * b. using move api copy the content to the CONTENT folder of the external storage
     * c. Check if the content_id folder exists in the SD Card
     * d. Check if the content_id folder does not exist in the Internal Memory
     */
    @Test
    public void _3moveContentFromExternalToInternalStorage() {
        String internalStorageFilePath = activity.getExternalFilesDir(null).toString();

        ContentMoveRequest.Builder contentMoveRequest = new ContentMoveRequest.Builder();
        contentMoveRequest.toFolder(internalStorageFilePath);

        GenieResponse<Void> response = activity.moveContent(contentMoveRequest);

        if (response != null) {
            String folderPathInSdCard = getExternalSdcardPath(activity) + "/content/" + CONTENT_ID;
            String folderPathInInternalMemory = activity.getExternalFilesDir(null).toString() + "/content/" + CONTENT_ID;

            Assert.assertFalse(FileUtil.doesFileExists(folderPathInSdCard));
            Assert.assertTrue(FileUtil.doesFileExists(folderPathInInternalMemory));
        }
    }

    /**
     * Test 3 - Copy same content present in External storage, from Internal storage
     *
     * a. Copy same content to the CONTENT folder in the internal storage.
     * b. using move api copy the content to the CONTENT folder of the external storage, which already has the same content
     * c. Check if the content_id folder exists in the SD Card
     * d. Check if the content_id folder does not exist in the Internal Memory
     */
    @Test
    public void _4moveAlreadyExistingContentFromInternalToExternal() {
        String contentPathInInternalMemory = activity.getExternalFilesDir(null).toString() + "/content/" + CONTENT_ID;
        String contentPathInExternalMemory = getExternalSdcardPath(activity) + "/content/" + CONTENT_ID;

        //copy the file from internal to external
        try {
            FileUtil.copyFolder(new File(contentPathInInternalMemory), new File(contentPathInExternalMemory));
        } catch (IOException e) {
            e.printStackTrace();
        }

        moveContentFromInternalToExternalStorage();
    }

    public String getExternalSdcardPath(Context context) {
        String sdCardPath = MountPointUtils.getExternalSecondaryStorage(context);
        File[] dirs = ContextCompat.getExternalFilesDirs(context, null);
        for (File d : dirs) {
            String path = d.getPath();
            if (path.contains(sdCardPath))
                return path;
        }
        return null;
    }

}
