package com.Matthaus.Lothar.keyboard.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.Matthaus.Lothar.keyboard.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by Akshay Raj on 18/10/20 at 12:37 pm.
 * support@snowcorp.org
 * www.snowcorp.org
 */

public class MainFragment extends Fragment {
    private Context mContext;
    private LinearLayout btnEnable;
    private LinearLayout btnChoose;
    private NavController navController;

    private String imId = "com.Matthaus.Lothar.keyboard/com.android.inputmethod.latin.LatinIME";

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
        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    @Override
    public void onViewCreated(@NonNull @Nonnull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);


        InputMethodManager mImm = ((InputMethodManager)
                mContext.getSystemService(Context.INPUT_METHOD_SERVICE));

        btnEnable = view.findViewById(R.id.button_activate);
        btnChoose = view.findViewById(R.id.button_choose);
        LinearLayout btnSetting = view.findViewById(R.id.button_settings);

        btnEnable.setOnClickListener(v -> {

            startActivityForResult(
                    new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS),
                    0);
        });


        btnChoose.setOnClickListener(v -> {
            if (isInputEnabled()) {
                mImm.showInputMethodPicker();
            } else {
                Toast.makeText(mContext, "Please enable keyboard first.", Toast.LENGTH_SHORT).show();
            }
        });

        String id = Settings.Secure.getString(
                mContext.getContentResolver(),
                Settings.Secure.DEFAULT_INPUT_METHOD
        );

        if (id.equalsIgnoreCase(imId)) {
            openSetting();
        }

        btnSetting.setOnClickListener(v -> openSetting());
    }

    private void openSetting() {
        NavDirections action = MainFragmentDirections.actionMainFragmentToHomeFragment();
        navController.navigate(action);
    }

    public boolean isInputEnabled() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> mInputMethodProperties = imm.getEnabledInputMethodList();

        final int N = mInputMethodProperties.size();
        boolean isInputEnabled = false;

        for (int i = 0; i < N; i++) {
            InputMethodInfo imi = mInputMethodProperties.get(i);
            Log.d("INPUT ID", String.valueOf(imi.getId()));
            if (imi.getId().contains(mContext.getPackageName())) {
                isInputEnabled = true;
            }
        }
        return isInputEnabled;
    }
}
