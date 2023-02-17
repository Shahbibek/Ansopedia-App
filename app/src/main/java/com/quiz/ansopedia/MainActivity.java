package com.quiz.ansopedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        ########################### splash screen start #############################################
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
//        ########################### splash screen End #############################################
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ########################### drawer Implementation Start #############################################
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id==R.id.navhome){
                    Toast.makeText(getApplicationContext(),"Home Clicked",Toast.LENGTH_SHORT).show();

                }else if (id==R.id.navprofile){
                    Toast.makeText(getApplicationContext(),"Profile Clicked",Toast.LENGTH_SHORT).show();

                }else if (id==R.id.navabout){
                    Toast.makeText(getApplicationContext(),"About Clicked",Toast.LENGTH_SHORT).show();

                }else if (id==R.id.navcourse){
                    Toast.makeText(getApplicationContext(),"Course Clicked",Toast.LENGTH_SHORT).show();

                }else if (id==R.id.navlogout){
                    Toast.makeText(getApplicationContext(),"Logout Clicked",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getApplicationContext(),"Share Clicked",Toast.LENGTH_SHORT).show();

                }

                drawerLayout.closeDrawer(GravityCompat.START);


                return true;
            }
        });
//        ########################### drawer Implementation End #############################################

    }

//        ########################### drawer Implementation Open And Close Start #############################################

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
//        ########################### drawer Implementation Open And Close End #############################################
    }
}
