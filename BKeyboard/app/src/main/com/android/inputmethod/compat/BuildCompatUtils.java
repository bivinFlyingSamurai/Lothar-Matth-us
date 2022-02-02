
package com.android.inputmethod.compat;

import android.os.Build;

public final class BuildCompatUtils {
    private BuildCompatUtils() {
        // This utility class is not publicly instantiable.
    }

    private static final boolean IS_RELEASE_BUILD = Build.VERSION.CODENAME.equals("REL");

    /**
     * The "effective" API version.
     * {@link Build.VERSION#SDK_INT} if the platform is a release build.
     * {@link Build.VERSION#SDK_INT} plus 1 if the platform is a development build.
     */
    public static final int EFFECTIVE_SDK_INT = IS_RELEASE_BUILD
            ? Build.VERSION.SDK_INT
            : Build.VERSION.SDK_INT + 1;
}
