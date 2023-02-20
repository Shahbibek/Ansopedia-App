package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.models.LoginModel;
import com.quiz.ansopedia.models.LoginRequestModel;
import com.quiz.ansopedia.retrofit.ContentApiImplementer;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {
    TextInputLayout t1;
    TextInputEditText userEmail;
    MaterialButton sendOTPBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        t1 = findViewById(R.id.t1);
        userEmail = findViewById(R.id.userEmail);
        sendOTPBtn = findViewById(R.id.sendOTPBtn);
        sendOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
    }

    private void sendEmail() {
        Utility.showProgress(ForgetPasswordActivity.this);
        LoginRequestModel loginRequestModel = new LoginRequestModel();
        loginRequestModel.setEmail(userEmail.getText().toString().trim());
        if (Utility.isNetConnected(this)) {
            try {
                ContentApiImplementer.sendEmailResetPassword(loginRequestModel,new Callback<List<LoginModel>>() {
                    @Override
                    public void onResponse(Call<List<LoginModel>> call, Response<List<LoginModel>> response) {
                        Utility.dismissProgress(ForgetPasswordActivity.this);
                        if (response.code() == 200) {
                            LoginModel loginModel = response.body().get(0);
                            if (loginModel.getStatus().toLowerCase().contains("success")) {
                                startActivity(new Intent(ForgetPasswordActivity.this, EnterOtpActivity.class)
                                        .putExtra("email", userEmail.getText().toString().trim()));
                                finish();
                            } else {
                                Utility.showAlertDialog(ForgetPasswordActivity.this, "Error", "Something went wrong, Please Try Again");
                            }
                        } else {
                            Utility.showAlertDialog(ForgetPasswordActivity.this, "Error", "Something went wrong, Please Try Again");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<LoginModel>> call, Throwable t) {
                        Utility.dismissProgress(ForgetPasswordActivity.this);
                        t.printStackTrace();
                        Utility.showAlertDialog(ForgetPasswordActivity.this, "Error", "Something went wrong, Please Try Again");
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