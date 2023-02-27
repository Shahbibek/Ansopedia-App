package com.quiz.ansopedia;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.fragments.HomeFragment;
import com.quiz.ansopedia.fragments.LeaderBoardFragment;
import com.quiz.ansopedia.fragments.NotificationFragment;
import com.quiz.ansopedia.fragments.QuizFragment;
import com.quiz.ansopedia.retrofit.ContentApiImplementer;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    boolean isDoubleBackPressed = false;
    TextView tvToolbar;
    CircleImageView profileMenu;
    SharedPreferences preferences;

    @Override
    protected void onStart() {
        super.onStart();
        try {
            if (!Utility.userDetail.getAvatar().substring(33).equalsIgnoreCase("undefined")) {
                try {
                    Glide.with(this)
                            .load(Utility.userDetail.getAvatar())
                            .into(profileMenu);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
//        Utility.getLogin(this);
        bottomNavigationView.setSelectedItemId(R.id.navhome);
        try {
            String fragment = getIntent().getStringExtra("fragment");
            if (fragment.equalsIgnoreCase("HomeFragment")) {
                loadFrag(new HomeFragment());
                bottomNavigationView.setSelectedItemId(R.id.navHome);
            } else if (fragment.equalsIgnoreCase("NotificationFragment")){
                loadFrag(new NotificationFragment());
                bottomNavigationView.setSelectedItemId(R.id.navNotification);
            } else if (fragment.equalsIgnoreCase("QuizFragment")){
                loadFrag(new QuizFragment());
                bottomNavigationView.setSelectedItemId(R.id.navList);
            } else if (fragment.equalsIgnoreCase("LeaderBoardFragment")){
                loadFrag(new LeaderBoardFragment());
                bottomNavigationView.setSelectedItemId(R.id.navBookmark);
            }
        } catch (Exception e){
            loadFrag(new HomeFragment());
            e.printStackTrace();
        }
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
                    tvToolbar.setText("My Courses");
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
                    loadFrag(new HomeFragment());
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
//        ########################### User profile Icon Start #############################################
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });
//        ########################### User profile Icon End #############################################

//        ########################### Profile menu Bar Shown Start #############################################
        try {
            if (!Utility.userDetail.getAvatar().substring(33).equalsIgnoreCase("undefined")) {
                try {
//                GlideUrl url = new GlideUrl(ContentApiImplementer.BASE_URL + "user/avatar", new LazyHeaders.Builder()
//                        .addHeader("Authorization", "Bearer " + Constants.TOKEN)
//                        .build());
                    Glide.with(this)
                            .load(Utility.userDetail.getAvatar())
                            .into(profileMenu);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


//        ########################### Profile menu Bar Shown End #############################################
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bnHome);
        frameLayout = findViewById(R.id.flHome);
        tvToolbar = findViewById(R.id.tvToolbar);
        profileMenu = findViewById(R.id.profileMenu);
    }

//        ########################### drawer Implementation Open And Close Start ###############################

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (isDoubleBackPressed) {
                new AlertDialog.Builder(this)
                        .setMessage("Do you want to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create().show();
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
//    ########################## Option Menu ##################################################################

//    @SuppressLint("CheckResult")
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search_menu, menu);
//        MenuItem menuItem = menu.findItem(R.id.searchView);
//
////    ########################## Profile Menu Bar Start   ##################################################################
//
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        MenuItem profile = menu.findItem(R.id.userIcons);
//        profile.setIcon(profileDrawable);
//
////    ########################## Profile Menu Bar End   ##################################################################
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }
//
////    ############################### Menu bar Call start ########################
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch(item.getItemId()){
//            case R.id.userIcons:
//                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

//        ############################### Menu bar Call end ######################################

    public void loadFrag(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flHome, fragment);
        fragmentTransaction.commit();
    }


}
