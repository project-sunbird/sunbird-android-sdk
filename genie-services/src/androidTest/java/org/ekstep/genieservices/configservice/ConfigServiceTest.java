package org.ekstep.genieservices.configservice;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MasterData;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

/**
 * Created by Sneha on 4/27/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ConfigServiceTest extends GenieServiceTestBase {
    private static final String TAG = ConfigServiceTest.class.getSimpleName();

    /**
     * Scenario : To get the age data, on successful fetching of data the response will return the status as true
     * with MasterData in the result.
     * Given : To fetch the age data.
     * When : MasterDataType is Age.
     * Then : On success, the response will return the status as TRUE and with Age data in the result.
     */
    @Test
    public void shouldGetAgeFromMasterData() {

        waitForGenieToBecomeIdle();

        GenieResponse<MasterData> genieResponse = activity.getMasterData(MasterDataType.AGE);
        MasterData ageData = genieResponse.getResult();
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(ageData);
        Assert.assertEquals(ageData.getValue(), "age");
    }

    /**
     * Scenario : To get the board data, on successful fetching of data the response will return as true
     * with MasterData in the result.
     * Given : To fetch the board data.
     * When : MasterDataType is Board.
     * Then : On success, the response will return status as TRUE and with Board data in the result.
     */
    @Test
    public void shouldGetBoardMasterData() {

        waitForGenieToBecomeIdle();

        GenieResponse<MasterData> genieResponse = activity.getMasterData(MasterDataType.BOARD);
        MasterData boardData = genieResponse.getResult();
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(boardData);
        Assert.assertEquals(boardData.getValue(), "board");
    }

    /**
     * Scenario : To get the medium data, on successful fetching of data the response will return as true
     * with MasterData in the result.
     * Given : To fetch the medium data.
     * When : MasterDataType is Medium.
     * Then : On success, the response will return status as TRUE and with Medium data in the result.
     */
    @Test
    public void shouldGetMediumMasterData() {

        waitForGenieToBecomeIdle();

        GenieResponse<MasterData> genieResponse = activity.getMasterData(MasterDataType.MEDIUM);
        MasterData masterData = genieResponse.getResult();
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(masterData);
        Assert.assertEquals(masterData.getValue(), "medium");
    }

    /**
     * Scenario : To get the subject data, on successful fetching of data the response will return as true
     * with MasterData in the result.
     * Given : To get the Subject Data.
     * When : MasterDataType is Subject.
     * Then : On success, the response will return status as TRUE and with Subject data in the result.
     */
    @Test
    public void shouldGetSubjectMasterData() {

        waitForGenieToBecomeIdle();

        GenieResponse<MasterData> genieResponse = activity.getMasterData(MasterDataType.SUBJECT);
        MasterData masterData = genieResponse.getResult();
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(masterData);
        Assert.assertEquals(masterData.getValue(), "subject");
    }

    /**
     * Scenario : To get the grade level data, on successful fetching of data the response will return as true
     * with MasterData in the result.
     * Given : To get the Grade level data.
     * When : MasterDataType is GradeLevel.
     * Then : On success, the response will return status as TRUE and with GradeLevel data in the result.
     */
    @Test
    public void shouldGetGradeLevelMasterData() {
        GenieResponse<MasterData> genieResponse = activity.getMasterData(MasterDataType.GRADELEVEL);
        MasterData masterData = genieResponse.getResult();
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(masterData);
        Assert.assertEquals(masterData.getValue(), "gradeLevel");
    }

    /**
     * Scenario : To get the age group data, on successful fetching of data the response will return as true
     * with MasterData in the result.
     * Given : To get the Age group data
     * When : MasterDataType is AgeGroup.
     * Then : On success, the response will return status as TRUE and with AgeGroup data in the result.
     */
    @Test
    public void shouldGetAgeGroupMasterData() {

        GenieResponse<MasterData> genieResponse = activity.getMasterData(MasterDataType.AGEGROUP);
        MasterData masterData = genieResponse.getResult();
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(masterData);
        Assert.assertEquals(masterData.getValue(), "ageGroup");
    }

    /**
     * Scenario : To check the response by the passing the request as null to get the MasterData.
     * Given : To get the MasterData.
     * When : MasterDataType is null.
     * Then : Error message is displayed.
     */
    @Test
    public void shouldCheckForNullMasterType() {

        GenieResponse<MasterData> genieResponse = activity.getMasterData(null);
        Assert.assertNotNull(genieResponse);
        Assert.assertFalse(genieResponse.getStatus());
    }

    /**
     * Scenario : To get the ordered related data about the platform and other platform parameters.
     * Given : To fetch the ordered related data about the platform and other platform parameters.
     * When :
     * Then : On successful fetching of the data, the response will return status as TRUE and with result set in Map.
     */
    @Test
    public void shouldGetOrdinals() {

        waitForGenieToBecomeIdle();

        GenieResponse<Map<String, Object>> genieResponse = activity.getOrdinals();
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(genieResponse.getResult());

    }

    /**
     * Scenario : To get the platform specific data, specific to locale chosen.
     * Given : To fetch the platform specific data.
     * When :
     * Then : On successful fetching the data, the response will return status as TRUE and with result set in Map.
     */
    @Test
    public void shouldGetResourceBundle() {

        waitForGenieToBecomeIdle();

        GenieResponse<Map<String, Object>> genieResponse = activity.getResourceBundle("en");
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(genieResponse.getResult());
    }

    /**
     * Scenario : To validate the platform specific data, for an invalid locale.
     * Given : For an invalid locale, validate the platform specific data.
     * When :
     * Then : Error message for invalid locale.
     */
    @Test
    public void shouldValidateResourceBundle() {

        waitForGenieToBecomeIdle();

        GenieResponse<Map<String, Object>> genieResponse = activity.getResourceBundle("se");
        Assert.assertFalse(genieResponse.getStatus());
        Assert.assertNotNull(genieResponse.getResult());
    }
}

