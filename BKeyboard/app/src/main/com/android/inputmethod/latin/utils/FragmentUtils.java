package com.android.inputmethod.latin.utils;

import com.android.inputmethod.dictionarypack.DictionarySettingsFragment;
import com.android.inputmethod.latin.about.AboutPreferences;
import com.android.inputmethod.latin.settings.AccountsSettingsFragment;
import com.android.inputmethod.latin.settings.AdvancedSettingsFragment;
import com.android.inputmethod.latin.settings.AppearanceSettingsFragment;
import com.android.inputmethod.latin.settings.CorrectionSettingsFragment;
import com.android.inputmethod.latin.settings.CustomInputStyleSettingsFragment;
import com.android.inputmethod.latin.settings.DebugSettingsFragment;
import com.android.inputmethod.latin.settings.GestureSettingsFragment;
import com.android.inputmethod.latin.settings.PreferencesSettingsFragment;
import com.android.inputmethod.latin.settings.SettingsFragment;
import com.android.inputmethod.latin.settings.ThemeSettingsFragment;
import com.android.inputmethod.latin.spellcheck.SpellCheckerSettingsFragment;
import com.android.inputmethod.latin.userdictionary.UserDictionaryAddWordFragment;
import com.android.inputmethod.latin.userdictionary.UserDictionaryList;
import com.android.inputmethod.latin.userdictionary.UserDictionaryLocalePicker;
import com.android.inputmethod.latin.userdictionary.UserDictionarySettings;

import java.util.HashSet;

public class FragmentUtils {
    private static final HashSet<String> sLatinImeFragments = new HashSet<>();
    static {
        sLatinImeFragments.add(DictionarySettingsFragment.class.getName());
        sLatinImeFragments.add(AboutPreferences.class.getName());
        sLatinImeFragments.add(PreferencesSettingsFragment.class.getName());
        sLatinImeFragments.add(AccountsSettingsFragment.class.getName());
        sLatinImeFragments.add(AppearanceSettingsFragment.class.getName());
        sLatinImeFragments.add(ThemeSettingsFragment.class.getName());
        sLatinImeFragments.add(CustomInputStyleSettingsFragment.class.getName());
        sLatinImeFragments.add(GestureSettingsFragment.class.getName());
        sLatinImeFragments.add(CorrectionSettingsFragment.class.getName());
        sLatinImeFragments.add(AdvancedSettingsFragment.class.getName());
        sLatinImeFragments.add(DebugSettingsFragment.class.getName());
        sLatinImeFragments.add(SettingsFragment.class.getName());
        sLatinImeFragments.add(SpellCheckerSettingsFragment.class.getName());
        sLatinImeFragments.add(UserDictionaryAddWordFragment.class.getName());
        sLatinImeFragments.add(UserDictionaryList.class.getName());
        sLatinImeFragments.add(UserDictionaryLocalePicker.class.getName());
        sLatinImeFragments.add(UserDictionarySettings.class.getName());
    }

    public static boolean isValidFragment(String fragmentName) {
        return sLatinImeFragments.contains(fragmentName);
    }
}
