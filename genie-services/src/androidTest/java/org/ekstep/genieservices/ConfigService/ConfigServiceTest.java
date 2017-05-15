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
                Assert.assertTrue("successful", genieResponse.getStatus());
                String age = (String) genieResponse.getResult();
                getJsonValuesArrayLength(age);
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure", genieResponse.getErrorMessages().get(0));
                Log.e("", "onError: onError block");

            }
        });
    }

    @Test
    public void _2shouldGetBoardMasterData() {
        activity.getMasterData(MasterDataType.BOARD, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                Assert.assertTrue("successful", genieResponse.getStatus());

                String board = (String) genieResponse.getResult();

                getJsonValuesArrayLength(board);
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure", genieResponse.getErrorMessages().get(0));
                Log.e("", "onError: onError block");
            }
        });
    }

    @Test
    public void _3shouldGetMediumMasterData() {
        activity.getMasterData(MasterDataType.MEDIUM, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                Assert.assertTrue("successful", genieResponse.getStatus());

                String medium = (String) genieResponse.getResult();
                getJsonValuesArrayLength(medium);
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure", genieResponse.getErrorMessages().get(0));
                Log.e("", "onError: onError block");
            }
        });
    }

    @Test
    public void _4shouldGetSubjectMasterData() {
        activity.getMasterData(MasterDataType.SUBJECT, new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                Assert.assertTrue("successful", genieResponse.getStatus());

                String subject = (String) genieResponse.getResult();
                getJsonValuesArrayLength(subject);
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure", genieResponse.getErrorMessages().get(0));
                Log.e("", "onError: onError block");
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
                Assert.assertTrue("successful", genieResponse.getStatus());
                String gradeLevel = (String) genieResponse.getResult();
                getJsonValuesArrayLength(gradeLevel);
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure", genieResponse.getErrorMessages().get(0));
                Log.e("", "onError: onError block");
            }
        });
    }

    @Test
    public void _6shouldGetAgeGroupMasterData() {

        activity.getMasterData(MasterDataType.AGEGROUP, new IResponseHandler() {

            @Override
            public void onSuccess(GenieResponse genieResponse) {
                Assert.assertTrue("successful", genieResponse.getStatus());
                String ageGroup = (String) genieResponse.getResult();
                getJsonValuesArrayLength(ageGroup);
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Assert.assertEquals("Failure", genieResponse.getErrorMessages().get(0));
                Log.e("", "onError: onError block");
            }
        });
    }

    @Test
    public void _7shouldGetOrdinals() {

        waitForGenieToBecomeIdle();

        activity.getOrdinals(new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                Assert.assertTrue("successful", genieResponse.getStatus());

                String result = (String) genieResponse.getResult();
                Log.e(TAG, "onSuccess: result :: " + result);
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Log.e(TAG, "onError: " + genieResponse.getStatus());
            }
        });
    }

    @Test
    public void _8shouldGetResourceBundle() {

        waitForGenieToBecomeIdle();

        activity.getResourceBundle("en", new IResponseHandler() {
            @Override
            public void onSuccess(GenieResponse genieResponse) {
                Assert.assertTrue("successful", genieResponse.getStatus());

                String result = (String) genieResponse.getResult();
                Log.v(TAG, "onSuccess: result :: " + result);
            }

            @Override
            public void onError(GenieResponse genieResponse) {
                Log.e(TAG, "onError: " + genieResponse.getStatus());
            }
        });
    }
}

