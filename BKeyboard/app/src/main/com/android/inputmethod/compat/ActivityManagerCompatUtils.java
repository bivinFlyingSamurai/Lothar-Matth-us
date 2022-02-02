

package com.android.inputmethod.compat;

import android.app.ActivityManager;
import android.content.Context;

import java.lang.reflect.Method;

public class ActivityManagerCompatUtils {
    private static final Object LOCK = new Object();
    private static volatile Boolean sBoolean = null;
    private static final Method METHOD_isLowRamDevice = CompatUtils.getMethod(
            ActivityManager.class, "isLowRamDevice");

    private ActivityManagerCompatUtils() {
        // Do not instantiate this class.
    }

    public static boolean isLowRamDevice(Context context) {
        if (sBoolean == null) {
            synchronized(LOCK) {
                if (sBoolean == null) {
                    final ActivityManager am =
                            (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    sBoolean = (Boolean)CompatUtils.invoke(am, false, METHOD_isLowRamDevice);
                }
            }
        }
        return sBoolean;
    }
}
