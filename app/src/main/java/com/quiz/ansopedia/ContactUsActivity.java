package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.api.ApiResponse;
import com.quiz.ansopedia.models.LoginModel;
import com.quiz.ansopedia.models.LoginRequestModel;
import com.quiz.ansopedia.retrofit.ContentApiImplementer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends AppCompatActivity {
    Button btnSendBtn;
    TextInputEditText tvEditText;
    TextInputLayout t1;
    SharedPreferences preferences;
    RelativeLayout svMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        initView();

        btnSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = Objects.requireNonNull(tvEditText.getText()).toString().trim();
                Utility.hideSoftKeyboard(ContactUsActivity.this);
                t1.setErrorEnabled(false);
                if (isValidateCredentials()) {
                    sendContactMessage();
                }else{
                    try{
                        if (message.isEmpty()) {
                            t1.setErrorEnabled(true);
                            t1.setError("* Please enter your query !!!..");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                }
//                if (!tvEditText.getText().toString().isEmpty()) {
//                    sendContactMessage();
//                }
            }
        });
        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        svMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(ContactUsActivity.this);
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
                    ContentApiImplementer.sendContactMessage(loginRequestModel, new Callback<ApiResponse<LoginModel>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<LoginModel>> call, Response<ApiResponse<LoginModel>> response) {
                            Utility.dismissProgress(ContactUsActivity.this);
                            if (response.isSuccessful()) {
                                    Utility.showAlertDialog(ContactUsActivity.this, response.body().getStatus().toString().trim(), response.body().getMessage().toString().trim());
                                    tvEditText.setText("");
                            }
                            if(response.errorBody() != null){
                                try {
                                    JSONObject Error = new JSONObject(response.errorBody().string());
                                    Utility.showAlertDialog(ContactUsActivity.this, Error.getString("status") , Error.getString("message"));
                                } catch (Exception e) {
                                    Utility.dismissProgress(ContactUsActivity.this);
                                    e.printStackTrace();
                                    FirebaseCrashlytics.getInstance().recordException(e);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<LoginModel>> call, Throwable t) {
                            Utility.dismissProgress(ContactUsActivity.this);
                            t.printStackTrace();
                            Utility.showAlertDialog(ContactUsActivity.this, "Error", "Something went wrong, Please Try Again");
                        }
                    });
//                ContentApiImplementer.sendContactMessage(loginRequestModel,new Callback<List<LoginModel>>() {
//                    @Override
//                    public void onResponse(Call<List<LoginModel>> call, Response<List<LoginModel>> response) {
//                        Utility.dismissProgress(ContactUsActivity.this);
//                        if (response.code() == 200) {
//                            LoginModel loginModel = response.body().get(0);
//                            if (loginModel.getStatus().toLowerCase().contains("success")) {
//                                Utility.showAlertDialog(ContactUsActivity.this, loginModel.getStatus(), loginModel.getMessage());
//                            } else {
//                                Utility.showAlertDialog(ContactUsActivity.this, "Error", "Something went wrong, Please Try Again");
//                            }
//                        } else {
//                            Utility.showAlertDialog(ContactUsActivity.this, "Error", "Something went wrong, Please Try Again");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<LoginModel>> call, Throwable t) {
//                        Utility.dismissProgress(ContactUsActivity.this);
//                        t.printStackTrace();
//                        Utility.showAlertDialog(ContactUsActivity.this, "Error", "Something went wrong, Please Try Again");
//                    }
//                });
            } catch (Exception e) {
                Utility.dismissProgress(this);
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        } else {
            Utility.dismissProgress(this);
            Utility.showAlertDialog(this, "Error", "Please Connect to Internet");
        }
    }

    private void initView() {
        t1 = findViewById(R.id.t1);
        svMain = findViewById(R.id.svMain);
        btnSendBtn = findViewById(R.id.btnSendBtn);
        tvEditText = findViewById(R.id.tvEditText);
    }
    private boolean isValidateCredentials() {
        String message = Objects.requireNonNull(tvEditText.getText()).toString().trim();
        return !message.isEmpty();
    }

}