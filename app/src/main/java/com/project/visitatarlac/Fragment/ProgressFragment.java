package com.project.visitatarlac.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.project.visitatarlac.Adapter.DetailsProgressAdapter;
import com.project.visitatarlac.Class.HelperClass;
import com.project.visitatarlac.Model.ChurchMapModel;
import com.project.visitatarlac.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProgressFragment extends Fragment {

    HelperClass helperClass = new HelperClass();
    private ArrayList<ChurchMapModel> finishChurchArrayList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private GoogleMap gMap;
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView finishChurchRecView;
    private Button clearBtn;
    private LatLng tarlacLatLang;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_progress, container, false);
        swipeLayout = viewGroup.findViewById(R.id.swipe_layout);
        finishChurchRecView = viewGroup.findViewById(R.id.finish_church_rec_view);
        clearBtn = viewGroup.findViewById(R.id.clear_btn);

        sharedPreferences = getActivity().getSharedPreferences("FINISH_CHURCH", Context.MODE_PRIVATE);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(ProgressFragment.this::onMapReady);

        clearBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setTitle("Delete Church")
                    .setMessage("Are you sure you want to delete all reached church? This action cannot be undone.").
                    setNegativeButton("CANCEL", null)
                    .setPositiveButton("DELETE", (dialog, which) -> {
                        clearHistory();
                        helperClass.setFragment(getActivity().getSupportFragmentManager(), R.id.frame_layout, new HomeFragment(), null);
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        return viewGroup;
    }

    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setCompassEnabled(true);
        gMap.getUiSettings().setMapToolbarEnabled(false);

        displayMarker();
        displayDetails();
    }

    private void displayMarker() {
        String json = sharedPreferences.getString("storedData", null);
        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    finishChurchArrayList.add(new ChurchMapModel(
                            jsonObject.getString("churchName"),
                            jsonObject.getString("churchAddress"),
                            jsonObject.getString("churchImg"),
                            jsonObject.getString("dateAndTime"),
                            Float.parseFloat(jsonObject.getString("km")),
                            Float.parseFloat(jsonObject.getString("latitude")),
                            Float.parseFloat(jsonObject.getString("longitude"))
                    ));
                    LatLng churchLatLng = new LatLng(finishChurchArrayList.get(i).getLatitude(), finishChurchArrayList.get(i).getLongitude());
                    gMap.moveCamera(setCameraLocation(churchLatLng, 12.0f));
                    addMarker(churchLatLng, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE), finishChurchArrayList.get(i).getChurchName(), finishChurchArrayList.get(i).getChurchAddress());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayDetails() {
        DetailsProgressAdapter adapter = new DetailsProgressAdapter();
        adapter.setFinishChurchArrayList(finishChurchArrayList);
        finishChurchRecView.setAdapter(adapter);
        finishChurchRecView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeLayout.setOnRefreshListener(() -> {
            //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ProgressFragment()).commit();
            adapter.notifyDataSetChanged();
            swipeLayout.setRefreshing(false);
        });
    }

    private CameraUpdate setCameraLocation(LatLng latLng, float zoom) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        return cameraUpdate;
    }

    private void addMarker(LatLng latLng, BitmapDescriptor bitmapDescriptor, String title, String snippet) {
        gMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(bitmapDescriptor)
                .title(title).snippet(snippet));
    }

    private void clearHistory() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("FINISH_CHURCH", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("INITIAL_DISTANCE_KM", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
        editor2.clear();
        editor2.apply();
    }
}