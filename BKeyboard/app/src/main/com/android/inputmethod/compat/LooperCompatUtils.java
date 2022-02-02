
package com.android.inputmethod.compat;

import android.os.Looper;

import java.lang.reflect.Method;

/**
 * Helper to call Looper#quitSafely, which was introduced in API
 * level 18 (Build.VERSION_CODES.JELLY_BEAN_MR2).
 *
 * In unit tests, we create lots of instances of LatinIME, which means we need to clean up
 * some Loopers lest we leak file descriptors. In normal use on a device though, this is never
 * necessary (although it does not hurt).
 */
public final class LooperCompatUtils {
    private static final Method METHOD_quitSafely = CompatUtils.getMethod(
            Looper.class, "quitSafely");

    public static void quitSafely(final Looper looper) {
        if (null != METHOD_quitSafely) {
            CompatUtils.invoke(looper, null /* default return value */, METHOD_quitSafely);
        } else {
            looper.quit();
        }
    }
}
