package org.ekstep.genieservices;

import android.os.Environment;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static java.text.MessageFormat.format;

/**
 * Created by Sneha on 4/27/2017.
 */

public class GenieServiceTestBase {
    public static final int DEFAULT_TIMEOUT_IN_SECONDS = 30;
    public static final int MASTER_POLICY_TIMEOUT_IN_MINS = 1;
    public static final int IDLING_RESOURCE_TIMEOUT_IN_MINS = 1;
    private final String GS_DB = "/data/data/org.ekstep.genieservices/databases/GenieServices.db";

    @Rule
    public ActivityTestRule<GenieServiceTestActivity> rule = new ActivityTestRule<GenieServiceTestActivity>
            (GenieServiceTestActivity.class);
    protected GenieServiceTestActivity activity;
    String COPY_OF_GS_DB = format("{0}/Download/GenieServices_copy.db",
            Environment.getExternalStorageDirectory().getPath());
    private GenieServiceIdlingResource idlingResource;

    @Before
    public void setup() throws IOException {

        IdlingPolicies.setMasterPolicyTimeout(MASTER_POLICY_TIMEOUT_IN_MINS, TimeUnit.MINUTES);
        IdlingPolicies.setIdlingResourceTimeout(IDLING_RESOURCE_TIMEOUT_IN_MINS, TimeUnit.MINUTES);

        activity = rule.getActivity();
        idlingResource = new GenieServiceIdlingResource(activity);

        Espresso.registerIdlingResources(idlingResource);

        activity.setIdle();
    }

    @After
    public void tearDown() throws IOException {
        // Below verification, contrary to how it may appear, is required to make Espresso check on idling resources.
        // Do not remove unless you understand the implications of it
        Espresso.unregisterIdlingResources(idlingResource);

        activity.setIdle();
    }

    protected void waitForGenieToBecomeIdle(int... timeoutOverride) {

        int timeout = DEFAULT_TIMEOUT_IN_SECONDS;

        if (timeoutOverride.length > 0) {
            timeout = timeoutOverride[0];
        }

        await().dontCatchUncaughtExceptions()
                .atMost(timeout, TimeUnit.SECONDS)
                .until(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        return activity.isIdle();
                    }
                });
    }
}
