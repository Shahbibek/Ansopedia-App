package com.quiz.ansopedia;

import static com.quiz.ansopedia.Utility.Constants.contents;
import static com.quiz.ansopedia.Utility.Constants.contentsList;
import static com.quiz.ansopedia.Utility.Constants.subjectsArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.api.ApiResponse;
import com.quiz.ansopedia.models.Contents;
import com.quiz.ansopedia.retrofit.ContentApiImplementer;
import com.quiz.ansopedia.sqlite.CoursesHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroActivity extends AppCompatActivity {
    SharedPreferences preferences;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        ########################### splash screen start #############################################
        SplashScreen.installSplashScreen(this);
//        ########################### splash screen End #############################################
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
//        ############################# Get Data From DataBase #####################################
        CoursesHelper db = new CoursesHelper(this);
        subjectsArrayList = new ArrayList<>();
        subjectsArrayList = db.readData(this);

        ImageView ivProgress = findViewById(R.id.ivProgress);
        try{
            Glide.with(this).load(R.drawable.ansopedia_loader_new).into(ivProgress);
        }
        catch(Exception e){
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if(Utility.isNetConnected(this)){
            try{
                if (preferences.getBoolean(Constants.isLogin, false)) {
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    Constants.Email = currentUser.getEmail();
                    currentUser.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                        @Override
                        public void onSuccess(GetTokenResult getTokenResult) {
                            Constants.TOKEN = getTokenResult.getToken();
                            updateUI(currentUser);
                        }
                    });
                } else {
                    startActivity(new Intent(this, SignInActivity.class));
                    finish();
                }
            }catch (Exception e){
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        }else{
            Utility.showAlertDialog(IntroActivity.this, "Failed", "Please, Connect Internet, And Try Again !!..");
        }

//        if (preferences.getBoolean(Constants.isLogin, false)) {
//            Utility.getUserDetail(this);
//            Utility.getLogin(this);
//            getContent();
//        } else {

//            startActivity(new Intent(this, SignInActivity.class));
//            finish();
//        }
    }
    private void updateUI(FirebaseUser user) {
        if(Utility.isNetConnected(this)){
            if(user != null){
                preferences.edit().putBoolean(Constants.isLogin, true).apply();
                preferences.edit().putString(Constants.token, Constants.TOKEN ).apply();
                preferences.edit().putString(Constants.username,  Constants.Email).apply();
                getContent();
            }
        }else{
            Utility.showAlertDialog(IntroActivity.this, "Failed", "Please, Connect Internet, And Try Again !!..");
        }

    }
    private void getContent() {
//        Utility.showProgressGif(getContext());
        if (Utility.isNetConnected(this)) {
            try {
                Utility.getUserDetail(this);
                ContentApiImplementer.getContent(new Callback<ApiResponse<List<Contents>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<Contents>>> call, Response<ApiResponse<List<Contents>>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getData().size() != 0) {
                                contents = (Contents) response.body().getData().get(0);
                                contentsList = new ArrayList<>();
                                contentsList = (ArrayList<Contents>) response.body().getData();
                                startActivity(new Intent(IntroActivity.this, MainActivity.class));
                                finish();
                            }
                        }else if(response.code() == 500) {
                            showTryAgainDialog( "Failed", "Server Error, Please Try Again");
                        }else{
                            showTryAgainDialog( "Error", "Something went wrong, Please Try Again");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<Contents>>> call, Throwable t) {
                        t.printStackTrace();
                        showTryAgainDialog("Error", "Something went wrong, Please Try Again");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        } else {
            Utility.showAlertDialog(IntroActivity.this, "Error", "Please Connect to Internet");
        }
    }

    private void showTryAgainDialog(String title, String msg) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        getContent();
                    }
                })
                .create().show();
    }
}