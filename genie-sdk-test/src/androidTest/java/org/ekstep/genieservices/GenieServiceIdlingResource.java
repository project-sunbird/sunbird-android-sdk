package org.ekstep.genieservices;

import android.support.test.espresso.IdlingResource;


public class GenieServiceIdlingResource implements IdlingResource {
    private ResourceCallback resourceCallback;
    private GenieServiceTestActivity genieServiceTestActivity;

    public GenieServiceIdlingResource(GenieServiceTestActivity genieServicesTestActivity) {
        this.genieServiceTestActivity = genieServicesTestActivity;
    }

    @Override
    public String getName() {
        return GenieServiceIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = genieServiceTestActivity.isIdle();

        if (idle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }
}
