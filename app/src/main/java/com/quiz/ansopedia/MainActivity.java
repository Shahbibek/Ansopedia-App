package com.quiz.ansopedia;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.adapter.CourseAdapter;
import com.quiz.ansopedia.fragments.HomeFragment;
import com.quiz.ansopedia.fragments.LeaderBoardFragment;
import com.quiz.ansopedia.fragments.NotificationFragment;
import com.quiz.ansopedia.fragments.QuizFragment;
import com.quiz.ansopedia.models.Branch;
import com.quiz.ansopedia.models.Contents;
import com.quiz.ansopedia.models.LoginModel;
import com.quiz.ansopedia.models.Subjects;
import com.quiz.ansopedia.retrofit.ContentApiImplementer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    boolean isDoubleBackPressed = false;
    TextView tvToolbar;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Utility.getLogin(this);
        bottomNavigationView.setSelectedItemId(R.id.navhome);
        loadFrag(new HomeFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navHome) {
                    tvToolbar.setText("Home");
                    loadFrag(new HomeFragment());
                } else if (id == R.id.navNotification) {
                    tvToolbar.setText("Notification");
                    loadFrag(new NotificationFragment());
                } else if (id == R.id.navList) {
                    tvToolbar.setText("Courses");
                    loadFrag(new QuizFragment());
                } else if (id == R.id.navBookmark) {
                    tvToolbar.setText("Leader Board");
                    loadFrag(new LeaderBoardFragment());
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

                } else if (id == R.id.navProfile) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));


                } else if (id == R.id.navAboutUs) {
                    startActivity(new Intent(MainActivity.this, AboutUsActivity.class));


                } else if (id == R.id.navContactUs) {
                    startActivity(new Intent(MainActivity.this, ContactUsActivity.class));

                } else if (id == R.id.navlogout) {
                    Utility.getLogout(MainActivity.this);
                } else if (id == R.id.navshare) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.quiz.ansopedia");
                    startActivity(Intent.createChooser(intent, "choose one"));
                }

                drawerLayout.closeDrawer(GravityCompat.START);


                return true;
            }
        });
//        ########################### drawer Implementation End #############################################
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bnHome);
        frameLayout = findViewById(R.id.flHome);
        tvToolbar = findViewById(R.id.tvToolbar);
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
        MenuItem menuItem = menu.findItem(R.id.searchView);
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

    public void loadFrag(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flHome, fragment);
        fragmentTransaction.commit();
    }
}
