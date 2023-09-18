package com.project.visitatarlac.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.visitatarlac.Adapter.ListItemAdapter;
import com.project.visitatarlac.Class.HelperClass;
import com.project.visitatarlac.Model.ChurchInfoModel;
import com.project.visitatarlac.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChurchInfoFragment extends Fragment {

    HelperClass helperClass = new HelperClass();
    private BottomNavigationView bottomNav;
    private TextView churchName, churchDescription;
    private ImageView churchImg;
    private RecyclerView churchInfoRecView;
    private FloatingActionButton locationFab;
    Bundle bundle;

    public ChurchInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().getSupportFragmentManager().popBackStack("CHURCH_LIST_TAG", getActivity().getFragmentManager().POP_BACK_STACK_INCLUSIVE);
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_church_info, container, false);

        churchImg = viewGroup.findViewById(R.id.img);
        churchName = viewGroup.findViewById(R.id.name);
        churchDescription = viewGroup.findViewById(R.id.church_description);
        churchInfoRecView = viewGroup.findViewById(R.id.church_info_rec_view);
        locationFab = viewGroup.findViewById(R.id.location_fab);

        bundle = getArguments();

        locationFab.setOnClickListener(view -> {
            openChurchLocation();
        });

        return viewGroup;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String drawableStr = bundle.getString("CHURCH_IMG");
        int id = getResources().getIdentifier(drawableStr, "drawable", getContext().getPackageName());
        Drawable drawable = getContext().getResources().getDrawable(id);

        churchName.setText(bundle.getString("CHURCH_NAME"));
        churchDescription.setText(bundle.getString("CHURCH_DESCRIPTION"));
        Glide.with(getContext()).load(drawable).into(churchImg);

        ListItemAdapter churchInfoAdapter = new ListItemAdapter();
        churchInfoAdapter.setArrayList(bundle.getStringArrayList("CHURCH_INFO"));
        churchInfoRecView.setAdapter(churchInfoAdapter);
        churchInfoRecView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void openChurchLocation() {
        bottomNav = getActivity().findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.church_map);
        bottomNav.setActivated(true);
        try {
            int jsonArraySize = helperClass.getJSONArray(getActivity(), "markers").length();
            for (int i = 0; i < jsonArraySize; i++) {
                JSONObject jsonObject = helperClass.getJSONArray(getActivity(), "markers").getJSONObject(i);
                String churchNameMarker = jsonObject.getString("churchName");
                String churchAddressMarker = jsonObject.getString("churchAddress");

                if (churchName.getText().toString().equalsIgnoreCase(churchNameMarker) && bundle.getString("CHURCH_ADDRESS").equalsIgnoreCase(churchAddressMarker)) {
                    Fragment fragment = new ChurchMapFragment();
                    fragment.setArguments(helperClass.createChurchMarkerBundle(
                            "CHURCH_INFO_LATITUDE", jsonObject.getDouble("latitude"),
                            "CHURCH_INFO_LONGITUDE", jsonObject.getDouble("longitude"),
                            "CHURCH_INFO_TITLE", jsonObject.getString("churchName"),
                            "CHURCH_INFO_SNIPPET", jsonObject.getString("churchAddress"),
                            "CHURCH_INFO_DESCRIPTION", bundle.getString("CHURCH_DESCRIPTION"),
                            "CHURCH_INFO_IMG", bundle.getString("CHURCH_IMG"),
                            "CHURCH_INFO_INFO", bundle.getStringArrayList("CHURCH_INFO")));
                    helperClass.setFragment(getActivity().getSupportFragmentManager(), R.id.frame_layout, fragment, null);
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}