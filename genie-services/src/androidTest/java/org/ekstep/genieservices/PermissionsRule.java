package org.ekstep.genieservices;

/**
 * Created by Sneha on 7/2/2017.
 */

import android.os.Build;
import android.support.test.InstrumentationRegistry;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * This rule adds selected permissions to test app
 */

public class PermissionsRule implements TestRule {

    private final String[] permissions;

    public PermissionsRule(String[] permissions) {
        this.permissions = permissions;
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {

                allowPermissions();

                base.evaluate();

                revokePermissions();
            }
        };
    }

    private void allowPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(
                        "pm grant " + InstrumentationRegistry.getTargetContext().getPackageName()
                                + " " + permission);
            }
        }
    }

    private void revokePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(
                        "pm revoke " + InstrumentationRegistry.getTargetContext().getPackageName()
                                + " " + permission);
            }
        }
    }
}
