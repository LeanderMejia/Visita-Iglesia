package com.project.visitatarlac.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.visitatarlac.Adapter.PrayerListAdapter;
import com.project.visitatarlac.Class.HelperClass;
import com.project.visitatarlac.Model.PrayersModel;
import com.project.visitatarlac.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PrayersInfoFragment extends Fragment {

    private ArrayList<PrayersModel> prayersModelArrayList = new ArrayList<>();
    private TextView prayerName, prayerDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_prayers_info, container, false);

        prayerName = viewGroup.findViewById(R.id.prayer_name);
        prayerDescription = viewGroup.findViewById(R.id.prayer_description);

        Bundle bundle = getArguments();
        prayerName.setText(bundle.getString("PRAYER_NAME"));
        prayerDescription.setText(bundle.getString("PRAYER_DESCRIPTION"));

        return viewGroup;
    }
}