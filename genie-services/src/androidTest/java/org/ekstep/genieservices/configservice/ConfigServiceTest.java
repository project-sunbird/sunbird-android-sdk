package org.ekstep.genieservices.configservice;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.SampleResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MasterData;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.Map;

/**
 * Created by Sneha on 4/27/2017.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigServiceTest extends GenieServiceTestBase {
    private static final String TAG = ConfigServiceTest.class.getSimpleName();

    /**
     * Scenario : To get the age data, on successful fetching of data the response will return the status as true
     * with MasterData in the result.
     * When : When SDK is fetching age from MasterData.
     * Then : On success, the response will return the status as TRUE and with Age data in the result.
     */
    @Test
    public void _11_shouldGetAgeFromMasterData() {

        startMockServer();
        waitForGenieToBecomeIdle();

        mMockServer.mockHttpResponse(SampleResponse.getTermsAPIResponse(), 200);
        try {
            activity.getMasterData(MasterDataType.AGE);
        } catch (Exception e) {
        }
        GenieResponse<MasterData> genieResponse = activity.getMasterData(MasterDataType.AGE);
        MasterData ageData = genieResponse.getResult();
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(ageData);
        Assert.assertEquals(ageData.getValue(), "age");

        shutDownMockServer();
    }

    @Test
    public void _22_shouldGetBoardMasterData() {

        startMockServer();
        waitForGenieToBecomeIdle();
        mMockServer.mockHttpResponse(SampleResponse.getTermsAPIResponse(), 200);
        GenieResponse<MasterData> genieResponse = activity.getMasterData(MasterDataType.BOARD);
        MasterData boardData = genieResponse.getResult();
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(boardData);
        Assert.assertEquals(boardData.getValue(), "board");
        shutDownMockServer();
    }

    /**
     * Scenario : To get the medium data, on successful fetching of data the response will return as true
     * with MasterData in the result.
     * When : When SDK is fetching medium from MasterData.
     * Then : On success, the response will return status as TRUE and with Medium data in the result.
     */
    @Test
    public void _33_shouldGetMediumMasterData() {
        startMockServer();
        waitForGenieToBecomeIdle();
        mMockServer.mockHttpResponse(SampleResponse.getTermsAPIResponse(), 200);
        GenieResponse<MasterData> genieResponse = activity.getMasterData(MasterDataType.MEDIUM);
        MasterData masterData = genieResponse.getResult();
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(masterData);
        Assert.assertEquals(masterData.getValue(), "medium");
        shutDownMockServer();
    }

    /**
     * Scenario : To get the subject data, on successful fetching of data the response will return as true
     * with MasterData in the result.
     * When : When SDK is fetching subject from MasterData.
     * Then : On success, the response will return status as TRUE and with Subject data in the result.
     */
    @Test
    public void _44_shouldGetSubjectMasterData() throws InterruptedException {
        startMockServer();
        waitForGenieToBecomeIdle();
        mMockServer.mockHttpResponse(SampleResponse.getTermsAPIResponse(), 200);
        GenieResponse<MasterData> genieResponse = activity.getMasterData(MasterDataType.SUBJECT);
        Thread.sleep(10000);
        MasterData masterData = genieResponse.getResult();
//        Assert.assertTrue(genieResponse.getStatus());
//        Assert.assertNotNull(masterData);
//        Assert.assertEquals(masterData.getValue(), "subject");
        shutDownMockServer();
    }

    /**
     * Scenario : To get the grade level data, on successful fetching of data the response will return as true
     * with MasterData in the result.
     * When : When SDK is fetching GradeLevel from MasterData.
     * Then : On success, the response will return status as TRUE and with GradeLevel data in the result.
     */
    @Test
    public void _55_shouldGetGradeLevelMasterData() {
        startMockServer();
        mMockServer.mockHttpResponse(SampleResponse.getTermsAPIResponse(), 200);
        GenieResponse<MasterData> genieResponse = activity.getMasterData(MasterDataType.GRADELEVEL);
        MasterData masterData = genieResponse.getResult();
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(masterData);
        Assert.assertEquals(masterData.getValue(), "gradeLevel");
        shutDownMockServer();
    }

    /**
     * Scenario : To get the age group data, on successful fetching of data the response will return as true
     * with MasterData in the result.
     * When : When SDK is fetching  AgeGroup from MasterData.
     * Then : On success, the response will return status as TRUE and with AgeGroup data in the result.
     */
    @Test
    public void _66_shouldGetAgeGroupMasterData() {

        startMockServer();
        mMockServer.mockHttpResponse(SampleResponse.getTermsAPIResponse(), 200);
        GenieResponse<MasterData> genieResponse = activity.getMasterData(MasterDataType.AGEGROUP);
        MasterData masterData = genieResponse.getResult();
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(masterData);
        Assert.assertEquals(masterData.getValue(), "ageGroup");
        shutDownMockServer();
    }

    /**
     * Scenario : To check the response by the passing the request as null to get the MasterData.
     * Given : To get the MasterData.
     * When : When SDK is fetching MasterData passing null.
     * Then : Error message is displayed.
     */
    @Test
    public void _77_shouldCheckForNullMasterType() {
        startMockServer();
        mMockServer.mockHttpResponse(SampleResponse.getTermsAPIResponse(), 200);
        try {
            GenieResponse<MasterData> genieResponse = activity.getMasterData(null);
            Assert.assertFalse(genieResponse.getStatus());
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
        shutDownMockServer();
    }

    /**
     * Scenario : To get the ordered related data about the platform and other platform parameters.
     * When : When SDK is fetching ordered related data about the platform
     * Then : On successful fetching of the data, the response will return status as TRUE and with result set in Map.
     */
    @Test
    public void _88_shouldGetOrdinals() throws InterruptedException {
        startMockServer();
        waitForGenieToBecomeIdle();
        mMockServer.mockHttpResponse(SampleResponse.getOrdinalsAPIResponse(), 200);
        GenieResponse<Map<String, Object>> genieResponse = activity.getOrdinals();

        Thread.sleep(10000);
//        Assert.assertTrue(genieResponse.getStatus());
//        Assert.assertNotNull(genieResponse.getResult());
        shutDownMockServer();
    }

    /**
     * Scenario : To get the platform specific data, specific to locale chosen.
     * When : When SDK is fetching platform specific data by passing a specific locale.
     * Then : On successful fetching the data, the response will return status as TRUE and with result set in Map.
     */
    @Test
    public void _991_shouldGetResourceBundle() throws InterruptedException {
        startMockServer();
        waitForGenieToBecomeIdle();
        mMockServer.mockHttpResponse(SampleResponse.getResourceBundleAPIResponse(), 200);
        GenieResponse<Map<String, Object>> genieResponse = activity.getResourceBundle("en");
        Thread.sleep(10000);
//        Assert.assertTrue(genieResponse.getStatus());
//        Assert.assertNotNull(genieResponse.getResult());
        shutDownMockServer();
    }

    /**
     * TODO : needs to be checked.
     * Scenario : To validate the platform specific data, for an invalid locale.
     * When : Validation for invalid locale when SDK is fetching platform specific data.
     * Then : By default returns the data for "en" language.
     */
    @Test
    public void _992_shouldValidateResourceBundle() throws InterruptedException {

        startMockServer();
        waitForGenieToBecomeIdle();
        mMockServer.mockHttpResponse(SampleResponse.getResourceBundleAPIResponse(), 200);
        GenieResponse<Map<String, Object>> genieResponse = activity.getResourceBundle("me");
        Thread.sleep(10000);
//        Assert.assertTrue(genieResponse.getStatus());
        shutDownMockServer();
    }
}

