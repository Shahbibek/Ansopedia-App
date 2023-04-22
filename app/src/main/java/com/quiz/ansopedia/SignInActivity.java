package com.quiz.ansopedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.JsonObject;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.api.ApiResponse;
import com.quiz.ansopedia.models.LoginModel;
import com.quiz.ansopedia.models.LoginRequestModel;
import com.quiz.ansopedia.retrofit.ContentApiImplementer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;
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
//    SignInButton sign_in_button;
    ImageView sign_in_button;
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
        try{
            if (preferences.getBoolean(Constants.isLogin, false)) {
//                Utility.showProgress(this);
                FirebaseUser currentUser = mAuth.getCurrentUser();
                currentUser.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                    @Override
                    public void onSuccess(GetTokenResult getTokenResult) {
                        Constants.TOKEN = getTokenResult.getToken();
                        updateUI(currentUser);
                    }
                });
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    // [END on_start_check_user]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        try{
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
                        try {
                            getLogin();
                        }catch (Exception e){
                            e.printStackTrace();
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    } else {
                        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                            t1.setErrorEnabled(true);
                            t1.setError("* Invalid Email");
                        }
                        if (username.isEmpty()) {
                            t1.setErrorEnabled(true);
                            t1.setError("* Please Enter Email");
                        }
                        if (!password.matches(".{8,}")) {
                            t2.setErrorEnabled(true);
                            t2.setError("* Password must be of 8 digit");
                        }
                        if (!Utility.isValidPassword(password)) {
                            t2.setErrorEnabled(true);
                            t2.setError("* Invalid Password");
                        }
                        if (password.isEmpty()) {
                            t2.setErrorEnabled(true);
                            t2.setError("* Please Enter Password");
                        }
                        ;
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
                    if (Utility.isNetConnected(SignInActivity.this)) {
                        try {
                            signIn();
                        } catch (Exception e) {
                            e.printStackTrace();
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }

                    } else {
                        Utility.dismissProgress(SignInActivity.this);
                        Utility.showAlertDialog(SignInActivity.this, "Failed", "Please, Connect to Internet !!..");
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }

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
                mAuth.fetchSignInMethodsForEmail(account.getEmail())
                                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                        if(task.isSuccessful()){
                                            SignInMethodQueryResult result = task.getResult();
                                            List<String> signinMethod = result.getSignInMethods();
//                                            Constants.Email= account.getEmail();
                                            System.out.println(signinMethod);
                                            if (signinMethod.size() == 0) {
//                                                Constants.Email= account.getEmail();
//                                                preferences.edit().putString(Constants.username,  Constants.email;
                                                firebaseAuthWithGoogle(account.getIdToken());
                                            } else if (signinMethod.contains("google.com")) {
//                                                Constants.Email= account.getEmail();
//                                                preferences.edit().putString(Constants.username,  Constants.email;
                                                firebaseAuthWithGoogle(account.getIdToken());
                                            } else if (signinMethod.contains("password")){
                                                mGoogleSignInClient.signOut()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Constants.TOKEN = "";
                                                                preferences.edit().putBoolean(Constants.isLogin, false).apply();
                                                                preferences.edit().putString(Constants.token, "").apply();
                                                                Utility.showAlertDialog(SignInActivity.this, "Error", "Your have signup by email and Password, Please try with that method !!..");
                                                                updateUI(null);
                                                            }
                                                        });
                                            } else {
                                                mGoogleSignInClient.signOut()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Constants.TOKEN = "";
                                                                preferences.edit().putBoolean(Constants.isLogin, false).apply();
                                                                preferences.edit().putString(Constants.token, "").apply();
                                                                Utility.showAlertDialog(SignInActivity.this, "Error", "Something went wrong, please try again !!..");
                                                                updateUI(null);
                                                            }
                                                        });
                                            }
                                        }else{
                                            mGoogleSignInClient.signOut()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Constants.TOKEN = "";
                                                            preferences.edit().putBoolean(Constants.isLogin, false).apply();
                                                            preferences.edit().putString(Constants.token, "").apply();
                                                            Utility.dismissProgress(SignInActivity.this);
                                                            Utility.showAlertDialog(SignInActivity.this, "Error", "Bad Request, Please try Again !!..");
                                                        }
                                                    });
                                        }
                                    }
                                });

            } catch (ApiException e) {
                Utility.dismissProgress(this);
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                e.printStackTrace();
            }
        } else {
            Utility.dismissProgress(this);
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
//                            Constants.Email = user.getEmail();
                            assert user != null;

                            user.getIdToken(true).addOnSuccessListener(result -> {
//                              String idToken = result.getToken();
//                              Do whatever
                                Constants.TOKEN = result.getToken();
//                                Log.d(TAG, "GetTokenResult result = " + Constants.TOKEN);
//                                Log.d(TAG, "GetTokenResult result = " + user);
                                ContentApiImplementer.signInWithGoogle(new Callback<ApiResponse<LoginModel>>() {
                                    @Override
                                    public void onResponse(Call<ApiResponse<LoginModel>> call, Response<ApiResponse<LoginModel>> response) {
                                        if (response.code() == 200) {
                                            preferences.getBoolean(Constants.isLogin, true);
                                            updateUI(user);
                                        } else {
                                            updateUI(null);
                                            Utility.showAlertDialog(SignInActivity.this, "Failed", "Something went wrong, please try again !!..");
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<ApiResponse<LoginModel>> call, Throwable t) {
                                        updateUI(null);
                                        Utility.dismissProgress(SignInActivity.this);
                                        Utility.showAlertDialog(SignInActivity.this, "Failed", "Something went wrong, Please try Again !!..");
                                    }
                                });

                            });
//                            Toast.makeText(SignInActivity.this, ""+user, Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                            Utility.dismissProgress(SignInActivity.this);
                            Utility.showAlertDialog(SignInActivity.this, "Failed", "Invalid Request, Please try Again !!..");
                        }
                    }
                });
    }
    // [END auth_with_google]
    // [END onactivityresult]

    // ############################# START Signin #############################
    private void signIn() {
        try{
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }catch (Exception e){
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }
    // ###################################### END Signin ##############################

    private void updateUI(FirebaseUser user) {
        if(user != null){
            try{
                Utility.getUserDetail(this);
            }catch(Exception e){
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }
            preferences.edit().putBoolean(Constants.isLogin, true).apply();
            preferences.edit().putString(Constants.token, Constants.TOKEN ).apply();
//            preferences.edit().putString(Constants.username,  Constants.Email).apply();
//              Toast.makeText(SignInActivity.this, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "GetTokenResult result = " + Constants.TOKEN);
            Utility.dismissProgress(this);
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
        } else{
            Utility.dismissProgress(this);
            Utility.showAlertDialog(SignInActivity.this, "Failed", "Invalid Request, Please try Again !!..");
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
                mAuth.fetchSignInMethodsForEmail(etUsername.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                if(task.isSuccessful()) {
                                    SignInMethodQueryResult result = task.getResult();
                                    List<String> signinMethod = result.getSignInMethods();
                                    System.out.println(signinMethod);
                                    if (signinMethod.size() == 0) {
                                        Utility.showAlertDialog(SignInActivity.this, "Failed", "Account doesn't exists, Please Signup !!..");
                                        Utility.dismissProgress(SignInActivity.this);
                                    }
                                     if (signinMethod.contains("google.com")) {
                                        Utility.showAlertDialog(SignInActivity.this, "Failed", "Account Created with Google SignIn, Please try with that method !!..");
                                        Utility.dismissProgress(SignInActivity.this);
                                    } else if (signinMethod.contains("password")){
                                        ContentApiImplementer.getLogin(loginRequestModel, new Callback<ApiResponse<LoginModel>>() {
                                            @Override
                                            public void onResponse(Call<ApiResponse<LoginModel>> call, Response<ApiResponse<LoginModel>> response) {

                                                if (response.isSuccessful()) {
                                                    Utility.dismissProgress(SignInActivity.this);
                                                    LoginModel loginModel = (LoginModel) response.body().getData();
                                                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                                                        try{
                                                            signInFirebaseWithPassword();
                                                            Utility.getUserDetail(SignInActivity.this);
                                                        }catch(Exception e){
                                                            e.printStackTrace();
                                                            FirebaseCrashlytics.getInstance().recordException(e);
                                                        }
                                                        preferences.edit().putBoolean(Constants.isLogin, true).apply();
                                                        preferences.edit().putString(Constants.token, Constants.TOKEN ).apply();
                                                        startActivity(new Intent(SignInActivity.this, MainActivity.class)
                                                                .putExtra("fragment","HomeFragment"));
                                                        finish();
                                                    } else {
                                                        Utility.dismissProgress(SignInActivity.this);
                                                        Utility.showAlertDialog(SignInActivity.this, response.body().getStatus().toString().trim(), response.body().getMessage().toString().trim());
                                                    }
                                                }
                                                if(response.errorBody() != null){
                                                    Utility.dismissProgress(SignInActivity.this);
                                                    try {
                                                        JSONObject Error = new JSONObject(response.errorBody().string());
                                                        Utility.showAlertDialog(SignInActivity.this, Error.getString("status").toString().trim() , Error.getString("message").toString().trim());
                                                    } catch (Exception e) {
                                                        Utility.dismissProgress(SignInActivity.this);
                                                        e.printStackTrace();
                                                        FirebaseCrashlytics.getInstance().recordException(e);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ApiResponse<LoginModel>> call, Throwable t) {
                                                Utility.dismissProgress(SignInActivity.this);
                                                t.printStackTrace();
                                                Utility.showAlertDialog(SignInActivity.this, "Error", "Something went wrong, Please Try Again");
                                            }
                                        });
                                    }
                                }else{
                                    Utility.showAlertDialog(SignInActivity.this, "Error", "Bad Request, Please try Again !!..");
                                }
                            }
                        });
            } catch (Exception e) {
                Utility.dismissProgress(this);
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        } else {
            Utility.dismissProgress(this);
            Utility.showAlertDialog(this, "Error", "Please, Connect to Internet !!..");
        }
    }

    public void signInFirebaseWithPassword() {
        mAuth.signInWithEmailAndPassword(etUsername.getText().toString().trim(),etPassword.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                                @Override
                                public void onSuccess(GetTokenResult getTokenResult) {
                                    Constants.TOKEN = getTokenResult.getToken();
//                                    updateUI(user);
                                    preferences.edit().putBoolean(Constants.isLogin, true).apply();
                                    preferences.edit().putString(Constants.token, Constants.TOKEN ).apply();
                                }
                            });
                        } else{
                            Utility.dismissProgress(SignInActivity.this);
//                            Utility.showAlertDialog(SignInActivity.this, "Error", "Bad Request, Please try Again !!..");
                        }
                    }
                });
    }

    private boolean isValidateCredentials() {
        String username = Objects.requireNonNull(etUsername.getText()).toString().trim();
        String password = Objects.requireNonNull(etPassword.getText()).toString().trim();
        return !username.isEmpty() && !password.isEmpty()
                && Patterns.EMAIL_ADDRESS.matcher(username).matches()
                && password.matches(".{8,}")
                && Utility.isValidPassword(password);
    }
}