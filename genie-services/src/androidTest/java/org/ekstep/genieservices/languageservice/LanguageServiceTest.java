package org.ekstep.genieservices.languageservice;

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
    public void _1getLanguageTraversalRule() {
        GenieResponse<String> genieResponse = activity.getLanguageTraversalRule("en");
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(genieResponse.getResult());
        Assert.assertNull(genieResponse.getError());
    }

    @Test
    public void _2getLanguageTraversalRule() {
        GenieResponse<String> genieResponse = activity.getLanguageTraversalRule("se");
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(genieResponse.getResult());
        Assert.assertNull(genieResponse.getError());
    }

    @Test
    public void _3getLanguageTraversalRule() {
        //TODO : check what is the requestData to be passed to getLanguageSearch().
        GenieResponse<String> genieResponse = activity.getLanguageTraversalRule(null);
        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertNotNull(genieResponse.getResult());
        Assert.assertNull(genieResponse.getError());
    }

    // what should be the search string.
    @Test
    public void _1getLanguageSearch() {
        GenieResponse<String> genieResponse = activity.getLanguageSearch("en");

        Log.v(TAG, "getLanguageSearchRequestDataValidation genieResponse :: " + genieResponse.getStatus());
        Log.v(TAG, "getLanguageSearchRequestDataValidation genieResponse result:: " + genieResponse.getResult());
    }

    @Test
    public void _2getLanguageSearch() {
        GenieResponse<String> genieResponse = activity.getLanguageSearch(null);
        Assert.assertFalse(genieResponse.getStatus());

        Log.v(TAG, "getLanguageSearchRequestDataValidation genieResponse :: " + genieResponse.getStatus());
        Log.v(TAG, "getLanguageSearchRequestDataValidation genieResponse result:: " + genieResponse.getError());
        Log.v(TAG, "getLanguageSearchRequestDataValidation genieResponse result:: " + genieResponse.getErrorMessages().get(0));
    }

    @Test
    public void _3getLanguageSearch() {
        GenieResponse<String> genieResponse = activity.getLanguageSearch("");

        Log.v(TAG, "getLanguageSearch genieResponse :: " + genieResponse.getStatus());
        Log.v(TAG, "getLanguageSearch genieResponse result:: " + genieResponse.getResult());
    }

}
