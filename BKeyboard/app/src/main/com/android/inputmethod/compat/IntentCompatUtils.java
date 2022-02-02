

package com.android.inputmethod.compat;

import android.content.Intent;

public final class IntentCompatUtils {
    // Note that Intent.ACTION_USER_INITIALIZE have been introduced in API level 17
    // (Build.VERSION_CODE.JELLY_BEAN_MR1).
    private static final String ACTION_USER_INITIALIZE =
            (String)CompatUtils.getFieldValue(null /* receiver */, null /* defaultValue */,
                    CompatUtils.getField(Intent.class, "ACTION_USER_INITIALIZE"));

    private IntentCompatUtils() {
        // This utility class is not publicly instantiable.
    }

    public static boolean is_ACTION_USER_INITIALIZE(final String action) {
        return ACTION_USER_INITIALIZE != null && ACTION_USER_INITIALIZE.equals(action);
    }
}
