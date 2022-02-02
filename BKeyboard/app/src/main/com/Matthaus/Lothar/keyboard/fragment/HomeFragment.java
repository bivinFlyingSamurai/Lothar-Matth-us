package com.Matthaus.Lothar.keyboard.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.Matthaus.Lothar.keyboard.R;
import com.android.inputmethod.latin.settings.Settings;
import com.google.android.material.textview.MaterialTextView;

import static com.Matthaus.Lothar.keyboard.fragment.LanguageFragment.CURRENT_LANGUAGE;
import static com.Matthaus.Lothar.keyboard.fragment.ThemeFragment.LXX_KEYBOARD_THEME_KEY;
import static com.Matthaus.Lothar.keyboard.fragment.ThemeFragment.THEME_ID_LXX_DARK_N;
import static com.Matthaus.Lothar.keyboard.fragment.ThemeFragment.THEME_ID_LXX_DARK_Y;
import static com.android.inputmethod.latin.settings.Settings.PREF_VIBRATE_ON;

/**
 * Created by Akshay Raj on 28/09/20 at 6:49 pm.
 * support@snowcorp.org
 * www.snowcorp.org
 */

public class HomeFragment extends Fragment {
    private Context mContext;
    private SharedPreferences prefs;
    public static final String CURRENT_KEYBOARD = "sn_current_keyboard";

    private final String[] kbd_english = new String[]{"qwerty", "qwertz"};
    private final String[] kbd_german = new String[]{"swiss", "qwertz", "qwerty"};
    private final String[] kbd_turkish = new String[]{"qwerty" /*,"turkish-q",*/ /*"turkish-f"*/};
    private final String[] kbd_french = new String[]{"qwerty", /*"azerty",*/ "swiss"};
    private final String[] kbd_spanish = new String[]{"qwerty"/*,"qwertz",*//*"spanish"*/};
    private final String[] kbd_dutch = new String[]{"qwerty"/*, "azerty"*/};
    private String[] keyboardList = kbd_english;

    private MaterialTextView tvCurrentKeyboard;

    public static String PREF_ANIMATION = "sn_pref_animation";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        NavController navController = Navigation.findNavController(view);

        SwitchCompat switchSound = view.findViewById(R.id.button_sound);

        MaterialTextView btnBackground = view.findViewById(R.id.button_background);
        btnBackground.setOnClickListener(v -> {
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToBackgroundFragment();
            navController.navigate(action);
        });

        MaterialTextView btnImprint = view.findViewById(R.id.button_imprint);
        MaterialTextView btnPrivacy = view.findViewById(R.id.button_privacy_policy);
        MaterialTextView btnTerms = view.findViewById(R.id.button_conditions);
        MaterialTextView btnLng = view.findViewById(R.id.button_language);

        btnImprint.setOnClickListener(v -> {
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToImprintFragment();
            navController.navigate(action);
        });

        btnPrivacy.setOnClickListener(v -> {
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToPrivacyFragment();
            navController.navigate(action);
        });

        btnTerms.setOnClickListener(v -> {
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToTermsFragment();
            navController.navigate(action);
        });

        MaterialTextView btnTheme = view.findViewById(R.id.button_themes);
        btnTheme.setOnClickListener(v -> {
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToThemeFragment();
            navController.navigate(action);
        });

        btnLng.setOnClickListener(v -> {
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToLanguageFragment();
            navController.navigate(action);
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Resources res = getResources();
        boolean sound = prefs.getBoolean(Settings.PREF_SOUND_ON,
                Settings.readKeypressSoundEnabled(prefs, res));

        switchSound.setChecked(sound);

        SharedPreferences.Editor editor = prefs.edit();
        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                editor.putBoolean(Settings.PREF_SOUND_ON, true).apply();
            } else {
                editor.putBoolean(Settings.PREF_SOUND_ON, false).apply();
            }
        });

        String currentKeyboard = prefs.getString(CURRENT_KEYBOARD,
                LanguageFragment.DEFAULT_KEYBOARD);

        LinearLayoutCompat layoutKeyboardType = view.findViewById(R.id.layout_key_type);
        tvCurrentKeyboard = view.findViewById(R.id.button_key_type_e);
        tvCurrentKeyboard.setText(currentKeyboard);

        layoutKeyboardType.setOnClickListener(v -> openKeyboardDialog());

        SwitchCompat switchAnimation = view.findViewById(R.id.button_effects);
        boolean animation = prefs.getBoolean(PREF_ANIMATION, true);
        switchAnimation.setChecked(animation);

        SwitchCompat vibrationSwitch = view.findViewById(R.id.vibration_id);
        boolean isEnableVibration = prefs.getBoolean(PREF_VIBRATE_ON, false);
        vibrationSwitch.setChecked(isEnableVibration);
        AudioManager audio = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

        vibrationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                audio.setVibrateSetting(AudioManager.FLAG_VIBRATE,
                        AudioManager.VIBRATE_SETTING_ON);
            } else {
                audio.setVibrateSetting(AudioManager.FLAG_VIBRATE,
                        AudioManager.VIBRATE_SETTING_OFF);
            }
        });

        switchAnimation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                prefs.edit()
                        .putBoolean(PREF_ANIMATION, true)
                        .putString(LXX_KEYBOARD_THEME_KEY, THEME_ID_LXX_DARK_Y)
                        .apply();
            } else {
                prefs.edit()
                        .putBoolean(PREF_ANIMATION, false)
                        .putString(LXX_KEYBOARD_THEME_KEY, THEME_ID_LXX_DARK_N)
                        .apply();
            }
        });
    }

    private void openKeyboardDialog() {
        String currentLanguage = prefs.getString(LanguageFragment.CURRENT_LANGUAGE,
                LanguageFragment.DEFAULT_LANGUAGE);

        String lngCode = currentLanguage.substring(0, 2);

        switch (lngCode) {
            case "nl":
                keyboardList = kbd_dutch;
                break;
            case "fr":

                //   Log.e("error=============",kbd_french.toString());
                keyboardList = kbd_french;
                break;
            case "de":
                keyboardList = kbd_german;
                break;
            //  case "es":
            case "es":
                keyboardList = kbd_spanish;
                break;
            case "tr":
                keyboardList = kbd_turkish;
                break;
            default:
                keyboardList = kbd_english;
                break;
        }

        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("Keyboard Type")
                .setItems(keyboardList, ((dialog1, which) -> {
                    prefs.edit().putString(CURRENT_KEYBOARD, keyboardList[which]).apply();
                    tvCurrentKeyboard.setText(keyboardList[which]);

                    updateLang(lngCode, keyboardList[which]);

                    dialog1.dismiss();
                }))
                .create();

        dialog.show();
    }

    private void updateLang(String lngCode, String type) {
        switch (lngCode) {
            case "nl":
                if (type.equalsIgnoreCase("qwerty")) {
                    prefs.edit().putString(CURRENT_LANGUAGE, "nl").apply();
                } else {
                    prefs.edit().putString(CURRENT_LANGUAGE, "nl_BE").apply();
                }
                break;
            case "fr":
                if (type.equalsIgnoreCase("azerty")) {
                    prefs.edit().putString(CURRENT_LANGUAGE, "fr").apply();
                } else if (type.equalsIgnoreCase("qwerty")) {
                    prefs.edit().putString(CURRENT_LANGUAGE, "fr_CA").apply();
                } else {
                    prefs.edit().putString(CURRENT_LANGUAGE, "fr_CH").apply();
                }
                break;
            case "de":
                if (type.equalsIgnoreCase("qwertz")) {
                    prefs.edit().putString(CURRENT_LANGUAGE, "de").apply();
                } else {
                    prefs.edit().putString(CURRENT_LANGUAGE, "de_CH").apply();
                }
                break;
            case "es":
//                if (type.equalsIgnoreCase("spanish")) {
                prefs.edit().putString(CURRENT_LANGUAGE, "es").apply();
//                }
                break;
            case "gl":
                if (type.equalsIgnoreCase("spanish")) {
                    prefs.edit().putString(CURRENT_LANGUAGE, "gl_ES").apply();
                }
                break;
            case "tr":
//                if (type.equalsIgnoreCase("qwerty")) {
                prefs.edit().putString(CURRENT_LANGUAGE, "tr").apply();
//                }
                break;
            default:
                prefs.edit().putString(CURRENT_LANGUAGE, "en_US").apply();
                break;
        }
    }
}
