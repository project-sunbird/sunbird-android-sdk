package org.ekstep.genieservices.userprofile;

import org.ekstep.genieservices.GenieServiceDBHelper;
import org.ekstep.genieservices.GenieServiceTestBase;
import org.ekstep.genieservices.SampleResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentDetails;
import org.ekstep.genieservices.commons.bean.LearnerAssessmentSummary;
import org.ekstep.genieservices.commons.bean.SummaryRequest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.List;

/**
 * Created by swayangjit on 5/9/17.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SummarizerServiceTest extends GenieServiceTestBase {

    private static String SUMMARIZED_CONTENTID = "do_30013486";

    @Before
    public void setup() throws IOException {
        super.setup();
        activity = rule.getActivity();
        GenieServiceDBHelper.clearProfileTable();
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }

    @Test
    public void _11ShouldPopulateLearnerSummaryDetailsTable() throws InterruptedException {
        activity.saveTelemetry(SampleResponse.getSampleOEEndEvent());
        Thread.sleep(15000);
        Assert.assertEquals(1, GenieServiceDBHelper.findLearnerContentSummaryDetails(SUMMARIZED_CONTENTID).size());

    }

    @Test
    public void _21ShouldPopulateLearnerAssementDetailsTable() throws InterruptedException {
        activity.saveTelemetry(SampleResponse.getSampleOEACESSEvent());
        Thread.sleep(5000);
        Assert.assertEquals(1, GenieServiceDBHelper.findLearnerAssesmentDetails().size());

    }

    @Test
    public void _22ShouldUpdateLearnerAssementDetailsTable() throws InterruptedException {
        activity.saveTelemetry(SampleResponse.getSampleOEACESSEvent());
        Thread.sleep(5000);
        Assert.assertEquals(1, GenieServiceDBHelper.findLearnerAssesmentDetails().size());

    }

    @Test
    public void _31ShouldReturnSummaryForGivenUid() throws InterruptedException {
        SummaryRequest summaryRequest = new SummaryRequest.Builder().uid(activity.getAnonymousUser().getResult().getUid()).build();
        GenieResponse<List<LearnerAssessmentSummary>> genieResponse = activity.getSummary(summaryRequest);
//        Assert.assertEquals(1, genieResponse.getResult().size());

    }

    @Test
    public void _32ShouldReturnSummaryForGivenContentId() throws InterruptedException {
        SummaryRequest summaryRequest = new SummaryRequest.Builder().contentId(SUMMARIZED_CONTENTID).build();
        GenieResponse<List<LearnerAssessmentSummary>> genieResponse = activity.getSummary(summaryRequest);
//        Assert.assertEquals(1, genieResponse.getResult().size());

    }

    @Test
    public void _41ShouldReturnLearnerAssessmentDetails() throws InterruptedException {
        SummaryRequest summaryRequest = new SummaryRequest.Builder().uid(activity.getAnonymousUser().getResult().getUid()).contentId(SUMMARIZED_CONTENTID).hierarchyData("do_2121925679111454721253/do_30019820").build();
        GenieResponse<List<LearnerAssessmentDetails>> genieResponse = activity.getLearnerAssessmentDetails(summaryRequest);
//        Assert.assertEquals(1, genieResponse.getResult().size());

    }


}
