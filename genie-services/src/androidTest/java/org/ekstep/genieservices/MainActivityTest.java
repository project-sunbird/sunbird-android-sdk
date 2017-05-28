package org.ekstep.genieservices;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

/**
 * Created by swayangjit on 10/5/17.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends GenieServiceTestBase {
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);
    private MainActivity activity;

    @Before
    public void setup() throws IOException {
        super.setup();
        activity = rule.getActivity();

    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }

    @Test
    public void useAppContext() throws Exception {
//       activity.getMasterData(MasterDataType.AGE);
        GenieServiceDBHelper.findEventById("GE_SERVICE_API_CALL");
    }
}
