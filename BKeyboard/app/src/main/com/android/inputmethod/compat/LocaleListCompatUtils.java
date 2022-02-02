

package com.android.inputmethod.compat;

import java.lang.reflect.Method;
import java.util.Locale;

public final class LocaleListCompatUtils {
    private static final Class CLASS_LocaleList = CompatUtils.getClass("android.os.LocaleList");
    private static final Method METHOD_get =
            CompatUtils.getMethod(CLASS_LocaleList, "get", int.class);
    private static final Method METHOD_isEmpty =
            CompatUtils.getMethod(CLASS_LocaleList, "isEmpty");

    private LocaleListCompatUtils() {
        // This utility class is not publicly instantiable.
    }

    public static boolean isEmpty(final Object localeList) {
        return (Boolean) CompatUtils.invoke(localeList, Boolean.FALSE, METHOD_isEmpty);
    }

    public static Locale get(final Object localeList, final int index) {
        return (Locale) CompatUtils.invoke(localeList, null, METHOD_get, index);
    }
}
