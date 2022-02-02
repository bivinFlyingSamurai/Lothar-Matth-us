

package com.android.inputmethod.compat;

import android.inputmethodservice.InputMethodService;

import java.lang.reflect.Method;

public final class InputMethodServiceCompatUtils {
    // Note that {@link InputMethodService#enableHardwareAcceleration} has been introduced
    // in API level 17 (Build.VERSION_CODES.JELLY_BEAN_MR1).
    private static final Method METHOD_enableHardwareAcceleration =
            CompatUtils.getMethod(InputMethodService.class, "enableHardwareAcceleration");

    private InputMethodServiceCompatUtils() {
        // This utility class is not publicly instantiable.
    }

    public static boolean enableHardwareAcceleration(final InputMethodService ims) {
        return (Boolean)CompatUtils.invoke(ims, false /* defaultValue */,
                METHOD_enableHardwareAcceleration);
    }
}
