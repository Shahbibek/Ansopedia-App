package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.models.LoginModel;
import com.quiz.ansopedia.models.LoginRequestModel;
import com.quiz.ansopedia.retrofit.ContentApiImplementer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterOtpActivity extends AppCompatActivity {
    TextInputEditText password;
    TextInputLayout t4;
    TextView textView4;
    TextView textView5;
    MaterialButton verifyOTPbtn;
    TextView textView6;
    String email, otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        initView();
        email = getIntent().getStringExtra("email");
        textView4.setText(email);
        timer();
        textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendOTP();
            }
        });
        verifyOTPbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp = password.getText().toString().trim();
                sendOTP();
            }
        });
    }

    private void resendOTP() {
        Utility.showProgress(this);
        LoginRequestModel loginRequestModel = new LoginRequestModel();
        loginRequestModel.setEmail(email);
        if (Utility.isNetConnected(this)) {
            try {
                ContentApiImplementer.sendEmailResetPassword(loginRequestModel,new Callback<List<LoginModel>>() {
                    @Override
                    public void onResponse(Call<List<LoginModel>> call, Response<List<LoginModel>> response) {
                        Utility.dismissProgress(EnterOtpActivity.this);
                        if (response.code() == 200) {
                            LoginModel loginModel = response.body().get(0);
                            if (loginModel.getStatus().toLowerCase().contains("success")) {
                                timer();
                            } else {
                                Utility.showAlertDialog(EnterOtpActivity.this, "Error", "Something went wrong, Please Try Again");
                            }
                        } else {
                            Utility.showAlertDialog(EnterOtpActivity.this, "Error", "Something went wrong, Please Try Again");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<LoginModel>> call, Throwable t) {
                        Utility.dismissProgress(EnterOtpActivity.this);
                        t.printStackTrace();
                        Utility.showAlertDialog(EnterOtpActivity.this, "Error", "Something went wrong, Please Try Again");
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

    private void sendOTP() {
        LoginRequestModel loginRequestModel = new LoginRequestModel();
        loginRequestModel.setEmail(email);
        loginRequestModel.setOtp(otp);
        Utility.showProgress(this);
        if (Utility.isNetConnected(this)) {
            try {
                ContentApiImplementer.sendOTPResetPassword(loginRequestModel,new Callback<List<LoginModel>>() {
                    @Override
                    public void onResponse(Call<List<LoginModel>> call, Response<List<LoginModel>> response) {
                        Utility.dismissProgress(EnterOtpActivity.this);
                        if (response.code() == 200) {
                            LoginModel loginModel = response.body().get(0);
                            if (loginModel.getStatus().toLowerCase().contains("success")) {
                                Constants.TOKEN = loginModel.getToken();
                                startActivity(new Intent(EnterOtpActivity.this, ForgotPasswordLinkActivity.class));
                                finish();
                            } else {
                                Utility.showAlertDialog(EnterOtpActivity.this, "Error", "Something went wrong, Please Try Again");
                            }
                        } else {
                            Utility.showAlertDialog(EnterOtpActivity.this, "Error", "Something went wrong, Please Try Again");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<LoginModel>> call, Throwable t) {
                        Utility.dismissProgress(EnterOtpActivity.this);
                        t.printStackTrace();
                        Utility.showAlertDialog(EnterOtpActivity.this, "Error", "Something went wrong, Please Try Again");
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

    private void timer() {
        textView6.setVisibility(View.GONE);
        new CountDownTimer(300000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                textView5.setText("The otp expire in " + f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                textView6.setVisibility(View.VISIBLE);
                textView5.setText("OTP is expired");
            }
        }.start();
    }

    private void initView() {
        password = findViewById(R.id.password);
        t4 = findViewById(R.id.t4);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        verifyOTPbtn = findViewById(R.id.verifyOTPbtn);
        textView6 = findViewById(R.id.textView6);
    }
}