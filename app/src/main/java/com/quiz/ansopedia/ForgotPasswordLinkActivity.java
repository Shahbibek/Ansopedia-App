package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordLinkActivity extends AppCompatActivity {
    TextInputLayout t5, t4;
    TextInputEditText password, confirmPassword;
    MaterialButton updatePasswordBtn;
    RelativeLayout svMain;
    ImageView ivBack;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_link);
        initView();
        email = getIntent().getStringExtra("email");
        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uPass = password.getText().toString();
                String uCNFPass = confirmPassword.getText().toString();
                Utility.hideSoftKeyboard(ForgotPasswordLinkActivity.this);
                t4.setErrorEnabled(false);
                t5.setErrorEnabled(false);
                if (isValidateCredentials()) {
                    sendNewPassword();
                } else {
                    if (uPass.isEmpty()) {
                        t4.setErrorEnabled(true);
                        t4.setError("* Please Enter Password");
                    }else if(!uPass.matches( ".{8,}")){
                        t4.setErrorEnabled(true);
                        t4.setError("* Password must be 8 digit");
                    }else if(!Utility.isValidPassword(uPass)){
                        t4.setErrorEnabled(true);
                        t4.setError("* password must contain uppercase, lowercase, one digit and one special character");
                    }else if(uCNFPass.isEmpty()){
                        t5.setErrorEnabled(true);
                        t5.setError("* Please Enter Password");
                    }else if(!(uCNFPass.matches(uPass))){
                        t5.setErrorEnabled(true);
                        t5.setError("* Password must be Same");
                    };
                }
            }
        });

//         ############################# Hide KeyBoard OnClick #######################################
        svMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {Utility.hideSoftKeyboard(ForgotPasswordLinkActivity.this);}
        });
//        ############################# Hide KeyBoard OnClick #######################################

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void sendNewPassword() {
        LoginRequestModel loginRequestModel = new LoginRequestModel();
        loginRequestModel.setPassword(password.getText().toString().trim());
//        loginRequestModel.setEmail(getIntent().getStringExtra("email").toString().trim());
        loginRequestModel.setPassword_confirmation(confirmPassword.getText().toString().trim());
        Utility.showProgress(this);
        if (Utility.isNetConnected(this)) {
            try {
                    ContentApiImplementer.sendNewPasswordResetPassword(loginRequestModel, new Callback<ApiResponse<LoginModel>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<LoginModel>> call, Response<ApiResponse<LoginModel>> response) {
                            Utility.dismissProgress(ForgotPasswordLinkActivity.this);
                            if (response.isSuccessful()) {
                                if (response.body().getStatus().toLowerCase().contains("success")) {
                                    new AlertDialog.Builder(ForgotPasswordLinkActivity.this)
                                            .setTitle(response.body().getStatus().toString().trim())
                                            .setMessage(response.body().getMessage().toString().trim())
                                            .setCancelable(false)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                    startActivity(new Intent(ForgotPasswordLinkActivity.this, SignInActivity.class));
                                                    finish();
                                                }
                                            })
                                            .create().show();

                                } else {
                                    Utility.showAlertDialog(ForgotPasswordLinkActivity.this, "Error", "Something went wrong, Please Try Again");
                                }
                            }
                            if(response.errorBody() != null){
                                try {
                                    JSONObject Error = new JSONObject(response.errorBody().string());
                                    Utility.showAlertDialog(ForgotPasswordLinkActivity.this, Error.getString("status") , Error.getString("message"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Utility.dismissProgress(ForgotPasswordLinkActivity.this);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Utility.dismissProgress(ForgotPasswordLinkActivity.this);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<LoginModel>> call, Throwable t) {
                            Utility.dismissProgress(ForgotPasswordLinkActivity.this);
                            t.printStackTrace();
                            Utility.showAlertDialog(ForgotPasswordLinkActivity.this, "Error", "Something went wrong, Please Try Again");
                        }
                    });
//                ContentApiImplementer.sendNewPasswordResetPassword(loginRequestModel,new Callback<List<LoginModel>>() {
//                    @Override
//                    public void onResponse(Call<List<LoginModel>> call, Response<List<LoginModel>> response) {
//                        Utility.dismissProgress(ForgotPasswordLinkActivity.this);
//                        if (response.code() == 200) {
//                            LoginModel loginModel = response.body().get(0);
//                            if (loginModel.getStatus().toLowerCase().contains("success")) {
//                                startActivity(new Intent(ForgotPasswordLinkActivity.this, SignInActivity.class));
//                                finish();
//                            } else {
//                                Utility.showAlertDialog(ForgotPasswordLinkActivity.this, "Error", "Something went wrong, Please Try Again");
//                            }
//                        } else {
//                            Utility.showAlertDialog(ForgotPasswordLinkActivity.this, "Error", "Something went wrong, Please Try Again");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<LoginModel>> call, Throwable t) {
//                        Utility.dismissProgress(ForgotPasswordLinkActivity.this);
//                        t.printStackTrace();
//                        Utility.showAlertDialog(ForgotPasswordLinkActivity.this, "Error", "Something went wrong, Please Try Again");
//                    }
//                });
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
        svMain = findViewById(R.id.svMain);
        ivBack = findViewById(R.id.ivBack);
    }

    private boolean isValidateCredentials() {
        String uPass = password.getText().toString();
        String uCNFPass = confirmPassword.getText().toString();
        if (!uPass.isEmpty() && !uCNFPass.isEmpty() && uPass.matches( ".{8,}") && (uCNFPass.matches(uPass)) && Utility.isValidPassword(uPass)) {
            return true;
        } else {
            return false;
        }
    }

}