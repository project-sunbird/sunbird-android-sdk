package org.ekstep.genieservices.contentservice.content;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.ekstep.genieservices.EcarCopyUtil;
import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.SampleApiResponse;
import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.bean.ContentImportRequest;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.EcarImportRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.contentservice.AssertImportTelemetryEvent;
import org.ekstep.genieservices.contentservice.collection.AssertCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swayangjit on 8/9/17.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class ImportEcarTest extends GenieServiceTestBase {

    private static final String VISIBILITY_DEFAULT = "default";
    private final String CONTENT_ID = "do_30013486";
    private final String CONTENT_ID_WITH_CHILD = "do_30019820";
    private final String UNSUPPORTED_CONTENT_PATH = "Download/unsupported.zip";
    private final String CONTENT_FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/Multiplication2.ecar";
    private final String CONTENT_WITH_CHILD_FILEPATH = Environment.getExternalStorageDirectory().toString() + "/Download/Times_Tables_2_to_10.ecar";
    private final String COLLECTION_ASSET_PATH = "Download/Times_Tables_2_to_10.ecar";
    private final String CONTENT_ASSET_PATH = "Download/Multiplication2.ecar";
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
    public void _0ShouldCopyNecessoryFiles() {
        EcarCopyUtil.createFileFromAsset(activity, UNSUPPORTED_CONTENT_PATH, DESTINATION);
        EcarCopyUtil.createFileFromAsset(activity, COLLECTION_ASSET_PATH, DESTINATION);
        EcarCopyUtil.createFileFromAsset(activity, CONTENT_ASSET_PATH, DESTINATION);
    }


    @Test
    public void _1ShouldImportContent() {
        List<String> contentIdList = new ArrayList<>();
        contentIdList.add(CONTENT_ID_WITH_CHILD);
        startMockServer();
        mMockServer.mockHttpResponse(SampleApiResponse.getSearchResultForDownload(), 200);
        ContentImportRequest.Builder contentImportRequest = new ContentImportRequest.Builder()
                .toFolder(activity.getExternalFilesDir(null).toString()).contentIds(contentIdList);
        GenieResponse<List<ContentImportResponse>> genieResponse = activity.importContent(contentImportRequest.build());
        Assert.assertTrue("true", genieResponse.getStatus());
        shutDownMockServer();
    }

    @Test
    public void _2ShouldCheckContentImportStatusWhenContentisDownloading() {
        GenieServiceDBHelper.clearContentEntryFromDB();
        List<String> contentIdList = new ArrayList<>();
        contentIdList.add(CONTENT_ID_WITH_CHILD);
        ContentImportRequest.Builder contentImportRequest = new ContentImportRequest.Builder()
                .toFolder(activity.getExternalFilesDir(null).toString()).contentIds(contentIdList);
        startMockServer();
        mMockServer.mockHttpResponse(SampleApiResponse.getSearchResultForDownload(), 200);
        GenieResponse<List<ContentImportResponse>> genieResponse = activity.importContent(contentImportRequest.build());
        Assert.assertTrue("true", genieResponse.getStatus());
        GenieResponse<ContentImportResponse> contentImportStatus = activity.getImportStatus(CONTENT_ID_WITH_CHILD);
        Assert.assertEquals(CONTENT_ID_WITH_CHILD, contentImportStatus.getResult().getIdentifier());
        Assert.assertEquals(CONTENT_ID_WITH_CHILD, contentImportStatus.getResult().getIdentifier());
        Assert.assertEquals(1, contentImportStatus.getResult().getStatus().getValue());

        shutDownMockServer();
    }

    @Test
    public void _3ShouldCheckForCompletedStatusofContent() throws InterruptedException {
        GenieServiceDBHelper.clearContentEntryFromDB();
        List<String> contentIdList = new ArrayList<>();
        contentIdList.add(CONTENT_ID);
        ContentImportRequest.Builder contentImportRequest = new ContentImportRequest.Builder()
                .toFolder(activity.getExternalFilesDir(null).toString()).contentIds(contentIdList);
        GenieResponse<List<ContentImportResponse>> genieResponse = activity.importContent(contentImportRequest.build());
        Assert.assertTrue("true", genieResponse.getStatus());
        final GenieResponse<ContentImportResponse> contentImportStatus = activity.getImportStatus(CONTENT_ID);
        Assert.assertEquals(CONTENT_ID, contentImportStatus.getResult().getIdentifier());

        //wait for the download to happen
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        Assert.assertEquals(3, contentImportStatus.getResult().getStatus());
                    }
                },
                2 * 60 * 1000
        );
    }

    @Test
    public void _4ShouldCancelDownloadOfContent() {
        GenieServiceDBHelper.clearContentEntryFromDB();
        List<String> contentIdList = new ArrayList<>();
        contentIdList.add(CONTENT_ID_WITH_CHILD);
        ContentImportRequest.Builder contentImportRequest = new ContentImportRequest.Builder()
                .toFolder(activity.getExternalFilesDir(null).toString()).contentIds(contentIdList);
        GenieResponse<List<ContentImportResponse>> genieResponse = activity.importContent(contentImportRequest.build());
        Assert.assertTrue("true", genieResponse.getStatus());
        GenieResponse<Void> downloadCancelResponse = activity.cancelDownload(CONTENT_ID_WITH_CHILD);
        Assert.assertTrue(downloadCancelResponse.getStatus());
    }

    @Test
    public void _5ShouldValidateImportEcarForUnsupportedFile() {

        String ext = FileUtil.getFileExtension(UNSUPPORTED_CONTENT_PATH);

        EcarImportRequest.Builder contentImportRequest = new EcarImportRequest.Builder()
                .fromFilePath(Environment.getExternalStorageDirectory().toString() + "unsupported.zip")
                .toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse<List<ContentImportResponse>> genieResponse = activity.importEcar(contentImportRequest.build());

        Assert.assertFalse(genieResponse.getStatus());
        Assert.assertNotSame(ServiceConstants.FileExtension.CONTENT, ext);
    }

    @Test
    public void test1shouldImportEcarHavingNoChild() {
        String ext = FileUtil.getFileExtension(CONTENT_FILEPATH);

        EcarImportRequest.Builder importEcar = new EcarImportRequest.Builder().fromFilePath(CONTENT_FILEPATH).toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse<List<ContentImportResponse>> response = activity.importEcar(importEcar.build());
        Assert.assertTrue("true", response.getStatus());
        Assert.assertNull(response.getError());
        Assert.assertEquals(ServiceConstants.FileExtension.CONTENT, ext);

        GenieServiceDBHelper.findEcarDBEntry(CONTENT_ID);
        AssertCollection.verifyNoChildContentEntry(CONTENT_ID);

        AssertImportTelemetryEvent.verifyGEInteractIsLoggedForContentImportInitiated("GE_INTERACT");
        AssertImportTelemetryEvent.verifyGeTransferIsLoggedForContentImport("GE_TRANSFER");
        AssertImportTelemetryEvent.verifyGEInteractIsLoggedForContentImportSuccess("GE_INTERACT");
    }

    @Test
    public void _6ShouldImportEcarHavingChild() throws InterruptedException {
        String ext = FileUtil.getFileExtension(CONTENT_WITH_CHILD_FILEPATH);

        EcarImportRequest.Builder contentImportRequest = new EcarImportRequest.Builder().fromFilePath(CONTENT_WITH_CHILD_FILEPATH).toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse<List<ContentImportResponse>> genieResponse = activity.importEcar(contentImportRequest.build());

        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNull(genieResponse.getError());
        Assert.assertEquals(ServiceConstants.FileExtension.CONTENT, ext);

        AssertImportTelemetryEvent.verifyGEInteractIsLoggedForContentImportInitiated("GE_INTERACT");
        AssertImportTelemetryEvent.verifyGeTransferIsLoggedForContentImport("GE_TRANSFER");
        AssertImportTelemetryEvent.verifyGEInteractIsLoggedForContentImportSuccess("GE_INTERACT");

        GenieServiceDBHelper.findEcarDBEntry(CONTENT_ID_WITH_CHILD);
        AssertCollection.verifyCollectionEntryAndVisibility(CONTENT_ID_WITH_CHILD, VISIBILITY_DEFAULT);
    }

    @Test
    public void _7ShouldValidateImportEcarForInvalidFilepath() {

        EcarImportRequest.Builder contentImportRequest = new EcarImportRequest.Builder()
                .fromFilePath(Environment.getExternalStorageDirectory().toString() + "Multiplication2.ecar")
                .toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse<List<ContentImportResponse>> genieResponse = activity.importEcar(contentImportRequest.build());

        Assert.assertFalse(genieResponse.getStatus());
        Assert.assertEquals("Content import failed, file doesn't exist.", genieResponse.getErrorMessages().get(0));
    }

    @Test
    public void _8ShouldReturnValidationErrorForInvalidEcar() {


        EcarImportRequest.Builder contentImportRequest = new EcarImportRequest.Builder()
                .fromFilePath(DESTINATION + File.separator + "unsupported.zip")
                .toFolder(activity.getExternalFilesDir(null).toString());
        GenieResponse<List<ContentImportResponse>> genieResponse = activity.importEcar(contentImportRequest.build());
        Assert.assertFalse(genieResponse.getStatus());
        Assert.assertEquals(ServiceConstants.ErrorCode.INVALID_FILE, genieResponse.getError());
        Assert.assertEquals(ServiceConstants.ErrorMessage.UNSUPPORTED_FILE, genieResponse.getErrorMessages().get(0));
    }


}
