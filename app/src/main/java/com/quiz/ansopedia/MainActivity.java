package com.quiz.ansopedia;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.quiz.ansopedia.Utility.Utility;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    ImageSlider image_slider;
    TabLayout tabLayout;
    RecyclerView rvContent;
    boolean isDoubleBackPressed = false;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navHome) {
                    Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.navNotification) {
                    Toast.makeText(MainActivity.this, "Notification", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.navList) {
                    Toast.makeText(MainActivity.this, "List", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.navBookmark) {
                    Toast.makeText(MainActivity.this, "LeaderBoard", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

//        ########################### drawer Implementation Start #############################################
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.navhome) {
                    Toast.makeText(getApplicationContext(), "Home Clicked", Toast.LENGTH_SHORT).show();

                } else if (id == R.id.navprofile) {
                    Toast.makeText(getApplicationContext(), "Profile Clicked", Toast.LENGTH_SHORT).show();

                } else if (id == R.id.navabout) {
                    Toast.makeText(getApplicationContext(), "About Clicked", Toast.LENGTH_SHORT).show();

                } else if (id == R.id.navcourse) {
                    Toast.makeText(getApplicationContext(), "Course Clicked", Toast.LENGTH_SHORT).show();

                } else if (id == R.id.navlogout) {
                    Utility.getLogout(MainActivity.this);
                } else if (id == R.id.navshare) {
                    Toast.makeText(getApplicationContext(), "Share Clicked", Toast.LENGTH_SHORT).show();

                }

                drawerLayout.closeDrawer(GravityCompat.START);


                return true;
            }
        });
//        ########################### drawer Implementation End #############################################
        setImage_slider();
        settabLayout();
        setRecyclerView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bnHome);
        image_slider = findViewById(R.id.image_slider);
        tabLayout = findViewById(R.id.tabLayout);
        rvContent = findViewById(R.id.rvContent);
    }

//        ########################### drawer Implementation Open And Close Start #############################################

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (isDoubleBackPressed) {
                super.onBackPressed();
            } else {
                isDoubleBackPressed = true;
                Toast.makeText(this, "Tap Again to Exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isDoubleBackPressed = false;
                    }
                }, 2000);
            }
        }
//        ########################### drawer Implementation Open And Close End #############################################
    }

    public void onClickClose(View view) {
        drawerLayout.closeDrawer(GravityCompat.START);
    }
//    ########################## Option Menu ######################################################

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void setImage_slider() {
        ArrayList<SlideModel> imageList = new ArrayList<>(); // Create image list

        imageList.add(new SlideModel("https://res.cloudinary.com/ddhtmkllj/image/upload/v1676712377/samples/Slider_Mobile/Ansopedia_Variant_gpgwgr.png ", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://res.cloudinary.com/ddhtmkllj/image/upload/v1676712377/samples/Slider_Mobile/Programming_Variant_slg06c.png ", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://res.cloudinary.com/ddhtmkllj/image/upload/v1676712377/samples/Slider_Mobile/Medical_Variant_ppfuud.png ", ScaleTypes.FIT));

        image_slider.setImageList(imageList);
        image_slider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {

            }
        });
    }

    private void settabLayout() {
        ArrayList<String> tabList = new ArrayList<>();
        tabList.add("Programming");
        tabList.add("Aptitude");
        tabList.add("GK");
        tabList.add("Interview");

        for (String tab :
                tabList) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(MainActivity.this, "Selected tab is " + tab.getText(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setRecyclerView() {
        rvContent.setHasFixedSize(true);
        rvContent.setItemViewCacheSize(10);
    }
}
