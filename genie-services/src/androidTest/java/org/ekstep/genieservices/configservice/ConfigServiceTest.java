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

    @Test
    public void shouldGetAgeFromMasterData() {

        waitForGenieToBecomeIdle();

        GenieResponse<MasterData> genieResponse = activity.getMasterData(MasterDataType.AGE);
        MasterData ageData = genieResponse.getResult();
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(ageData);
        Assert.assertEquals(ageData.getValue(), "age");
    }

    @Test
    public void shouldGetBoardMasterData() {

        waitForGenieToBecomeIdle();

        GenieResponse<MasterData> genieResponse = activity.getMasterData(MasterDataType.BOARD);
        MasterData boardData = genieResponse.getResult();
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(boardData);
        Assert.assertEquals(boardData.getValue(), "board");
    }

    @Test
    public void shouldGetMediumMasterData() {

        waitForGenieToBecomeIdle();

        GenieResponse<MasterData> genieResponse = activity.getMasterData(MasterDataType.MEDIUM);
        MasterData masterData = genieResponse.getResult();
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(masterData);
        Assert.assertEquals(masterData.getValue(), "medium");
    }

    @Test
    public void shouldGetSubjectMasterData() {

        waitForGenieToBecomeIdle();

        GenieResponse<MasterData> genieResponse = activity.getMasterData(MasterDataType.SUBJECT);
        MasterData masterData = genieResponse.getResult();
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(masterData);
        Assert.assertEquals(masterData.getValue(), "subject");
    }

    @Test
    public void shouldGetGradeLevelMasterData() {
        GenieResponse<MasterData> genieResponse = activity.getMasterData(MasterDataType.GRADELEVEL);
        MasterData masterData = genieResponse.getResult();
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(masterData);
        Assert.assertEquals(masterData.getValue(), "gradeLevel");
    }

    @Test
    public void shouldGetAgeGroupMasterData() {

        GenieResponse<MasterData> genieResponse = activity.getMasterData(MasterDataType.AGEGROUP);
        MasterData masterData = genieResponse.getResult();
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(masterData);
        Assert.assertEquals(masterData.getValue(), "ageGroup");
    }

    @Test
    public void shouldGetOrdinals() {

        waitForGenieToBecomeIdle();

        GenieResponse<Map<String, Object>> genieResponse = activity.getOrdinals();
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(genieResponse.getResult());

    }

    @Test
    public void shouldGetResourceBundle() {

        waitForGenieToBecomeIdle();

        GenieResponse<Map<String, Object>> genieResponse = activity.getResourceBundle("en");
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(genieResponse.getResult());
    }
}

