

package com.android.inputmethod.dictionarypack;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.android.inputmethod.latin.utils.FragmentUtils;

/**
 * Preference screen.
 */
public final class DictionarySettingsActivity extends PreferenceActivity {
    private static final String DEFAULT_FRAGMENT = DictionarySettingsFragment.class.getName();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Intent getIntent() {
        final Intent modIntent = new Intent(super.getIntent());
        modIntent.putExtra(EXTRA_SHOW_FRAGMENT, DEFAULT_FRAGMENT);
        modIntent.putExtra(EXTRA_NO_HEADERS, true);
        // Important note : the original intent should contain a String extra with the key
        // DictionarySettingsFragment.DICT_SETTINGS_FRAGMENT_CLIENT_ID_ARGUMENT so that the
        // fragment can know who the client is.
        return modIntent;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean isValidFragment(String fragmentName) {
        return FragmentUtils.isValidFragment(fragmentName);
    }
}
