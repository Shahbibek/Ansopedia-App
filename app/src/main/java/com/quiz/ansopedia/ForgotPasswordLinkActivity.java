package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.models.LoginModel;
import com.quiz.ansopedia.models.LoginRequestModel;
import com.quiz.ansopedia.retrofit.ContentApiImplementer;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordLinkActivity extends AppCompatActivity {
    TextInputLayout t5, t4;
    TextInputEditText password, confirmPassword;
    MaterialButton updatePasswordBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_link);
        initView();
        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNewPassword();
            }
        });
    }

    private void sendNewPassword() {
        LoginRequestModel loginRequestModel = new LoginRequestModel();
        loginRequestModel.setPassword(password.getText().toString().trim());
        loginRequestModel.setPassword_confirmation(confirmPassword.getText().toString().trim());
        Utility.showProgress(this);
        if (Utility.isNetConnected(this)) {
            try {
                ContentApiImplementer.sendNewPasswordResetPassword(loginRequestModel,new Callback<List<LoginModel>>() {
                    @Override
                    public void onResponse(Call<List<LoginModel>> call, Response<List<LoginModel>> response) {
                        Utility.dismissProgress(ForgotPasswordLinkActivity.this);
                        if (response.code() == 200) {
                            LoginModel loginModel = response.body().get(0);
                            if (loginModel.getStatus().toLowerCase().contains("success")) {
                                startActivity(new Intent(ForgotPasswordLinkActivity.this, SignInActivity.class));
                                finish();
                            } else {
                                Utility.showAlertDialog(ForgotPasswordLinkActivity.this, "Error", "Something went wrong, Please Try Again");
                            }
                        } else {
                            Utility.showAlertDialog(ForgotPasswordLinkActivity.this, "Error", "Something went wrong, Please Try Again");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<LoginModel>> call, Throwable t) {
                        Utility.dismissProgress(ForgotPasswordLinkActivity.this);
                        t.printStackTrace();
                        Utility.showAlertDialog(ForgotPasswordLinkActivity.this, "Error", "Something went wrong, Please Try Again");
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

    private void initView() {
        t5 = findViewById(R.id.t5);
        t4 = findViewById(R.id.t4);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.comfirmPassword);
        updatePasswordBtn = findViewById(R.id.updatePasswordBtn);
    }
}