package org.ekstep.genieservices.contentservice.storagemanagement;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;

import org.ekstep.genieservices.EcarCopyUtil;
import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.ContentMoveRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.content.db.model.ContentModel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;

/**
 * Created on 13/10/17.
 * shriharsh
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class MoveContentAPITest extends GenieServiceTestBase {

    private static final String VISIBILITY_DEFAULT = "default";
    private static final String COLLECTION_ASSET_PATH = "Download/Times_Tables_2_to_10.ecar";
    private final String CONTENT_ID = "do_30013486";
    private final String CONTENT_FILEPATH = "Download/Multiplication2.ecar";

    @Rule
    public ExpectedException exception = ExpectedException.none();

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
    public void _11importContentEcar() {
//        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder().fromFilePath(DESTINATION + "/Multiplication2.ecar").toFolder(activity.getExternalFilesDir(null).toString());
//        GenieResponse<List<ContentImportResponse>> response = activity.importEcar(ecarImportRequest.build());
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
    public void _22moveContentFromInternalToExternalStorage() {
//        moveContentFromInternalToExternalStorage();
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

            ContentModel contentModel = GenieServiceDBHelper.getContentPathInDB(CONTENT_ID);
            Assert.assertEquals(folderPathInSdCard, contentModel.getPath());
        }
    }

    /**
     * Scenario: Move downloaded content to default storage.
     * Given: When user sets the external storage as default storage,
     * then the contents that are downloaded later should move to external storage.
     * When: User sets the external storage as default.
     * Then: The downloaded contents get stored to the external storage.
     */
    @Test
    public void _33checkDownloadedContentMovesToDefaultStorage() {

//        String folderPathInSdCard = getExternalSdcardPath(activity) + "/content/" + CONTENT_ID;
//        String folderPathInInternalMemory = activity.getExternalFilesDir(null).toString() + "/content/" + CONTENT_ID;
//
//        //import content
//        EcarCopyUtil.createFileFromAsset(activity, COLLECTION_ASSET_PATH, DESTINATION);
//        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder()
//                .fromFilePath(DESTINATION + "/Multiplication2.ecar")
//                .toFolder(getExternalSdcardPath(activity));
//        GenieResponse<List<ContentImportResponse>> response = activity.importEcar(ecarImportRequest.build());
//
//        //check if it's getting stored in external storage.
//        Assert.assertTrue(response.getStatus());
//        Assert.assertTrue(FileUtil.doesFileExists(folderPathInSdCard));
//        Assert.assertFalse(FileUtil.doesFileExists(folderPathInInternalMemory));
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
    public void _44moveContentFromExternalToInternalStorage() {
//        moveContentFromExternalToInternalStorage();
    }

    /**
     * Test 3 - Copy same content present in External storage, from Internal storage
     * <p>
     * a. Copy same content to the CONTENT folder in the internal storage.
     * b. using move api copy the content to the CONTENT folder of the external storage, which already has the same content
     * c. Check if the content_id folder exists in the SD Card
     * d. Check if the content_id folder does not exist in the Internal Memory
     */
    @Test
    public void _55moveAlreadyExistingContentFromInternalToExternal() {
//        String contentPathInInternalMemory = activity.getExternalFilesDir(null).toString() + "/content/" + CONTENT_ID;
//        String contentPathInExternalMemory = getExternalSdcardPath(activity) + "/content/" + CONTENT_ID;
//
//        //copy the file from internal to external
//        try {
//            FileUtil.copyFolder(new File(contentPathInInternalMemory), new File(contentPathInExternalMemory));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        moveContentFromInternalToExternalStorage();
    }

    /**
     * Scenario: When device external memory is low and we try to move the file to external storage, File IOException is thrown.
     * Given: When device is external memory is low.
     * When: We try to move file to device storage.
     * Then: IOException is thrown
     *
     * @throws IOException - No space left on device
     */
//    @Test
//    public void _77_checkForLowExternalMemory() throws IOException {
//
//        File contentPathInMemory = new File(getExternalSdcardPath(activity) + "/content/" + CONTENT_ID);
//        String dummyFilepath = getExternalSdcardPath(activity) + "/content2/";
//
//        int i = 0;
//
//        long usedSpace = DeviceSpec.getTotalExternalMemorySize() - DeviceSpec.getAvailableExternalMemorySize();
//        long availableSpace = DeviceSpec.getAvailableExternalMemorySize();
//
//        while (availableSpace != 0) {
//            i++;
//            File copyOfContent = new File(dummyFilepath + i);
//            FileUtil.copyFolder(contentPathInMemory, copyOfContent);
//            exception.expect(IOException.class);
//
//            if (usedSpace == availableSpace) {
//                exception.expect(IOException.class);
//                break;
//            }
//        }
//    }

    /**
     * Scenario: When device internal memory is low and we try to move the file to internal storage, File IOException is thrown.
     * Given: When device is internal memory is low.
     * When: We try to move file to device storage.
     * Then: IOException is thrown
     *
     * @throws IOException - No space left on device
     */
//    @Test
//    public void _99_checkForLowInternalMemory() throws IOException {
//
//        moveContentFromExternalToInternalStorage();
//
//        String contentPathInInternalMemory = activity.getExternalFilesDir(null).toString() + "/content/" + CONTENT_ID;
//        String dummyPath = activity.getExternalFilesDir(null).toString() + "/content2/";
//
//        int i = 0;
//
//        long usedSpace = DeviceSpec.getTotalInternalMemorySize() - DeviceSpec.getAvailableInternalMemorySize();
//        long availableSpace = DeviceSpec.getAvailableInternalMemorySize();
//
//        while (availableSpace != 0) {
//            i++;
//            File copyOfContent = new File(dummyPath + i);
//
//            FileUtil.copyFolder(new File(contentPathInInternalMemory), copyOfContent);
//            exception.expect(IOException.class);
//
//            if (usedSpace == availableSpace) {
//                exception.expect(IOException.class);
//                break;
//            }
//        }
//    }
    private void moveContentFromExternalToInternalStorage() {
        String internalStorageFilePath = activity.getExternalFilesDir(null).toString();

        ContentMoveRequest.Builder contentMoveRequest = new ContentMoveRequest.Builder();
        contentMoveRequest.toFolder(internalStorageFilePath);

        GenieResponse<Void> response = activity.moveContent(contentMoveRequest);

        if (response != null) {
            String folderPathInSdCard = getExternalSdcardPath(activity) + "/content/" + CONTENT_ID;
            String folderPathInInternalMemory = activity.getExternalFilesDir(null).toString() + "/content/" + CONTENT_ID;

            Assert.assertFalse(FileUtil.doesFileExists(folderPathInSdCard));
            Assert.assertTrue(FileUtil.doesFileExists(folderPathInInternalMemory));

            ContentModel contentModel = GenieServiceDBHelper.getContentPathInDB(CONTENT_ID);
            Assert.assertEquals(folderPathInInternalMemory, contentModel.getPath());
        }
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
