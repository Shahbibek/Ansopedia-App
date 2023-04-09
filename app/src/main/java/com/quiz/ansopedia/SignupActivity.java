package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
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

public class SignupActivity extends AppCompatActivity {
    TextView signInTextBtn;
    MaterialButton signUpBtn;
    TextInputEditText userName, userEmail, password, confirmPassword, userName_text;
    TextInputLayout userNameTextField, t2, t3, passwordTextField, confirmPasswordTextField;
    String uName, uUserName, uEmail, pass, confirmPass;
    SharedPreferences preferences;
    RelativeLayout svMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
        preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        signInTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, SignInActivity.class));
                finish();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uName = userName.getText().toString().trim();
                uUserName = userName_text.getText().toString().trim();
                uEmail = userEmail.getText().toString().trim();
                pass = password.getText().toString();
                confirmPass = confirmPassword.getText().toString();
                Utility.hideSoftKeyboard(SignupActivity.this);
                userNameTextField.setErrorEnabled(false);
                t2.setErrorEnabled(false);
                t3.setErrorEnabled(false);
                passwordTextField.setErrorEnabled(false);
                confirmPasswordTextField.setErrorEnabled(false);
                if (isValidateCredentials()) {
                    getRegister();
                } else {
                    if (uName.isEmpty()) {
                        userNameTextField.setErrorEnabled(true);
                        userNameTextField.setError("* Please Enter Name");
                    }
                    if (!uName.matches("[a-zA-Z ]+")) {
                        userNameTextField.setErrorEnabled(true);
                        userNameTextField.setError("* Enter Valid Name");
                    }
                    if (!uName.matches(".{3,}")) {
                        userNameTextField.setErrorEnabled(true);
                        userNameTextField.setError("* Name must be of at least 3 character");
                    }
                    if (uUserName.isEmpty()) {
                        t2.setErrorEnabled(true);
                        t2.setError("* Please Enter Name");
                    }
                    if (!uUserName.matches(".{3,}")) {
                        t2.setErrorEnabled(true);
                        t2.setError("* Username must be of at least 3 character");
                    }
                    if (!Patterns.EMAIL_ADDRESS.matcher(uEmail).matches()) {
                        t3.setErrorEnabled(true);
                        t3.setError("* Invalid Email");
                    }
                    if (uEmail.isEmpty()) {
                        t3.setErrorEnabled(true);
                        t3.setError("* Please Enter Email");
                    }
                    if (!pass.matches(".{8,}")) {
                        passwordTextField.setErrorEnabled(true);
                        passwordTextField.setError("* Password must be of 8 digit");
                    }
                    if (!Utility.isValidPassword(pass)) {
                        passwordTextField.setErrorEnabled(true);
                        passwordTextField.setError("* password must contain uppercase, lowercase, one digit and one special character");
                    }
                    if (pass.isEmpty()) {
                        passwordTextField.setErrorEnabled(true);
                        passwordTextField.setError("* Please Enter Password");
                    }
                    if (!confirmPass.matches(pass)) {
                        confirmPasswordTextField.setErrorEnabled(true);
                        confirmPasswordTextField.setError("* Password must be same");
                    }
                    if (confirmPass.isEmpty()) {
                        confirmPasswordTextField.setErrorEnabled(true);
                        confirmPasswordTextField.setError("* Please Enter Confirm Password");
                    }
                }
            }
        });
        svMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(SignupActivity.this);
            }
        });
    }

    private void initView() {
        signInTextBtn = findViewById(R.id.signInTextBtn);
        signUpBtn = findViewById(R.id.signUpBtn);
        userName_text = findViewById(R.id.userName_text);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        userNameTextField = findViewById(R.id.userNameTextField);
        t3 = findViewById(R.id.t3);
        t2 = findViewById(R.id.t2);
        passwordTextField = findViewById(R.id.passwordTextField);
        confirmPasswordTextField = findViewById(R.id.confirmPasswordTextField);
        svMain = findViewById(R.id.svMain);
    }

    private void getRegister() {
        Utility.showProgress(this);
        LoginRequestModel loginRequestModel = new LoginRequestModel();
        loginRequestModel.setUserName(uUserName);
        loginRequestModel.setName(uName);
        loginRequestModel.setEmail(uEmail);
        loginRequestModel.setPassword(pass);
        loginRequestModel.setPassword_confirmation(confirmPass);
        loginRequestModel.setTc(true);
        if (Utility.isNetConnected(this)) {
            try {
                    ContentApiImplementer.getRegister(loginRequestModel, new Callback<ApiResponse<LoginModel>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<LoginModel>> call, Response<ApiResponse<LoginModel>> response) {
                            Utility.dismissProgress(SignupActivity.this);
                            if (response.isSuccessful()) {
                                LoginModel loginModel = response.body().getData();
//                                Constants.TOKEN = loginModel.getToken();
//                                preferences.edit().putString(Constants.token, loginModel.getToken()).apply();
                                preferences.edit().putString(Constants.username, uEmail).apply();
                                preferences.edit().putString(Constants.name, uName).apply();
                                preferences.edit().putString(Constants.password, confirmPass).apply();
                                preferences.edit().putBoolean(Constants.isLogin, false).apply();
//                                Utility.showAlertDialog(SignupActivity.this, loginModel.getStatus() , loginModel.getMessage());
//                                Utility.showAlertDialog(SignupActivity.this, response.body().getStatus().toString().trim() , response.body().getMessage().toString().trim());
//                                Toast.makeText(SignupActivity.this, "" + loginModel.getMessage(), Toast.LENGTH_SHORT).show();
                                new AlertDialog.Builder(SignupActivity.this)
                                        .setTitle(response.body().getStatus().toString().trim())
                                        .setMessage(response.body().getMessage().toString().trim())
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                startActivity(new Intent(SignupActivity.this, SignInActivity.class));
                                                finish();
                                            }
                                        })
                                        .create().show();
                            }
                            if(response.errorBody() != null){
                                try {
                                    JSONObject Error = new JSONObject(response.errorBody().string());
                                    Utility.showAlertDialog(SignupActivity.this, Error.getString("status") , Error.getString("message"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Utility.dismissProgress(SignupActivity.this);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Utility.dismissProgress(SignupActivity.this);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<LoginModel>> call, Throwable t) {
                            Utility.dismissProgress(SignupActivity.this);
                            Utility.showAlertDialog(SignupActivity.this, "Error", "Something went wrong, Please Try Again");
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                Utility.dismissProgress(this);
            }
        } else {
            Utility.dismissProgress(this);
            Utility.showAlertDialog(this, "Error", "Please connect to Internet");
        }
    }

    private boolean isValidateCredentials() {
        uName = userName.getText().toString().trim();
        uUserName = userName_text.getText().toString().trim();
        uEmail = userEmail.getText().toString().trim();
        pass = password.getText().toString();
        confirmPass = confirmPassword.getText().toString();
        if (!uUserName.isEmpty()
                && uUserName.matches(".{3,}")
                && !uName.isEmpty()
                && uName.matches(".{3,}")
                && !uEmail.isEmpty()
                && !pass.isEmpty()
                && !confirmPass.isEmpty()
                && Patterns.EMAIL_ADDRESS.matcher(uEmail).matches()
                && pass.matches(".{8,}")
                && (confirmPass.matches(pass))
                && uName.matches("[a-zA-Z ]+")
                && Utility.isValidPassword(pass)) {
            return true;
        }
        return false;

    }

}