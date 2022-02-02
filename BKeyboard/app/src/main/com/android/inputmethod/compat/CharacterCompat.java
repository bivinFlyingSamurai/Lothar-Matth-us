
package com.android.inputmethod.compat;

import java.lang.reflect.Method;

public final class CharacterCompat {
    // Note that Character.isAlphabetic(int), has been introduced in API level 19
    // (Build.VERSION_CODE.KITKAT).
    private static final Method METHOD_isAlphabetic = CompatUtils.getMethod(
            Character.class, "isAlphabetic", int.class);

    private CharacterCompat() {
        // This utility class is not publicly instantiable.
    }

    public static boolean isAlphabetic(final int code) {
        if (METHOD_isAlphabetic != null) {
            return (Boolean)CompatUtils.invoke(null, false, METHOD_isAlphabetic, code);
        }
        switch (Character.getType(code)) {
        case Character.UPPERCASE_LETTER:
        case Character.LOWERCASE_LETTER:
        case Character.TITLECASE_LETTER:
        case Character.MODIFIER_LETTER:
        case Character.OTHER_LETTER:
        case Character.LETTER_NUMBER:
            return true;
        default:
            return false;
        }
    }
}
