package com.zysdk.vulture.clib.corel;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * @hide
 */
abstract class AbsActLifecycle implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
