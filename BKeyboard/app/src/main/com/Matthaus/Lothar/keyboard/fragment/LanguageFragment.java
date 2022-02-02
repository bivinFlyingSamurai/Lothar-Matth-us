package com.Matthaus.Lothar.keyboard.fragment;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Matthaus.Lothar.keyboard.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by Akshay Raj on 16/10/20 at 8:49 pm.
 * support@snowcorp.org
 * www.snowcorp.org
 */

public class LanguageFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_SECURESETTING = 1;
    String[] languages = new String[] {
            "English",
//            "English (US)",
//            "English (India)",
//            "English (UK)",
//            "Dutch",
//            "Dutch (Belgium)",
            "French",
//            "French (Canada)",
//            "French (Switzerland)",
//            "Galician",
//            "Galician (Spain)",
            "German",
//            "German (Switzerland)",
            "Spanish",
//            "Spanish (Latin America)",
//            "Spanish (US)",

            "Turkish",
            "Portuguese"
    };

    String[] languages_code = new String[] {
            "en_US",
//            "en_IN",
//            "en_GB",
//            "nl",
//            "nl_BE",
            "fr",
//            "fr_CA",
//            "fr_CH",
//            "gl_ES",
            "de",
//            "de_CH",
            "es",
//            "es_419",
//            "es_US",
            "tr",
            "pt_BR"
    };

    String[] keyboard = new String[]{
            "qwerty",   // en_US
//            "qwerty",   // en_IN
//            "qwerty",   // en_GB
//            "qwerty",   // nl
//            "azerty",   // nl_BE
            "qwerty",   // fr
//            "qwerty",   // fr_CA
//            "swiss",    // fr_CH
//            "spanish",  // gl_ES
            "qwertz",   // de
//            "swiss",    // de_CH
            "qwerty",  // es
//            "spanish",  // es_419
//            "spanish",  // es_US
            "qwerty",
            "qwerty"
    }; // tr

    InputMethodManager mImm;
    private InputMethodInfo mImi;
//    private ArrayList<CharSequence> listSelLanguages;
//    private ArrayList<Language> listLanguages;
    Context context;
    LanguageAdapter adapter;
    private SharedPreferences prefs;

    public static final String CURRENT_LANGUAGE = "sn_current_language";
    public static final String DEFAULT_LANGUAGE = "en_US";//"en_US";
    public static final String CURRENT_KEYBOARD = "sn_current_keyboard";
    public static final String DEFAULT_KEYBOARD = "qwertz";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_language, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        mImm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mImi = getMyImi(context, mImm);
//        mRichImm = RichInputMethodManager.getInstance();

//        listLanguages = new ArrayList<>();
//        listSelLanguages = new ArrayList<>();
        adapter = new LanguageAdapter();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_SECURE_SETTINGS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_SECURE_SETTINGS},
                        MY_PERMISSIONS_REQUEST_SECURESETTING);
//                Toast.makeText(context,"permission not avialbe need to give",Toast.LENGTH_LONG).show();

                // MY_PERMISSIONS_REQUEST_SECURESETTING is an
                // app-defined int constant

                return;
            }else {
//                Toast.makeText(context,"permission already granted",Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SECURESETTING: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    // calendar task you need to do.
                    prefs.edit().putString(CURRENT_KEYBOARD, keyboard[4]).apply();
//                    Toast.makeText(context,"permission accet",Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(context,"permission denied",Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }
    private void init() {
        if (mImi == null || mImi.getSubtypeCount() <= 1) {
            return;
        }

//        List<InputMethodSubtype> enabledSubtypes =
//                mImm.getEnabledInputMethodSubtypeList(mImi, true);
//        for (int j = 0; j < enabledSubtypes.size(); j++) {
//            InputMethodSubtype enabledSubtype = enabledSubtypes.get(j);
//            CharSequence enabledName = enabledSubtype.getDisplayName(context, mImi.getPackageName(),
//                    mImi.getServiceInfo().applicationInfo);
//            Log.e("__snow", "Sel Lng: "+enabledSubtype.getLocale());
//            listSelLanguages.add(enabledName);
//        }

        for (int i = 0; i < mImi.getSubtypeCount(); i ++) {
            InputMethodSubtype subtype = mImi.getSubtypeAt(i);
            CharSequence name = subtype.getDisplayName(context, mImi.getPackageName(),
                    mImi.getServiceInfo().applicationInfo);

//            listLanguages.add(new Language(subtype, name));
//            if(listSelLanguages.contains(name)) {
//                listLanguages.add(new Language(subtype, name, true));
//            } else {
//                listLanguages.add(new Language(subtype, name, false));
//            }
        }
    }

    private InputMethodInfo getMyImi(Context context, InputMethodManager imm) {
        final List<InputMethodInfo> imis = imm.getInputMethodList();
        for (int i = 0; i < imis.size(); ++i) {
            final InputMethodInfo imi = imis.get(i);
            if (imis.get(i).getPackageName().equals(context.getPackageName())) {
                return imi;
            }
        }
        return null;
    }

    class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.MyHolder> {
        class MyHolder extends RecyclerView.ViewHolder {
            MaterialTextView tvTitle;
            ShapeableImageView sivChecked;
            public MyHolder(@NonNull @Nonnull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.language_title);
                sivChecked = itemView.findViewById(R.id.siv_checked);
            }
        }

        @NonNull
        @Override
        public LanguageAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyHolder(LayoutInflater.from(context)
                    .inflate(R.layout.list_item_language, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull LanguageAdapter.MyHolder holder, int position) {
            String item = languages[position];

            holder.tvTitle.setText(item);
            String currentLocale = prefs.getString(LanguageFragment.CURRENT_LANGUAGE,
                    LanguageFragment.DEFAULT_LANGUAGE);

            String currentLanguage = currentLocale.substring(0, 2);
            String languageCode = languages_code[position].substring(0, 2);

//            if (currentLocale.equals(languages_code[position])) {
            if (currentLanguage.equals(languageCode)) {
                holder.sivChecked.setVisibility(View.VISIBLE);
            } else {
                holder.sivChecked.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(v-> {
                prefs.edit().putString(CURRENT_LANGUAGE, languages_code[position]).apply();
                prefs.edit().putString(CURRENT_KEYBOARD, keyboard[position]).apply();
                String _currentLocale = prefs.getString(CURRENT_LANGUAGE,
                        DEFAULT_LANGUAGE);
                String currentKeyboard = prefs.getString(CURRENT_KEYBOARD,
                        CURRENT_KEYBOARD);

                if (_currentLocale.equals(languages_code[position])) {
                    holder.sivChecked.setVisibility(View.VISIBLE);
                } else {
                    holder.sivChecked.setVisibility(View.GONE);
                }

                notifyDataSetChanged();
            });
        }

        @Override
        public int getItemCount() {
            return languages.length;
        }
    }
}
