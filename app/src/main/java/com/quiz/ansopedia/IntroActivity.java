package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.quiz.ansopedia.Utility.Constants;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        ########################### splash screen start #############################################
        SplashScreen.installSplashScreen(this);
//        ########################### splash screen End #############################################
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        Constants.TOKEN = preferences.getString(Constants.token, "");
        boolean  isLogin = preferences.getBoolean(Constants.isLogin, false);
        if (isLogin) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, SignInActivity.class));
        }
        finish();
    }
}