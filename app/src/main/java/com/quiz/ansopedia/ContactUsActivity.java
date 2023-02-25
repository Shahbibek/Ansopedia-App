package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.models.LoginModel;
import com.quiz.ansopedia.models.LoginRequestModel;
import com.quiz.ansopedia.retrofit.ContentApiImplementer;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends AppCompatActivity {
    Button btnSendBtn;
    TextInputEditText tvEditText;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        btnSendBtn = findViewById(R.id.btnSendBtn);
        tvEditText = findViewById(R.id.tvEditText);
        btnSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tvEditText.getText().toString().isEmpty()) {
                    sendContactMessage();
                }
            }
        });
        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void sendContactMessage() {
        LoginRequestModel loginRequestModel = new LoginRequestModel();
        loginRequestModel.setMessage(tvEditText.getText().toString().trim());
        loginRequestModel.setName(preferences.getString(Constants.name, ""));
        loginRequestModel.setEmail(preferences.getString(Constants.username, ""));
        Utility.showProgress(this);
        if (Utility.isNetConnected(this)) {
            try {
                ContentApiImplementer.sendContactMessage(loginRequestModel,new Callback<List<LoginModel>>() {
                    @Override
                    public void onResponse(Call<List<LoginModel>> call, Response<List<LoginModel>> response) {
                        Utility.dismissProgress(ContactUsActivity.this);
                        if (response.code() == 200) {
                            LoginModel loginModel = response.body().get(0);
                            if (loginModel.getStatus().toLowerCase().contains("success")) {
                                Utility.showAlertDialog(ContactUsActivity.this, loginModel.getStatus(), loginModel.getMessage());
                            } else {
                                Utility.showAlertDialog(ContactUsActivity.this, "Error", "Something went wrong, Please Try Again");
                            }
                        } else {
                            Utility.showAlertDialog(ContactUsActivity.this, "Error", "Something went wrong, Please Try Again");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<LoginModel>> call, Throwable t) {
                        Utility.dismissProgress(ContactUsActivity.this);
                        t.printStackTrace();
                        Utility.showAlertDialog(ContactUsActivity.this, "Error", "Something went wrong, Please Try Again");
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

}