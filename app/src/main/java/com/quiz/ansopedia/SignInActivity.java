package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
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

public class SignInActivity extends AppCompatActivity {
    Button btnLogin;
    TextInputLayout t1, t2;
    TextInputEditText etUsername, etPassword;
    SharedPreferences preferences;
    CheckBox checkbox;
    TextView textViewForgetPassword, textViewSignUp;
    SignInButton sign_in_button;
    RelativeLayout svMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        initView();
        etUsername.setText(preferences.getString(Constants.username, ""));
        etPassword.setText(preferences.getString(Constants.password, ""));
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                Utility.hideSoftKeyboard(SignInActivity.this);
                t1.setErrorEnabled(false);
                t2.setErrorEnabled(false);
                if (isValidateCredentials()) {
                    getLogin();
                } else {
                    if (username.isEmpty()) {
                        t1.setErrorEnabled(true);
                        t1.setError("Please Enter Email");
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
                        t1.setErrorEnabled(true);
                        t1.setError("Please Enter Valid Email");
                    }else if (password.isEmpty()){
                        t2.setErrorEnabled(true);
                        t2.setError("Please Enter Password");
                    }else if(!password.matches( ".{8,}")){
                        t2.setErrorEnabled(true);
                        t2.setError("Password must be of 8 digit");
                    };
                }
            }
        });

        textViewForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, ForgetPasswordActivity.class));
                finish();
            }
        });
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, SignupActivity.class));
                finish();
            }
        });
        svMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(SignInActivity.this);
            }
        });
    }

    private void initView() {
        btnLogin = findViewById(R.id.btnLogin);
        etPassword = findViewById(R.id.etPassword);
        etUsername = findViewById(R.id.etUsername);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        checkbox = findViewById(R.id.checkbox);
        textViewForgetPassword = findViewById(R.id.textViewForgetPassword);
        textViewSignUp = findViewById(R.id.textViewSignUp);
        sign_in_button = findViewById(R.id.sign_in_button);
        svMain = findViewById(R.id.svMain);

    }

    private void getLogin() {
        Utility.showProgress(this);
        LoginRequestModel loginRequestModel = new LoginRequestModel();
        loginRequestModel.setEmail(etUsername.getText().toString().trim());
        loginRequestModel.setPassword(etPassword.getText().toString().trim());

        if (checkbox.isChecked()) {
            preferences.edit().putString(Constants.username, etUsername.getText().toString().trim()).apply();
            preferences.edit().putString(Constants.password, etPassword.getText().toString().trim()).apply();
        }
        if (Utility.isNetConnected(this)) {
            try {
                ContentApiImplementer.getLogin(loginRequestModel, new Callback<List<LoginModel>>() {
                    @Override
                    public void onResponse(Call<List<LoginModel>> call, Response<List<LoginModel>> response) {
                        Utility.dismissProgress(SignInActivity.this);
                        if (response.code() == 200) {
                            LoginModel loginModel = (LoginModel) response.body().get(0);
                            if (loginModel.getStatus().equalsIgnoreCase("success")) {
                                Constants.TOKEN = loginModel.getToken();
                                preferences.edit().putBoolean(Constants.isLogin, true).apply();
                                preferences.edit().putString(Constants.token, loginModel.getToken()).apply();
                                Toast.makeText(SignInActivity.this, "" + loginModel.getMessage(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Utility.showAlertDialog(SignInActivity.this, loginModel.getStatus(), loginModel.getMessage());
                            }
                        } else {
                            Utility.showAlertDialog(SignInActivity.this, "Error", "Something went wrong, Please Try Again");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<LoginModel>> call, Throwable t) {
                        Utility.dismissProgress(SignInActivity.this);
                        t.printStackTrace();
                        Utility.showAlertDialog(SignInActivity.this, "Error", "Something went wrong, Please Try Again");
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

    private boolean isValidateCredentials() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        if (!username.isEmpty() && !password.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(username).matches() && password.matches( ".{8,}")) {
            return true;
        } else {
            return false;
        }
    }
}