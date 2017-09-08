package org.ekstep.genieservices.contentservice.content;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.MockServer;
import org.ekstep.genieservices.SampleApiResponse;
import org.ekstep.genieservices.commons.bean.ContentListing;
import org.ekstep.genieservices.commons.bean.ContentListingCriteria;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.db.contract.PageEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;

/**
 * Created by swayangjit on 8/9/17.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class ContentListingAPITest extends GenieServiceTestBase {

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
    public void _1ShouldReturnFalseStatusFor404Error() {
        startMockServer();

        mMockServer.mockHttpResponse(SampleApiResponse.getContentListingAPIResponse(), 400);

        String PARTNER_HOME_IDENTIFIER = "org.ekstep.genie.content";
        ContentListingCriteria.Builder contentListingCriteria = new ContentListingCriteria.Builder();
        contentListingCriteria.listingId(PARTNER_HOME_IDENTIFIER);

        GenieResponse<ContentListing> genieResponse = activity.getContentListing(contentListingCriteria.build());
        Assert.assertFalse(genieResponse.getStatus());
        Assert.assertEquals("No data found.", genieResponse.getErrorMessages().get(0));
        shutDownMockServer();
    }

    @Test
    public void _2ShouldInvokeGetListingAPISuccessfully() {

        startMockServer();

        mMockServer.mockHttpResponse(SampleApiResponse.getContentListingAPIResponse(), 200);

        GenieServiceDBHelper.clearTable(PageEntry.TABLE_NAME);

        String PARTNER_HOME_IDENTIFIER = "org.ekstep.genie.content.partnerhome";
        ContentListingCriteria.Builder contentListingCriteria = new ContentListingCriteria.Builder();
        contentListingCriteria.listingId(PARTNER_HOME_IDENTIFIER);

        GenieResponse<ContentListing> genieResponse = activity.getContentListing(contentListingCriteria.build());

        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertEquals(PARTNER_HOME_IDENTIFIER, genieResponse.getResult().getContentListingId());

        mMockServer.assertRequestCount(1);
        shutDownMockServer();
    }

    @Test
    public void _3ShouldResultFromDBWithoutCallingServerAPI() {


        mMockServer = new MockServer();

        String PARTNER_HOME_IDENTIFIER = "org.ekstep.genie.content.partnerhome";
        ContentListingCriteria.Builder contentListingCriteria = new ContentListingCriteria.Builder();
        contentListingCriteria.listingId(PARTNER_HOME_IDENTIFIER);

        GenieResponse<ContentListing> genieResponse = activity.getContentListing(contentListingCriteria.build());

        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertEquals(PARTNER_HOME_IDENTIFIER, genieResponse.getResult().getContentListingId());

        mMockServer.assertRequestCount(0);
        shutDownMockServer();
    }


    @Test
    public void _4ShouldInvokeServerAPIIfTtlExpired() throws InterruptedException {

        startMockServer();
        mMockServer.mockHttpResponse(SampleApiResponse.getContentListingAPIResponse(), 200);

        String PARTNER_HOME_IDENTIFIER = "org.ekstep.genie.content.partnerhome";
        ContentListingCriteria.Builder contentListingCriteria = new ContentListingCriteria.Builder();
        contentListingCriteria.listingId(PARTNER_HOME_IDENTIFIER);

        GenieServiceDBHelper.updateTtlinContentListingTable(contentListingCriteria.build());

        GenieResponse<ContentListing> genieResponse = activity.getContentListing(contentListingCriteria.build());

        Assert.assertTrue(genieResponse.getStatus());
        Assert.assertEquals(PARTNER_HOME_IDENTIFIER, genieResponse.getResult().getContentListingId());

        Thread.sleep(5000);
        mMockServer.assertRequestCount(1);
        shutDownMockServer();
    }
}
