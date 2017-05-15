package org.ekstep.genieservices.ConfigService;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.enums.MasterDataType;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Sneha on 4/27/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ConfigServiceTest extends GenieServiceTestBase {
    private static final String TAG = ConfigServiceTest.class.getSimpleName();
    JsonParser jsonParser = new JsonParser();

    @Test
    public void _1shouldGetAgeFromMasterData() {

        waitForGenieToBecomeIdle();

        activity.getMasterData(MasterDataType.AGE, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                String age = (String) genieResponse.getResult();
                Assert.assertNotNull(age);
                getJsonValuesArrayLength(age);

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
                String board = (String) genieResponse.getResult();
                Assert.assertNotNull(board);
                getJsonValuesArrayLength(board);
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
                String medium = (String) genieResponse.getResult();
                Assert.assertNotNull(medium);
                getJsonValuesArrayLength(medium);
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
                String subject = (String) genieResponse.getResult();
                Assert.assertNotNull(subject);
                getJsonValuesArrayLength(subject);
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure", genieResponse.getErrorMessages().get(0));
            }
        });
    }

    private void getJsonValuesArrayLength(String masterDataType) {

        JsonObject boardObject = jsonParser.parse(masterDataType).getAsJsonObject();
        try {
            JsonArray boardValuesArray = boardObject.getAsJsonArray("values");
            Log.e(TAG, "getJsonValuesArrayLength :: " + boardValuesArray.size());

        } catch (JsonParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void _5shouldGetGradeLevelMasterData() {
        activity.getMasterData(MasterDataType.GRADELEVEL, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                String gradeLevel = (String) genieResponse.getResult();
                Assert.assertNotNull(gradeLevel);
                getJsonValuesArrayLength(gradeLevel);
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
                String ageGroup = (String) genieResponse.getResult();
                Assert.assertNotNull(ageGroup);
                getJsonValuesArrayLength(ageGroup);
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

