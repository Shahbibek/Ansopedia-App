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
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
    ImageSlider image_slider;
    TabLayout tabLayout;
    RecyclerView rvContent;
    boolean isDoubleBackPressed = false;
    Contents contents;
    CourseAdapter courseAdapter;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Utility.getLogin(this);
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
        setImage_slider();
        getContent();
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

    private void settabLayout(String branch_name) {
        ArrayList<String> tabList = new ArrayList<>();
        for (Branch branch : contents.getBranch()) {
            tabList.add(branch.getBranch_name());
        }

        for (String tab : tabList) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setRecyclerView(getSubjects(tab.getText().toString()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setRecyclerView(ArrayList<Subjects> subjects) {
        rvContent.setHasFixedSize(true);
        rvContent.setItemViewCacheSize(10);
        courseAdapter = new CourseAdapter(this, subjects);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        rvContent.setLayoutManager(layoutManager);
        rvContent.setAdapter(courseAdapter);
    }

    private void getContent() {
        Utility.showProgress(MainActivity.this);
        if (Utility.isNetConnected(this)) {
            try {
                ContentApiImplementer.getContent(new Callback<List<Contents>>() {
                    @Override
                    public void onResponse(Call<List<Contents>> call, Response<List<Contents>> response) {
                        Utility.dismissProgress(MainActivity.this);
                        if (response.code() == 200) {
                            if (response.body().size() != 0) {
                                contents = response.body().get(0);
                                settabLayout(contents.getBranch().get(0).getBranch_name());
                                setRecyclerView(getSubjects(contents.getBranch().get(0).getBranch_name()));
                            }
                        } else {
                            Utility.showAlertDialog(MainActivity.this, "Error", "Something went wrong, Please Try Again");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Contents>> call, Throwable t) {
                        Utility.dismissProgress(MainActivity.this);
                        t.printStackTrace();
                        Utility.showAlertDialog(MainActivity.this, "Error", "Something went wrong, Please Try Again");
                    }
                });
            } catch (Exception e) {
                Utility.dismissProgress(this);
                e.printStackTrace();
            }
        } else {
            Utility.dismissProgress(this);
            Utility.showAlertDialog(this, "Error", "Please Connect to Internet");
        }
    }

    private ArrayList<Subjects> getSubjects(String branch_name) {
        ArrayList<Subjects> tempList = new ArrayList<>();
        for (Branch branch : contents.getBranch()) {
            if (branch.getBranch_name().equalsIgnoreCase(branch_name)) {
                tempList = (ArrayList<Subjects>) branch.getSubjects();
            }
        }
        return tempList;
    }
}
