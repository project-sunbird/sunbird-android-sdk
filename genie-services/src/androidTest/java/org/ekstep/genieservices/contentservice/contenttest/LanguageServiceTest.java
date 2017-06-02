package org.ekstep.genieservices.contentservice.contenttest;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Sneha on 6/2/2017.
 */

@RunWith(AndroidJUnit4.class)
public class LanguageServiceTest extends GenieServiceTestBase {
    private final static String TAG = LanguageServiceTest.class.getSimpleName();

    @Test
    public void getLanguageTraversalRule() {
        GenieResponse<String> genieResponse = activity.getLanguageTraversalRule("en");
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(genieResponse.getResult());
        Log.v(TAG, "genieResponse :: " + genieResponse.getStatus());
        Log.v(TAG, "genieResponse result :: " + genieResponse.getResult());
    }

    @Test
    public void getLanguageSearch() {
        //TODO : check what is the requestData to be passed to getLanguageSearch().
//        GenieResponse<String> genieResponse = activity.getLanguageSearch();
//        Log.v(TAG, "genieResponse :: " + genieResponse.getStatus());
    }

}
