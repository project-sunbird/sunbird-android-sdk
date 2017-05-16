package org.ekstep.genieservices;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import java.io.IOException;

/**
 * Created by swayangjit on 11/5/17.
 */

public class GenieServiceTestBase {
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(
            MainActivity.class);
    protected MainActivity activity;

    @Before
    public void setup() throws IOException {

    }

    @After
    public void tearDown() throws IOException {

    }
}
