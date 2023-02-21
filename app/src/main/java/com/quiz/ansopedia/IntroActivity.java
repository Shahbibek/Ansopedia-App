package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.Utility.Utility;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        ########################### splash screen start #############################################
        SplashScreen.installSplashScreen(this);
//        ########################### splash screen End #############################################
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Utility.getLogin(this);
    }
}