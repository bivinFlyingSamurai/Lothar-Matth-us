
package com.android.inputmethod.latin.settings;

import android.os.Bundle;

import com.Matthaus.Lothar.keyboard.R;
import com.android.inputmethod.latin.common.Constants;
import com.android.inputmethod.latin.define.ProductionFlags;

/**
 * "Appearance" settings sub screen.
 */
public final class AppearanceSettingsFragment extends SubScreenFragment {
//    @Override
//    public void onCreate(final Bundle icicle) {
//        super.onCreate(icicle);
//        addPreferencesFromResource(R.xml.prefs_screen_appearance);
//        if (!ProductionFlags.IS_SPLIT_KEYBOARD_SUPPORTED ||
//                Constants.isPhone(Settings.readScreenMetrics(getResources()))) {
//            removePreference(Settings.PREF_ENABLE_SPLIT_KEYBOARD);
//        }
//    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.prefs_screen_appearance, rootKey);
        if (!ProductionFlags.IS_SPLIT_KEYBOARD_SUPPORTED ||
                Constants.isPhone(Settings.readScreenMetrics(getResources()))) {
            removePreference(Settings.PREF_ENABLE_SPLIT_KEYBOARD);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        CustomInputStyleSettingsFragment.updateCustomInputStylesSummary(
//                findPreference(Settings.PREF_CUSTOM_INPUT_STYLES));
//        ThemeSettingsFragment.updateKeyboardThemeSummary(findPreference(Settings.SCREEN_THEME));
    }
}
