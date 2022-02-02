
package com.android.inputmethod.latin.settings;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import com.Matthaus.Lothar.keyboard.R;
import com.android.inputmethod.keyboard.KeyboardTheme;
import com.android.inputmethod.latin.settings.RadioButtonPreference.OnRadioButtonClickedListener;

/**
 * "Keyboard theme" settings sub screen.
 */
public final class ThemeSettingsFragment extends SubScreenFragment
        implements OnRadioButtonClickedListener {
    private int mSelectedThemeId;

    static class KeyboardThemePreference extends RadioButtonPreference {
        final int mThemeId;

        KeyboardThemePreference(final Context context, final String name, final int id) {
            super(context);
            setTitle(name);
            mThemeId = id;
        }
    }

    static void updateKeyboardThemeSummary(final Preference pref) {
        final Context context = pref.getContext();
        final Resources res = context.getResources();
        final KeyboardTheme keyboardTheme = KeyboardTheme.getKeyboardTheme(context);
        final String[] keyboardThemeNames = res.getStringArray(R.array.keyboard_theme_names);
        final int[] keyboardThemeIds = res.getIntArray(R.array.keyboard_theme_ids);
        for (int index = 0; index < keyboardThemeNames.length; index++) {
            if (keyboardTheme.mThemeId == keyboardThemeIds[index]) {
                pref.setSummary(keyboardThemeNames[index]);
                return;
            }
        }
    }

//    @Override
//    public void onCreate(final Bundle icicle) {
//        super.onCreate(icicle);
//        addPreferencesFromResource(R.xml.prefs_screen_theme);
//        final PreferenceScreen screen = getPreferenceScreen();
//        final Context context = getActivity();
//        final Resources res = getResources();
//        final String[] keyboardThemeNames = res.getStringArray(R.array.keyboard_theme_names);
//        final int[] keyboardThemeIds = res.getIntArray(R.array.keyboard_theme_ids);
//        for (int index = 0; index < keyboardThemeNames.length; index++) {
//            final KeyboardThemePreference pref = new KeyboardThemePreference(
//                    context, keyboardThemeNames[index], keyboardThemeIds[index]);
//            screen.addPreference(pref);
//            pref.setOnRadioButtonClickedListener(this);
//        }
//        final KeyboardTheme keyboardTheme = KeyboardTheme.getKeyboardTheme(context);
//        mSelectedThemeId = keyboardTheme.mThemeId;
//    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.prefs_screen_theme, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final PreferenceScreen screen = getPreferenceScreen();
        final Context context = getActivity();
        final Resources res = getResources();
        final String[] keyboardThemeNames = res.getStringArray(R.array.keyboard_theme_names);
        final int[] keyboardThemeIds = res.getIntArray(R.array.keyboard_theme_ids);
        for (int index = 0; index < keyboardThemeNames.length; index++) {
            final KeyboardThemePreference pref = new KeyboardThemePreference(
                    context, keyboardThemeNames[index], keyboardThemeIds[index]);
            screen.addPreference(pref);
            pref.setOnRadioButtonClickedListener(this);
        }
        final KeyboardTheme keyboardTheme = KeyboardTheme.getKeyboardTheme(context);
        mSelectedThemeId = keyboardTheme.mThemeId;
    }

    @Override
    public void onRadioButtonClicked(final RadioButtonPreference preference) {
        if (preference instanceof KeyboardThemePreference) {
            final KeyboardThemePreference pref = (KeyboardThemePreference)preference;
            mSelectedThemeId = pref.mThemeId;
            updateSelected();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSelected();
    }

    @Override
    public void onPause() {
        super.onPause();
        KeyboardTheme.saveKeyboardThemeId(mSelectedThemeId, getSharedPreferences());
    }

    private void updateSelected() {
        final PreferenceScreen screen = getPreferenceScreen();
        final int count = screen.getPreferenceCount();
        for (int index = 0; index < count; index++) {
            final Preference preference = screen.getPreference(index);
            if (preference instanceof KeyboardThemePreference) {
                final KeyboardThemePreference pref = (KeyboardThemePreference)preference;
                final boolean selected = (mSelectedThemeId == pref.mThemeId);
                pref.setSelected(selected);
            }
        }
    }
}
