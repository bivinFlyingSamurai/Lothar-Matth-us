package com.Matthaus.Lothar.keyboard.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Matthaus.Lothar.keyboard.R;
import com.Matthaus.Lothar.keyboard.models.BackgroundImageModel;
import com.android.inputmethod.latin.settings.Settings;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akshay Raj on 28/09/20 at 6:39 pm.
 * support@snowcorp.org
 * www.snowcorp.org
 */

public class BackgroundFragment extends Fragment {
    private Context mContext;
    private List<BackgroundImageModel> list;
    private SharedPreferences pref;

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
        return inflater.inflate(R.layout.fragment_background, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        pref = PreferenceManager.getDefaultSharedPreferences(mContext);

        list = new ArrayList<>();
        MyAdapter myAdapter = new MyAdapter();

        list.add(new BackgroundImageModel(R.drawable.bgi5, ""));
        list.add(new BackgroundImageModel(R.drawable.bgi1, ""));
        list.add(new BackgroundImageModel(R.drawable.bgi2, ""));
        list.add(new BackgroundImageModel(R.drawable.bgi3, ""));
        list.add(new BackgroundImageModel(R.drawable.bgi4, ""));
        list.add(new BackgroundImageModel(R.drawable.bgi6, ""));
        list.add(new BackgroundImageModel(R.drawable.bgi7, ""));
        list.add(new BackgroundImageModel(R.drawable.bgi8, ""));
        list.add(new BackgroundImageModel(R.drawable.bgi9, ""));
        list.add(new BackgroundImageModel(R.drawable.bgi10, ""));
        list.add(new BackgroundImageModel(R.drawable.bgi11, "Solid Black"));

       /* list.add(R.drawable.bg_key_1);
        list.add(R.drawable.bg_key_2);
        list.add(R.drawable.bg_key_3);
        list.add(R.drawable.bg_key_4);
        list.add(R.drawable.bg_key_5);*/

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(myAdapter);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.list_item_background, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyHolder holder, int position) {
            int currentBG = pref.getInt(Settings.PREF_CURRENT_BACKGROUND, R.drawable.bg_key_1);
            holder.siv.setImageResource(list.get(position).getImage());
            holder.backgroundTitle.setText(list.get(position).getTitle());



            holder.itemView.setOnClickListener(v -> {
                pref.edit().putInt(Settings.PREF_CURRENT_BACKGROUND_POSITION, position).apply();
                pref.edit().putInt(
                        Settings.PREF_CURRENT_BACKGROUND, list.get(position).getImage()).apply();
                Toast.makeText(mContext,
                        "Background successfully updated!",
                        Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            });

            if (list.get(position).getImage() == currentBG) {
                holder.ivChecked.setVisibility(View.VISIBLE);
            } else {
                holder.ivChecked.setVisibility(View.GONE);
            }

            int checkPosition = pref.getInt(Settings.PREF_CURRENT_BACKGROUND_POSITION, 0);
            if (position == checkPosition) {
                holder.ivChecked.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {
            ShapeableImageView siv;
            ImageView ivChecked;
            TextView backgroundTitle;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                siv = itemView.findViewById(R.id.image_view);
                backgroundTitle = itemView.findViewById(R.id.tv_background_title);
                ivChecked = itemView.findViewById(R.id.ivChecked);
            }
        }
    }
}
