package com.project.visitatarlac.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.visitatarlac.Class.HelperClass;
import com.project.visitatarlac.R;

public class HomeFragment extends Fragment {

    private long backPressedValue;
    private final int TIME_INTERVAL = 2000;
    private CardView churchCard, prayersCard, churchMapCard, progressCard;
    private BottomNavigationView bottomNav;
    private HelperClass helperClass = new HelperClass();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (backPressedValue + TIME_INTERVAL > System.currentTimeMillis()) {
                    System.exit(0);
                    return;
                }
                Toast.makeText(getActivity(), "Press again to exit", Toast.LENGTH_SHORT).show();
                backPressedValue = System.currentTimeMillis();
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        churchCard = viewGroup.findViewById(R.id.church_card);
        prayersCard = viewGroup.findViewById(R.id.prayers_card);
        churchMapCard = viewGroup.findViewById(R.id.church_map_card);
        progressCard = viewGroup.findViewById(R.id.progress_card);

        churchCard.setOnClickListener(view -> {
            helperClass.setFragment(getActivity().getSupportFragmentManager(), R.id.frame_layout, new ChurchesFragment(), null);
        });

        prayersCard.setOnClickListener(view -> {
            helperClass.setFragment(getActivity().getSupportFragmentManager(), R.id.frame_layout, new PrayersFragment(), null);
        });

        churchMapCard.setOnClickListener(view -> {
            helperClass.setFragment(getActivity().getSupportFragmentManager(), R.id.frame_layout, new ChurchMapFragment(), null);
            bottomNav = getActivity().findViewById(R.id.bottom_nav);
            bottomNav.setSelectedItemId(R.id.church_map);
            bottomNav.setActivated(true);
        });

        progressCard.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("FINISH_CHURCH", Context.MODE_PRIVATE);
            if (sharedPreferences.getString("storedData", null) == null) {
                Toast.makeText(getActivity(), "There are no churches you have reached.", Toast.LENGTH_SHORT).show();
            } else {
                helperClass.setFragment(getActivity().getSupportFragmentManager(), R.id.frame_layout, new ProgressFragment(), null);
            }
        });

        return viewGroup;
    }
}