

package com.android.inputmethod.compat;

import java.lang.reflect.Field;

public final class SettingsSecureCompatUtils {
    // Note that Settings.Secure.ACCESSIBILITY_SPEAK_PASSWORD has been introduced
    // in API level 15 (Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1).
    private static final Field FIELD_ACCESSIBILITY_SPEAK_PASSWORD = CompatUtils.getField(
            android.provider.Settings.Secure.class, "ACCESSIBILITY_SPEAK_PASSWORD");

    private SettingsSecureCompatUtils() {
        // This class is non-instantiable.
    }

    /**
     * Whether to speak passwords while in accessibility mode.
     */
    public static final String ACCESSIBILITY_SPEAK_PASSWORD = (String) CompatUtils.getFieldValue(
            null /* receiver */, null /* defaultValue */, FIELD_ACCESSIBILITY_SPEAK_PASSWORD);
}
