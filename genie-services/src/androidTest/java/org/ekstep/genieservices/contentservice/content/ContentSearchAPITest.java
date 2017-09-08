package org.ekstep.genieservices.contentservice.content;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.SampleApiResponse;
import org.ekstep.genieservices.commons.bean.ContentSearchCriteria;
import org.ekstep.genieservices.commons.bean.ContentSearchResult;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.Map;

/**
 * Created by swayangjit on 8/9/17.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class ContentSearchAPITest extends GenieServiceTestBase {

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
    public void _1ShouldInvokeContentSearchAPISuccessfully() {
        startMockServer();
        mMockServer.mockHttpResponse(SampleApiResponse.getSerachAPIResult(), 200);
        ContentSearchCriteria.SearchBuilder contentSearchCriteria = new ContentSearchCriteria.SearchBuilder().query("worksheet").limit(10);
        GenieResponse<ContentSearchResult> response = activity.searchContent(contentSearchCriteria.build());
        Assert.assertTrue(response.getStatus());
        Assert.assertNotNull(response.getResult().getContentDataList());
        Assert.assertEquals("worksheet", response.getResult().getRequest().get("query"));
        Map responseObj = GsonUtil.fromMap(response.getResult().getRequest(), Map.class);
        Assert.assertEquals(10.0, responseObj.get("limit"));
        shutDownMockServer();
    }

    @Test
    public void _2ShouldReturnSuccessResponseIfNoResultAvailable() {
        startMockServer();
        mMockServer.mockHttpResponse(SampleApiResponse.getEmptySerachAPIResult(), 200);
        ContentSearchCriteria.SearchBuilder contentSearchCriteria = new ContentSearchCriteria.SearchBuilder().query("worksheet").limit(10);
        GenieResponse<ContentSearchResult> response = activity.searchContent(contentSearchCriteria.build());
        Assert.assertTrue(response.getStatus());
        Assert.assertNotNull(response.getResult().getContentDataList());
        Assert.assertEquals("worksheet", response.getResult().getRequest().get("query"));
        shutDownMockServer();
    }

    @Test
    public void _3ShouldReturnFailureResponseFor404Error() {
        startMockServer();
        mMockServer.mockHttpResponse(SampleApiResponse.getEmptySerachAPIResult(), 400);
        ContentSearchCriteria.SearchBuilder contentSearchCriteria = new ContentSearchCriteria.SearchBuilder().query("worksheet").limit(10);
        GenieResponse<ContentSearchResult> response = activity.searchContent(contentSearchCriteria.build());
        Assert.assertFalse(response.getStatus());
        shutDownMockServer();
    }

}
