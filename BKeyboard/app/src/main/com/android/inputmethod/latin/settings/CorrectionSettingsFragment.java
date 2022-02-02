
package com.android.inputmethod.latin.settings;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

import com.Matthaus.Lothar.keyboard.R;
import com.android.inputmethod.dictionarypack.DictionarySettingsActivity;
import com.android.inputmethod.latin.permissions.PermissionsManager;
import com.android.inputmethod.latin.permissions.PermissionsUtil;
import com.android.inputmethod.latin.userdictionary.UserDictionaryList;
import com.android.inputmethod.latin.userdictionary.UserDictionarySettings;

import java.util.TreeSet;

/**
 * "Text correction" settings sub screen.
 *
 * This settings sub screen handles the following text correction preferences.
 * - Personal dictionary
 * - Add-on dictionaries
 * - Block offensive words
 * - Auto-correction
 * - Show correction suggestions
 * - Personalized suggestions
 * - Suggest Contact names
 * - Next-word suggestions
 */
public final class CorrectionSettingsFragment extends SubScreenFragment
    implements SharedPreferences.OnSharedPreferenceChangeListener,
            PermissionsManager.PermissionsResultCallback {

    private static final boolean DBG_USE_INTERNAL_PERSONAL_DICTIONARY_SETTINGS = false;
    private static final boolean USE_INTERNAL_PERSONAL_DICTIONARY_SETTINGS =
            DBG_USE_INTERNAL_PERSONAL_DICTIONARY_SETTINGS
            || Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2;

    private SwitchPreference mUseContactsPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.prefs_screen_correction, rootKey);

        final Context context = getActivity();
        final PackageManager pm = context.getPackageManager();

        final Preference dictionaryLink = findPreference(Settings.PREF_CONFIGURE_DICTIONARIES_KEY);
        final Intent intent = dictionaryLink.getIntent();
        intent.setClassName(context.getPackageName(), DictionarySettingsActivity.class.getName());
        final int number = pm.queryIntentActivities(intent, 0).size();
        if (0 >= number) {
            removePreference(Settings.PREF_CONFIGURE_DICTIONARIES_KEY);
        }

        final Preference editPersonalDictionary =
                findPreference(Settings.PREF_EDIT_PERSONAL_DICTIONARY);
        final Intent editPersonalDictionaryIntent = editPersonalDictionary.getIntent();
        final ResolveInfo ri = USE_INTERNAL_PERSONAL_DICTIONARY_SETTINGS ? null
                : pm.resolveActivity(
                editPersonalDictionaryIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (ri == null) {
            overwriteUserDictionaryPreference(editPersonalDictionary);
        }

        mUseContactsPreference = (SwitchPreference) findPreference(Settings.PREF_KEY_USE_CONTACTS_DICT);
        turnOffUseContactsIfNoPermission();
    }

//    @Override
//    public void onCreate(final Bundle icicle) {
//        super.onCreate(icicle);
//        addPreferencesFromResource(R.xml.prefs_screen_correction);
//
//        final Context context = getActivity();
//        final PackageManager pm = context.getPackageManager();
//
//        final Preference dictionaryLink = findPreference(Settings.PREF_CONFIGURE_DICTIONARIES_KEY);
//        final Intent intent = dictionaryLink.getIntent();
//        intent.setClassName(context.getPackageName(), DictionarySettingsActivity.class.getName());
//        final int number = pm.queryIntentActivities(intent, 0).size();
//        if (0 >= number) {
//            removePreference(Settings.PREF_CONFIGURE_DICTIONARIES_KEY);
//        }
//
//        final Preference editPersonalDictionary =
//                findPreference(Settings.PREF_EDIT_PERSONAL_DICTIONARY);
//        final Intent editPersonalDictionaryIntent = editPersonalDictionary.getIntent();
//        final ResolveInfo ri = USE_INTERNAL_PERSONAL_DICTIONARY_SETTINGS ? null
//                : pm.resolveActivity(
//                        editPersonalDictionaryIntent, PackageManager.MATCH_DEFAULT_ONLY);
//        if (ri == null) {
//            overwriteUserDictionaryPreference(editPersonalDictionary);
//        }
//
//        mUseContactsPreference = (SwitchPreference) findPreference(Settings.PREF_KEY_USE_CONTACTS_DICT);
//        turnOffUseContactsIfNoPermission();
//    }

    private void overwriteUserDictionaryPreference(final Preference userDictionaryPreference) {
        final Activity activity = getActivity();
        final TreeSet<String> localeList = UserDictionaryList.getUserDictionaryLocalesSet(activity);
        if (null == localeList) {
            // The locale list is null if and only if the user dictionary service is
            // not present or disabled. In this case we need to remove the preference.
            getPreferenceScreen().removePreference(userDictionaryPreference);
        } else if (localeList.size() <= 1) {
            userDictionaryPreference.setFragment(UserDictionarySettings.class.getName());
            // If the size of localeList is 0, we don't set the locale parameter in the
            // extras. This will be interpreted by the UserDictionarySettings class as
            // meaning "the current locale".
            // Note that with the current code for UserDictionaryList#getUserDictionaryLocalesSet()
            // the locale list always has at least one element, since it always includes the current
            // locale explicitly. @see UserDictionaryList.getUserDictionaryLocalesSet().
            if (localeList.size() == 1) {
                final String locale = (String)localeList.toArray()[0];
                userDictionaryPreference.getExtras().putString("locale", locale);
            }
        } else {
            userDictionaryPreference.setFragment(UserDictionaryList.class.getName());
        }
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        if (!TextUtils.equals(key, Settings.PREF_KEY_USE_CONTACTS_DICT)) {
            return;
        }
        if (!sharedPreferences.getBoolean(key, false)) {
            // don't care if the preference is turned off.
            return;
        }

        // Check for permissions.
        if (PermissionsUtil.checkAllPermissionsGranted(
                getActivity() /* context */, Manifest.permission.READ_CONTACTS)) {
            return; // all permissions granted, no need to request permissions.
        }

        PermissionsManager.get(getActivity() /* context */).requestPermissions(
                this /* PermissionsResultCallback */,
                getActivity() /* activity */,
                Manifest.permission.READ_CONTACTS);
    }

    @Override
    public void onRequestPermissionsResult(boolean allGranted) {
        turnOffUseContactsIfNoPermission();
    }

    private void turnOffUseContactsIfNoPermission() {
        if (!PermissionsUtil.checkAllPermissionsGranted(
                getActivity(), Manifest.permission.READ_CONTACTS)) {
            mUseContactsPreference.setChecked(false);
        }
    }
}
