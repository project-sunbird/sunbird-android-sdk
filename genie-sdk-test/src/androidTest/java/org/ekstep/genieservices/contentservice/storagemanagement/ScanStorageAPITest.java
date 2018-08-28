package org.ekstep.genieservices.contentservice.storagemanagement;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;

import org.ekstep.genieservices.EcarCopyUtil;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.Content;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
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
public class ScanStorageAPITest extends GenieServiceTestBase {

    private static final String VISIBILITY_DEFAULT = "default";
    private final String MULTIPLE2_CONTENT_ID = "do_30013486";
    private final String MULTIPLE4_CONTENT_ID = "do_30013504";
    private final String MULTIPLE2_CONTENT_FILEPATH = "Download/Multiplication2.ecar";
    private final String MULTIPLE4_CONTENT_FILEPATH = "Download/Multiplication4.ecar";
    private final String HAWA_CONTENT_FILEPATH = "Download/hawa_v1.0.ecar";

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
        EcarCopyUtil.createFileFromAsset(activity, MULTIPLE2_CONTENT_FILEPATH, DESTINATION);
    }


    @Test
    public void _1importContentEcar() {
        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder().fromFilePath(DESTINATION + "/Multiplication2.ecar").toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse<List<ContentImportResponse>> response = activity.importEcar(ecarImportRequest.build());
    }

    /**
     * Valid content is with the manifest.json in it
     * Invalid content is without the manifest.json in it
     */

    /**
     * Test 1 - Add an extra valid content to the CONTENT folder manually (Default visibility)
     * <p>
     * a. Add any valid default visibility content to the CONTENT folder in the internal storage, to the already existing contents.
     * b. Use scanStorage api, to check if any content is added or not
     * c. If added, check if the content which was manually added is added to the db
     * d. Check if the recently added content is got in the get all local contents api
     * <p>
     * Result - Should be added to the db and must be listed in the list of local contents
     */
    @Test
    public void _2addValidContentToTheFolder() {
        //get all local contents
        GenieResponse<List<Content>> genieResponse = activity.getAllLocalContent(null);

        //copy hawa.ecar to GenieSDKDumpTest folder
        EcarCopyUtil.createFileFromAsset(activity, HAWA_CONTENT_FILEPATH, DESTINATION);

        GenieResponse<List<ContentImportResponse>> response = importHawaContent();

        if (response.getStatus()) {
            //copy hawa.ecar to Content folder within org.ekstep.genieservices.test
            String sourceOfMultiple4Path = DESTINATION + "/content/" + MULTIPLE4_CONTENT_ID;
            String destinationOfMultiple4Path = activity.getExternalFilesDir(null).toString() + "/content/" + MULTIPLE4_CONTENT_ID;

            //copy folder from source to destination
            try {
                FileUtil.copyFolder(new File(sourceOfMultiple4Path), new File(destinationOfMultiple4Path));
            } catch (IOException e) {
                e.printStackTrace();
            }

            genieResponse = activity.getAllLocalContent(null);

            boolean isIdentifierPresent = false;

            if (genieResponse != null && genieResponse.getStatus() &&
                    genieResponse.getResult() != null && genieResponse.getResult().size() > 0) {
                for (Content content : genieResponse.getResult()) {
                    if (content.getIdentifier().equalsIgnoreCase(MULTIPLE4_CONTENT_ID)) {
                        isIdentifierPresent = true;
                    }
                }
            }

            if (isIdentifierPresent) {
                Assert.assertTrue(true);
            } else {
                Assert.assertTrue(false);
            }
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

    private GenieResponse<List<ContentImportResponse>> importMultiple4Content() {
        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder().fromFilePath(DESTINATION + "/Multiplication4.ecar").toFolder(DESTINATION);
        GenieResponse<List<ContentImportResponse>> response = activity.importEcar(ecarImportRequest.build());
        return response;
    }

    private GenieResponse<List<ContentImportResponse>> importHawaContent() {
        EcarImportRequest.Builder ecarImportRequest = new EcarImportRequest.Builder().fromFilePath(DESTINATION + "/hawa_v1.0.ecar").toFolder(DESTINATION);
        GenieResponse<List<ContentImportResponse>> response = activity.importEcar(ecarImportRequest.build());
        return response;
    }

    /**
     * Test 2 - Add an extra invalid content to the CONTENT folder manually (Default visibility)
     * <p>
     * a. Add any invalid default visibility content to the CONTENT folder in the internal storage, to the already existing contents.
     * b. Using scanStorage api, to check if any content is added or not
     * c. If added, check if the content which was manually added is added to the db
     * d. Check if the recently added content is got in the get all local contents api
     * <p>
     * Result - Should not be added to the db and must not be listed in the list of local contents
     */
    @Test
    public void _3addInvalidContentToTheFolder() {

    }

    /**
     * Test 3 - Add an extra valid content to the CONTENT folder manually (Parent visibility)
     *
     * a. Add any valid parent visibility content to the CONTENT folder in the internal storage, to the already existing contents.
     * b. Using scanStorage api, to check if any content is added or not
     * c. If added, check if the content which was manually added is added to the db
     * d. Check if the recently added content is got in the get all local contents api
     *
     * Result - Should be added to the db and must not be listed in the list of local contents
     */
//    @Test

    /**
     * Test 4 - Add an extra invalid content to the CONTENT folder manually (Parent visibility)
     *
     * a. Add any invalid parent visibility content to the CONTENT folder in the internal storage, to the already existing contents.
     * b. Using scanStorage api, to check if any content is added or not
     * c. If added, check if the content which was manually added is added to the db
     * d. Check if the recently added content is got in the get all local contents api
     *
     * Result - Should be added to the db and must not be listed in the list of local contents
     */
//    @Test

    /**
     * Test 5 - Delete a content from the CONTENT folder manually (Default visibility)
     *
     * a. Delete any valid default visibility content from the CONTENT folder in the internal storage
     * b. Using scanStorage api, to check if any content is deleted or not
     * c. If deleted, check if the content is not available in the get all local contents api
     *
     * Result - Should be deleted and must not be listed in the get all local contents api
     */
//    @Test

    /**
     * Test 6 - Delete a content from the CONTENT folder manually (Default visibility)
     *
     * a. Delete any valid default visibility content from the CONTENT folder in the internal storage
     * b. Using scanStorage api, to check if any content is deleted or not
     * c. If deleted, check if the content is not available in the get all local contents api
     *
     * Result - Should be deleted and must not be listed in the get all local contents api
     */
//    @Test
}
