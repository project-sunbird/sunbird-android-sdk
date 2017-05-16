package org.ekstep.genieservices.ConfigService;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.MasterData;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Sneha on 4/27/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ConfigServiceTest extends GenieServiceTestBase {
    private static final String TAG = ConfigServiceTest.class.getSimpleName();

    @Test
    public void _1shouldGetAgeFromMasterData() {

        waitForGenieToBecomeIdle();

        activity.getMasterData(MasterDataType.AGE, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                MasterData ageData = (MasterData) genieResponse.getResult();
                Assert.assertNotNull(ageData);
                Assert.assertEquals(ageData.getValue(), "age");
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure", genieResponse.getErrorMessages().get(0));

            }
        });
    }

    @Test
    public void _2shouldGetBoardMasterData() {
        activity.getMasterData(MasterDataType.BOARD, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                MasterData boardData = (MasterData) genieResponse.getResult();
                Assert.assertNotNull(boardData);
                Assert.assertEquals(boardData.getValue(), "board");
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure", genieResponse.getErrorMessages().get(0));
            }
        });
    }

    @Test
    public void _3shouldGetMediumMasterData() {
        activity.getMasterData(MasterDataType.MEDIUM, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                MasterData mediumData = (MasterData) genieResponse.getResult();
                Assert.assertNotNull(mediumData);
                Assert.assertEquals(mediumData.getValue(), "medium");
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure", genieResponse.getErrorMessages().get(0));
            }
        });
    }

    @Test
    public void _4shouldGetSubjectMasterData() {
        activity.getMasterData(MasterDataType.SUBJECT, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                MasterData subjectData = (MasterData) genieResponse.getResult();
                Assert.assertNotNull(subjectData);
                Assert.assertEquals(subjectData.getValue(), "subject");
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure", genieResponse.getErrorMessages().get(0));
            }
        });
    }

    @Test
    public void _5shouldGetGradeLevelMasterData() {
        activity.getMasterData(MasterDataType.GRADELEVEL, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                MasterData gradeLevel = (MasterData) genieResponse.getResult();
                Assert.assertNotNull(gradeLevel);
                Assert.assertEquals(gradeLevel.getValue(), "gradeLevel");
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure", genieResponse.getErrorMessages().get(0));
            }
        });
    }

    @Test
    public void _6shouldGetAgeGroupMasterData() {

        activity.getMasterData(MasterDataType.AGEGROUP, new IResponseHandler() {

            @Override
            public void onSuccess(GenieResponse genieResponse) {
                MasterData ageGroupData = (MasterData) genieResponse.getResult();
                Assert.assertNotNull(ageGroupData);
                Assert.assertEquals(ageGroupData.getValue(), "ageGroup");
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure", genieResponse.getErrorMessages().get(0));
            }
        });
    }

    @Test
    public void _7shouldGetOrdinals() {

        waitForGenieToBecomeIdle();

        activity.getOrdinals(new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                String result = (String) genieResponse.getResult();
                Assert.assertNotNull(result);
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertFalse(genieResponse.getStatus());
            }
        });
    }

    @Test
    public void _8shouldGetResourceBundle() {

        waitForGenieToBecomeIdle();

        activity.getResourceBundle("en", new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                String result = (String) genieResponse.getResult();
                Assert.assertNotNull(result);
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertFalse(genieResponse.getStatus());
            }
        });
    }
}

