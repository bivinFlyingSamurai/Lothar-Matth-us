
package com.android.inputmethod.latin.settings;

import android.os.Bundle;

import com.Matthaus.Lothar.keyboard.R;

/**
 * "Gesture typing preferences" settings sub screen.
 *
 * This settings sub screen handles the following gesture typing preferences.
 * - Enable gesture typing
 * - Dynamic floating preview
 * - Show gesture trail
 * - Phrase gesture
 */
public final class GestureSettingsFragment extends SubScreenFragment {
//    @Override
//    public void onCreate(final Bundle icicle) {
//        super.onCreate(icicle);
//        addPreferencesFromResource(R.xml.prefs_screen_gesture);
//    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.prefs_screen_gesture, rootKey);
    }
}
