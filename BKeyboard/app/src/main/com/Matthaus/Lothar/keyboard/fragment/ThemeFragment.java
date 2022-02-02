package com.Matthaus.Lothar.keyboard.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.Matthaus.Lothar.keyboard.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

/**
 * Created by Akshay Raj on 23/10/20 at 12:03 pm.
 * support@snowcorp.org
 * www.snowcorp.org
 */

public class ThemeFragment extends Fragment {
    Context mContext;
    SharedPreferences pref;
    ShapeableImageView checkedTheme1, checkedTheme2;

    static final String LXX_KEYBOARD_THEME_KEY = "pref_keyboard_theme_20140509";
    public static final String THEME_ID_LXX_LIGHT = "3";
    public static final String THEME_ID_LXX_DARK_Y = "4";
    public static final String THEME_ID_LXX_DARK_N = "5";
    public static final String DEFAULT_THEME_ID = THEME_ID_LXX_DARK_N;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_theme, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pref = PreferenceManager.getDefaultSharedPreferences(mContext);

        checkedTheme1 = view.findViewById(R.id.checkedTheme1);
        checkedTheme2 = view.findViewById(R.id.checkedTheme2);

        updateUI();

        MaterialCardView btnTheme1 = view.findViewById(R.id.theme1);
        btnTheme1.setOnClickListener(v -> {
            pref.edit().putString(LXX_KEYBOARD_THEME_KEY, THEME_ID_LXX_DARK_N).apply();

//            Toast.makeText(mContext, "Theme successfully changed!", Toast.LENGTH_SHORT).show();
            updateUI();
        });

        MaterialCardView btnTheme2 = view.findViewById(R.id.theme2);
        btnTheme2.setOnClickListener(v -> {
            boolean animation = pref.getBoolean(HomeFragment.PREF_ANIMATION, true);
            pref.edit().putString(LXX_KEYBOARD_THEME_KEY, THEME_ID_LXX_LIGHT).apply();

//            if (animation) {
//                pref.edit().putString(LXX_KEYBOARD_THEME_KEY, THEME_ID_LXX_LIGHT).apply();
//
//            } else {
//
//                pref.edit().putString(LXX_KEYBOARD_THEME_KEY, THEME_ID_LXX_DARK_N).apply();
//            }
//            Toast.makeText(mContext, "Theme successfully changed!", Toast.LENGTH_SHORT).show();
            updateUI();
        });
    }

    private void updateUI() {
        String currentTheme = pref.getString(LXX_KEYBOARD_THEME_KEY, DEFAULT_THEME_ID);
        if (currentTheme.equalsIgnoreCase(THEME_ID_LXX_LIGHT)) {
            checkedTheme1.setVisibility(View.GONE);
            checkedTheme2.setVisibility(View.VISIBLE);
        } else {
            checkedTheme1.setVisibility(View.VISIBLE);
            checkedTheme2.setVisibility(View.GONE);
        }
    }
}
