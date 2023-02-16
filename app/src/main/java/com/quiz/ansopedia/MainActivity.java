package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        splash screen start
            SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
//        splash screen end
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}