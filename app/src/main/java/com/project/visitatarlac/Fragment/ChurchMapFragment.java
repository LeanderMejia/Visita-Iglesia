package com.project.visitatarlac.Fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.project.visitatarlac.Adapter.NearbyChurchAdapter;
import com.project.visitatarlac.Class.HelperClass;
import com.project.visitatarlac.Model.ChurchMapModel;
import com.project.visitatarlac.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChurchMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private static final int REQUEST_CODE_LOCATION = 0;
    HelperClass helperClass = new HelperClass();
    private ArrayList<ChurchMapModel> nearbyChurchModelArrayList = new ArrayList<>();
    private ArrayList<String> churchNameArrayList = new ArrayList<>();
    private BottomNavigationView bottomNav;
    private TabLayout tabLayout;
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView nearbyChurchRecView;
    private View map;
    private GoogleMap gMap;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationClient;
    private Location userLocation;
    private boolean isGPSEnabled = false;
    private SharedPreferences sharedPreferences;
    private LatLng tarlacLatLang;
    private Bundle bundle;
    private TextInputLayout searchLayout;
    private AutoCompleteTextView searchInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                bottomNav = getActivity().findViewById(R.id.bottom_nav);
                bottomNav.setSelectedItemId(R.id.home);
                bottomNav.setActivated(true);

                if (bundle != null && !bundle.getString("CHURCH_INFO_TITLE").isEmpty()) {
                    Fragment fragment = new ChurchInfoFragment();
                    fragment.setArguments(helperClass.createChurchInfoBundle(
                            bundle.getString("CHURCH_INFO_TITLE"),
                            bundle.getString("CHURCH_INFO_SNIPPET"),
                            bundle.getString("CHURCH_INFO_DESCRIPTION"),
                            bundle.getString("CHURCH_INFO_IMG"),
                            bundle.getStringArrayList("CHURCH_INFO_INFO")));
                    helperClass.setFragment(getActivity().getSupportFragmentManager(), R.id.frame_layout, fragment, null);
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).addToBackStack(null).commit();
                }
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_church_map, container, false);

        tabLayout = viewGroup.findViewById(R.id.tab_layout);
        map = viewGroup.findViewById(R.id.maps);
        swipeLayout = viewGroup.findViewById(R.id.swipe_layout);
        nearbyChurchRecView = viewGroup.findViewById(R.id.nearby_church_rec_view);
        searchLayout = viewGroup.findViewById(R.id.search_layout);
        searchInput = viewGroup.findViewById(R.id.search_input);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        sharedPreferences = getActivity().getSharedPreferences("FINISH_CHURCH", Context.MODE_PRIVATE);
        bundle = getArguments();

        fusedLocationClient.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(requireActivity(), location -> {
                    userLocation = location;
                    mapTabItem();
                });

        mapTabItem();
        tabListener();

        return viewGroup;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, churchNameArrayList);
        searchInput.setAdapter(searchAdapter);
        searchInput.setThreshold(1);
        performSearch();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        requireContext().registerReceiver(gpsStatusReceiver, intentFilter);
        mapTabItem();
    }

    @Override
    public void onPause() {
        super.onPause();
        requireContext().unregisterReceiver(gpsStatusReceiver);
        nearbyChurchModelArrayList.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        churchNameArrayList.clear();
    }

    private BroadcastReceiver gpsStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (LocationManager.PROVIDERS_CHANGED_ACTION.equals(intent.getAction())) {
                LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (isGPSEnabled || !isGPSEnabled) {
                    helperClass.setFragment(getActivity().getSupportFragmentManager(), R.id.frame_layout, new ChurchMapFragment(), null);
                }
            }
        }
    };

    private void tabListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    mapTabItem();
                } else if (tab.getPosition() == 1) {
                    churchNameArrayList.clear();
                    if (nearbyChurchModelArrayList.size() == 0) {
                        Toast.makeText(getActivity(), "There is no nearby church within your location.", Toast.LENGTH_SHORT).show();
                        bundle = null;
                        tarlacZoomCamera();
                        tabLayout.getTabAt(0).select();
                    } else {
                        nearbyChurchTabItem();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void mapTabItem() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(ChurchMapFragment.this::onMapReady);
        map.setVisibility(View.VISIBLE);
        searchLayout.setVisibility(View.VISIBLE);
        searchInput.setVisibility(View.VISIBLE);
        nearbyChurchRecView.setVisibility(View.GONE);
        swipeLayout.setVisibility(View.GONE);
        nearbyChurchModelArrayList.clear();
    }

    private void nearbyChurchTabItem() {
        map.setVisibility(View.GONE);
        searchLayout.setVisibility(View.GONE);
        searchInput.setVisibility(View.GONE);
        nearbyChurchRecView.setVisibility(View.VISIBLE);
        swipeLayout.setVisibility(View.VISIBLE);

        NearbyChurchAdapter adapter = new NearbyChurchAdapter();
        adapter.setNearbyChurchModelArrayList(nearbyChurchModelArrayList);
        nearbyChurchRecView.setAdapter(adapter);
        nearbyChurchRecView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeLayout.setOnRefreshListener(() -> {
            adapter.notifyDataSetChanged();
            swipeLayout.setRefreshing(false);
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setCompassEnabled(true);

        if (bundle != null) {
            // Set the camera view to marker
            if (bundle.getDouble("NEARBY_LATITUDE") != 0.0) {
                LatLng nearbyLatLang = new LatLng(bundle.getDouble("NEARBY_LATITUDE"), bundle.getDouble("NEARBY_LONGITUDE"));
                zoomMarker(nearbyLatLang, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN), bundle.getString("NEARBY_TITLE"), bundle.getString("NEARBY_SNIPPET"));
            } else {
                LatLng churchInfoLatLang = new LatLng(bundle.getDouble("CHURCH_INFO_LATITUDE"), bundle.getDouble("CHURCH_INFO_LONGITUDE"));
                zoomMarker(churchInfoLatLang, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE), bundle.getString("CHURCH_INFO_TITLE"), bundle.getString("CHURCH_INFO_SNIPPET"));
            }
        } else {
            tarlacZoomCamera();
        }


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
            gMap.setOnMyLocationButtonClickListener(this);
            displayMarker(gMap);
        } else {
            displayMarker(gMap);
            permissionListener();
        }
    }

    private void permissionListener() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                helperClass.setFragment(getActivity().getSupportFragmentManager(), R.id.frame_layout, new ChurchMapFragment(), null);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] -> [Permission]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isGPSEnabled) {
            Toast.makeText(getActivity(), "Please turn on your location.", Toast.LENGTH_SHORT).show();
            tarlacZoomCamera();
            gMap.animateCamera(setCameraLocation(tarlacLatLang, 10.0f));
            displayMarker(gMap);
        } else {
            fusedLocationClient.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(requireActivity(), location -> {
                        if (location != null) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            gMap.animateCamera(setCameraLocation(latLng, 15.0f));
                            userLocation = location;
                            nearbyChurchModelArrayList.clear();
                            displayMarker(gMap);
                        } else {
                            nearbyChurchModelArrayList.clear();
                            displayMarker(gMap);
                        }
                    });
        }
        return true;
    }

    private void tarlacZoomCamera() {
        tarlacLatLang = new LatLng(15.4755, 120.5963);
        gMap.moveCamera(setCameraLocation(tarlacLatLang, 10.0f));
    }

    private CameraUpdate setCameraLocation(LatLng latLng, float zoom) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        return cameraUpdate;
    }

    private void zoomMarker(LatLng latLng, BitmapDescriptor bitmapDescriptor, String title, String snippet) {
        tarlacZoomCamera();
        gMap.animateCamera(setCameraLocation(latLng, 15.0f), new GoogleMap.CancelableCallback() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onFinish() {
                infoWindowMarker(latLng, bitmapDescriptor, title, snippet);
            }
        });
    }

    private void displayMarker(GoogleMap gMap) {
        try {
            int jsonArraySize = helperClass.getJSONArray(getActivity(), "markers").length();

            for (int i = 0; i < jsonArraySize; i++) {
                JSONObject jsonObject = helperClass.getJSONArray(getActivity(), "markers").getJSONObject(i);
                String churchName = jsonObject.getString("churchName");
                String churchAddress = jsonObject.getString("churchAddress");
                String churchImg = jsonObject.getString("churchImg");
                double latitude = jsonObject.getDouble("latitude");
                double longitude = jsonObject.getDouble("longitude");
                LatLng churchLatLng = new LatLng(latitude, longitude);

                if (!churchNameArrayList.contains(churchName + " " + churchAddress))
                    churchNameArrayList.add(churchName + " " + churchAddress);

                if (isGPSEnabled && userLocation != null) {
                    displayNearbyMarker(churchName, churchAddress, churchImg, latitude, longitude, churchLatLng);
                } else {
                    if (churchName.equals("San Sebastian Cathedral Parish")) {
                        defaultMarker(churchLatLng, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET), churchName, churchAddress);
                    } else {
                        defaultMarker(churchLatLng, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE), churchName, churchAddress);
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayNearbyMarker(String churchName, String churchAddress, String churchImg, double latitude, double longitude, LatLng churchLatLng) {
        Location churchLocation = new Location("");
        churchLocation.setLatitude(latitude);
        churchLocation.setLongitude(longitude);

        float distanceInMeters = userLocation.distanceTo(churchLocation);
        float distanceInKm = (distanceInMeters / 1000);
        float km = Float.parseFloat(String.format("%.2f", distanceInKm));

        if (distanceInKm < 6) {
            defaultMarker(churchLatLng, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN), churchName, churchAddress);

            boolean isDuplicate = false;
            for (ChurchMapModel item : nearbyChurchModelArrayList) {
                if (item.getChurchName().equals(churchName)) {
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate)
                nearbyChurchModelArrayList.add(new ChurchMapModel(churchName, churchAddress, churchImg, km, latitude, longitude));
        } else {
            defaultMarker(churchLatLng, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE), churchName, churchAddress);
        }

        if (km < 0.201)
            storedData(churchName, churchAddress, churchImg, km, latitude, longitude); // 201 meter
        // if (km < 0.5) storedData(churchName, churchAddress, churchImg, km, latitude, longitude); // 500 meter
        // if (String.valueOf(km).equals("0.0")) storedData(churchName, churchAddress, churchImg, km, latitude, longitude);
    }

    private void storedData(String churchName, String churchAddress, String churchImg, float km, double latitude, double longitude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = sharedPreferences.getString("storedData", null);
        JSONArray jsonArray = null;
        if (json != null) {
            try {
                jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String existingChurchName = jsonObject.getString("churchName");
                    if (existingChurchName.equals(churchName)) return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            jsonArray = new JSONArray();
        }
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy 'at' h:mma", Locale.ENGLISH);
        String dateAndTime = dateFormat.format(date);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("churchName", churchName);
            jsonObject.put("churchAddress", churchAddress);
            jsonObject.put("churchImg", churchImg);
            jsonObject.put("km", km);
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
            jsonObject.put("dateAndTime", dateAndTime);
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editor.putString("storedData", jsonArray.toString());
        editor.apply();
    }

    private void defaultMarker(LatLng latLng, BitmapDescriptor bitmapDescriptor, String title, String snippet) {
        gMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(bitmapDescriptor)
                .title(title).snippet(snippet));

    }

    private void infoWindowMarker(LatLng latLng, BitmapDescriptor bitmapDescriptor, String title, String snippet) {
        gMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(bitmapDescriptor)
                .title(title).snippet(snippet))
                .showInfoWindow();
    }

    private void performSearch() {
        searchInput.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                getSearchResult();
                return true;
            }
            return false;
        });
    }

    private void getSearchResult() {
        int index = churchNameArrayList.indexOf(searchInput.getText().toString());

        if (index < 0) {
            searchInput.clearFocus();
            searchInput.getText().clear();
            return;
        }

        View view = getActivity().getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        try {
            JSONObject jsonObject = helperClass.getJSONArray(getActivity(), "markers").getJSONObject(index);
            String churchName = jsonObject.getString("churchName");
            String churchAddress = jsonObject.getString("churchAddress");
            double latitude = jsonObject.getDouble("latitude");
            double longitude = jsonObject.getDouble("longitude");

            LatLng searchMarkerLatLang = new LatLng(latitude, longitude);
            zoomMarker(searchMarkerLatLang, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE), churchName, churchAddress);
            searchInput.clearFocus();
            searchInput.getText().clear();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

