package com.quiz.ansopedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.models.LoginModel;
import com.quiz.ansopedia.models.LoginRequestModel;
import com.quiz.ansopedia.retrofit.ContentApiImplementer;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

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
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";

//    Firebase
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (preferences.getBoolean(Constants.isLogin, false)) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
        }
    }
    // [END on_start_check_user]

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
                String username = Objects.requireNonNull(etUsername.getText()).toString();
                String password = Objects.requireNonNull(etPassword.getText()).toString().trim();
                Utility.hideSoftKeyboard(SignInActivity.this);
                t1.setErrorEnabled(false);
                t2.setErrorEnabled(false);
                if (isValidateCredentials()) {
                    getLogin();
                } else {
                    if (username.isEmpty()) {
                        t1.setErrorEnabled(true);
                        t1.setError("* Please Enter Email");
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
                        t1.setErrorEnabled(true);
                        t1.setError("* Invalid Email");
                    }else if (password.isEmpty()){
                        t2.setErrorEnabled(true);
                        t2.setError("* Please Enter Password");
                    }else if(!password.matches( ".{8,}")){
                        t2.setErrorEnabled(true);
                        t2.setError("* Password must be of 8 digit");
                    }else if (!Utility.isValidPassword(password)){
                        t2.setErrorEnabled(true);
                        t2.setError("* Invalid Password");
                    };
                }
            }
        });
        textViewForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, ForgetPasswordActivity.class));
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

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END config_signin]

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
//        Toast.makeText(this, mAuth.getCurrentUser().getIdToken(), Toast.LENGTH_SHORT).show();

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.showProgress(SignInActivity.this);
                signIn();
            }
        });

    }
    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            user.getIdToken(true).addOnSuccessListener(result -> {
//                                String idToken = result.getToken();
                                //Do whatever
                                Constants.TOKEN = result.getToken();
                                Log.d(TAG, "GetTokenResult result = " + Constants.TOKEN);
                                Log.d(TAG, "GetTokenResult result = " + user);
                                ContentApiImplementer.signInWithGoogle(new Callback<List<LoginModel>>() {
                                    @Override
                                    public void onResponse(Call<List<LoginModel>> call, Response<List<LoginModel>> response) {
                                        if (response.code() == 200) {
                                            updateUI(user);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<LoginModel>> call, Throwable t) {
                                        updateUI(null);
                                    }
                                });

                            });
                            Toast.makeText(SignInActivity.this, ""+user, Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }
    // [END auth_with_google]
    // [END onactivityresult]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void updateUI(FirebaseUser user) {
        Utility.dismissProgress(this);
        if(user != null){
            preferences.edit().putBoolean(Constants.isLogin, true).apply();
            preferences.edit().putString(Constants.token, Constants.TOKEN ).apply();
    //                                Toast.makeText(SignInActivity.this, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "GetTokenResult result = " + Constants.TOKEN);
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
//            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
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
        loginRequestModel.setEmail(Objects.requireNonNull(etUsername.getText()).toString().trim());
        loginRequestModel.setPassword(Objects.requireNonNull(etPassword.getText()).toString().trim());

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
                        } else if(response.code() == 500){
                            Utility.showAlertDialog(SignInActivity.this, "Failed", "Server Error, Please Try Again..");
                        } else if(response.code() == 404){
                            Utility.showAlertDialog(SignInActivity.this, "Failed", "Account doesn't exist..!!");
                        } else if(response.code() == 403){
                            Utility.showAlertDialog(SignInActivity.this, "Failed", "You have not verified your email. Please Verify your email to login..");
                        } else if(response.code() == 429){
                            Utility.showAlertDialog(SignInActivity.this, "Failed", "Too many request, Please Try Again after 15 minutes..");
                        } else {
                            Utility.showAlertDialog(SignInActivity.this, "Error", "Something went wrong, Please Try Again..");
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
        String username = Objects.requireNonNull(etUsername.getText()).toString().trim();
        String password = Objects.requireNonNull(etPassword.getText()).toString().trim();
        return !username.isEmpty() && !password.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(username).matches() && password.matches(".{8,}") && Utility.isValidPassword(password);
    }
}