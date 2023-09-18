package com.project.visitatarlac.Activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.project.visitatarlac.Class.HelperClass;
import com.project.visitatarlac.Fragment.AboutFragment;
import com.project.visitatarlac.Fragment.ChurchMapFragment;
import com.project.visitatarlac.Fragment.ChurchesFragment;
import com.project.visitatarlac.Fragment.HomeFragment;
import com.project.visitatarlac.Fragment.OurTeamFragment;
import com.project.visitatarlac.Fragment.PrayersFragment;
import com.project.visitatarlac.Fragment.ProgressFragment;
import com.project.visitatarlac.Fragment.ReferenceFragment;
import com.project.visitatarlac.Fragment.TermsAndConditionFragment;
import com.project.visitatarlac.R;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private NavigationView sideNav;
    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;
    private HelperClass helperClass = new HelperClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_nav);
        sideNav = findViewById(R.id.side_nav);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_navbar, R.string.close_navbar);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        bottomNavListener();
        sideNavListener();
    }

    private void bottomNavListener() {
        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    helperClass.setFragment(getSupportFragmentManager(), R.id.frame_layout, new HomeFragment(), null);
                    break;
                case R.id.church_map:
                    helperClass.setFragment(getSupportFragmentManager(), R.id.frame_layout, new ChurchMapFragment(), null);
                    break;
            }
            return true;
        });
    }

    private void sideNavListener() {
        sideNav.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.churches:
                    setHomeSelectedNav();
                    helperClass.setFragment(getSupportFragmentManager(), R.id.frame_layout, new ChurchesFragment(), null);
                    break;
                case R.id.prayers:
                    setHomeSelectedNav();
                    helperClass.setFragment(getSupportFragmentManager(), R.id.frame_layout, new PrayersFragment(), null);
                    break;
                case R.id.church_map:
                    setChurchMapSelectedNav();
                    helperClass.setFragment(getSupportFragmentManager(), R.id.frame_layout, new ChurchMapFragment(), null);
                    break;
                case R.id.progress:
                    setHomeSelectedNav();
                    SharedPreferences sharedPreferences = getSharedPreferences("FINISH_CHURCH", Context.MODE_PRIVATE);
                    if (sharedPreferences.getString("storedData", null) == null) {
                        Toast.makeText(this, "There are no churches you have reached.", Toast.LENGTH_SHORT).show();
                    } else {
                        helperClass.setFragment(getSupportFragmentManager(), R.id.frame_layout, new ProgressFragment(), null);
                    }
                    break;
                case R.id.about_visita_iglesia:
                    setHomeSelectedNav();
                    helperClass.setFragment(getSupportFragmentManager(), R.id.frame_layout, new AboutFragment(), null);
                    break;
                case R.id.our_team:
                    setHomeSelectedNav();
                    helperClass.setFragment(getSupportFragmentManager(), R.id.frame_layout, new OurTeamFragment(), null);
                    break;
                case R.id.reference:
                    setHomeSelectedNav();
                    helperClass.setFragment(getSupportFragmentManager(), R.id.frame_layout, new ReferenceFragment(), null);
                    break;
                case R.id.terms_and_conditions:
                    setHomeSelectedNav();
                    helperClass.setFragment(getSupportFragmentManager(), R.id.frame_layout, new TermsAndConditionFragment(), null);
                    break;
            }
            drawerLayout.close();
            return true;
        });
    }

    private void setHomeSelectedNav() {
        bottomNav.setSelectedItemId(R.id.home);
        bottomNav.setActivated(true);
    }

    private void setChurchMapSelectedNav() {
        bottomNav.setSelectedItemId(R.id.church_map);
        bottomNav.setActivated(true);
    }
}