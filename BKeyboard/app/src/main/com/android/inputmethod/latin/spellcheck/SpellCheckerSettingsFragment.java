

package com.android.inputmethod.latin.spellcheck;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.Matthaus.Lothar.keyboard.R;
import com.android.inputmethod.latin.permissions.PermissionsManager;
import com.android.inputmethod.latin.permissions.PermissionsUtil;
import com.android.inputmethod.latin.settings.SubScreenFragment;
import com.android.inputmethod.latin.settings.TwoStatePreferenceHelper;
import com.android.inputmethod.latin.utils.ApplicationUtils;

import static com.android.inputmethod.latin.permissions.PermissionsManager.get;

/**
 * Preference screen.
 */
public final class SpellCheckerSettingsFragment extends SubScreenFragment
    implements SharedPreferences.OnSharedPreferenceChangeListener,
            PermissionsManager.PermissionsResultCallback {

    private SwitchPreference mLookupContactsPreference;

//    @Override
//    public void onActivityCreated(final Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        addPreferencesFromResource(R.xml.spell_checker_settings);
//        final PreferenceScreen preferenceScreen = getPreferenceScreen();
//        preferenceScreen.setTitle(ApplicationUtils.getActivityTitleResId(
//                getActivity(), SpellCheckerSettingsActivity.class));
//        TwoStatePreferenceHelper.replaceCheckBoxPreferencesBySwitchPreferences(preferenceScreen);
//
//        mLookupContactsPreference = findPreference(
//                AndroidSpellCheckerService.PREF_USE_CONTACTS_KEY);
//        turnOffLookupContactsIfNoPermission();
//    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.spell_checker_settings, rootKey);
        final PreferenceScreen preferenceScreen = getPreferenceScreen();
        preferenceScreen.setTitle(ApplicationUtils.getActivityTitleResId(
                getActivity(), SpellCheckerSettingsActivity.class));
        TwoStatePreferenceHelper.replaceCheckBoxPreferencesBySwitchPreferences(preferenceScreen);

        mLookupContactsPreference = findPreference(
                AndroidSpellCheckerService.PREF_USE_CONTACTS_KEY);
        turnOffLookupContactsIfNoPermission();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (!TextUtils.equals(key, AndroidSpellCheckerService.PREF_USE_CONTACTS_KEY)) {
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

        get(getActivity() /* context */).requestPermissions(this /* PermissionsResultCallback */,
                getActivity() /* activity */, Manifest.permission.READ_CONTACTS);
    }

    @Override
    public void onRequestPermissionsResult(boolean allGranted) {
        turnOffLookupContactsIfNoPermission();
    }

    private void turnOffLookupContactsIfNoPermission() {
        if (!PermissionsUtil.checkAllPermissionsGranted(
                getActivity(), Manifest.permission.READ_CONTACTS)) {
            mLookupContactsPreference.setChecked(false);
        }
    }
}
