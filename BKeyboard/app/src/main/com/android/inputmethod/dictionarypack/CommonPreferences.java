

package com.android.inputmethod.dictionarypack;

import android.content.Context;
import android.content.SharedPreferences;

public final class CommonPreferences {
    private static final String COMMON_PREFERENCES_NAME = "LatinImeDictPrefs";

    public static SharedPreferences getCommonPreferences(final Context context) {
        return context.getSharedPreferences(COMMON_PREFERENCES_NAME, 0);
    }

    public static void enable(final SharedPreferences pref, final String id) {
        final SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(id, true);
        editor.apply();
    }

    public static void disable(final SharedPreferences pref, final String id) {
        final SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(id, false);
        editor.apply();
    }
}
