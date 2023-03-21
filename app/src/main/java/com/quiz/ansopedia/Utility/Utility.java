package com.quiz.ansopedia.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.quiz.ansopedia.MainActivity;
import com.quiz.ansopedia.R;
import com.quiz.ansopedia.SignInActivity;
import com.quiz.ansopedia.models.LoginModel;
import com.quiz.ansopedia.models.LoginRequestModel;
import com.quiz.ansopedia.models.UserDetail;
import com.quiz.ansopedia.retrofit.ContentApiImplementer;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utility {
    private static ProgressDialog progressDialog;

    public static void showProgress(Context context) {
        progressDialog = new ProgressDialog(context);
        if (!progressDialog.isShowing()) {
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public static void dismissProgress(Context context) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static boolean isNetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static void showAlertDialog(Context context, String title, String msg) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void getLogout(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        new AlertDialog.Builder(context)
                .setTitle("Alert")
                .setMessage("Do you want to Logout")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        showProgress(context);
                        try {
                            ContentApiImplementer.getLogout(new Callback<List<LoginModel>>() {
                                @Override
                                public void onResponse(Call<List<LoginModel>> call, Response<List<LoginModel>> response) {
                                    dismissProgress(context);
                                    if (response.code() == 200) {
                                        Constants.TOKEN = "";
                                        preferences.edit().putBoolean(Constants.isLogin, false).apply();
                                        preferences.edit().putString(Constants.token, "").apply();
                                        context.startActivity(new Intent(context, SignInActivity.class));
                                        ((Activity) context).finish();
                                    }else if(response.code() == 500){
                                        showAlertDialog(context, "Failed", "Server Error, Please Try Again");
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<LoginModel>> call, Throwable t) {
                                    dismissProgress(context);
                                    showAlertDialog(context, "Error", "Something went wrong, Please try again");
                                }
                            });
                        } catch (Exception e) {
                            dismissProgress(context);
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }

    public static String toCapitalizeFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static void getLogin(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        LoginRequestModel loginRequestModel = new LoginRequestModel();
        loginRequestModel.setEmail(preferences.getString(Constants.username, ""));
        loginRequestModel.setPassword(preferences.getString(Constants.password, ""));

        if (Utility.isNetConnected(context)) {
            try {
                ContentApiImplementer.getLogin(loginRequestModel, new Callback<List<LoginModel>>() {
                    @Override
                    public void onResponse(Call<List<LoginModel>> call, Response<List<LoginModel>> response) {
                        if (response.code() == 200) {
                            LoginModel loginModel = (LoginModel) response.body().get(0);
                            if (loginModel.getStatus().equalsIgnoreCase("success")) {
                                Constants.TOKEN = loginModel.getToken();
                                preferences.edit().putBoolean(Constants.isLogin, true).apply();
                                preferences.edit().putString(Constants.token, loginModel.getToken()).apply();
                                getUserDetail(context);
                            } else {
                                Constants.TOKEN = "";
                                preferences.edit().putBoolean(Constants.isLogin, false).apply();
                                preferences.edit().putString(Constants.token, "").apply();
                                context.startActivity(new Intent(context, SignInActivity.class));
                                ((Activity) context).finish();
                            }
                        } else {
//                            Utility.showAlertDialog(context, "Error", "Something went wrong, Please Try Again");
                            Constants.TOKEN = "";
                            preferences.edit().putBoolean(Constants.isLogin, false).apply();
                            preferences.edit().putString(Constants.token, "").apply();
                            context.startActivity(new Intent(context, SignInActivity.class));
                            ((Activity) context).finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<LoginModel>> call, Throwable t) {
                        Constants.TOKEN = "";
                        preferences.edit().putBoolean(Constants.isLogin, false).apply();
                        preferences.edit().putString(Constants.token, "").apply();
                        t.printStackTrace();
                        Utility.showAlertDialog(context, "Error", "Something went wrong, Please Try Again");
                    }
                });
            } catch (Exception e) {
                Constants.TOKEN = "";
                preferences.edit().putBoolean(Constants.isLogin, false).apply();
                preferences.edit().putString(Constants.token, "").apply();
                e.printStackTrace();
            }
        } else {
            Utility.showAlertDialog(context, "Error", "Please Connect to Internet");
        }
    }
    public static AlertDialog dialog;
    public static void showProgressGif(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.my_dialog);
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.progress_image_dialog, null);
        ImageView imageView = view.findViewById(R.id.ivProgress);
        Glide.with(context).load(R.drawable.ansopedia_loader_new).into(imageView);
        builder.setView(view);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public static void dismissProgressGif() {
        dialog.dismiss();
    }
    public static UserDetail userDetail;
    public static void getUserDetail(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        ContentApiImplementer.getUserDetail(new Callback<List<UserDetail>>() {
            @Override
            public void onResponse(Call<List<UserDetail>> call, Response<List<UserDetail>> response) {
                if (response.code() == 200) {
                    userDetail = new UserDetail();
                    userDetail = response.body().get(0);
                    preferences.edit().putString(Constants.name, userDetail.getName()).apply();
                }
            }

            @Override
            public void onFailure(Call<List<UserDetail>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    public static void getUserDetailAwait(Context context, final onResponseFromServer responseFromServer) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        ContentApiImplementer.getUserDetail(new Callback<List<UserDetail>>() {
            @Override
            public void onResponse(Call<List<UserDetail>> call, Response<List<UserDetail>> response) {
                if (response.code() == 200) {
                    userDetail = new UserDetail();
                    userDetail = response.body().get(0);
                    preferences.edit().putString(Constants.name, userDetail.getName()).apply();
                    responseFromServer.iOnResponseFromServer(response.code(), "Success");
                } else {
                    responseFromServer.iOnResponseFromServer(response.code(), "fail");
                }
            }

            @Override
            public void onFailure(Call<List<UserDetail>> call, Throwable t) {
                t.printStackTrace();
                responseFromServer.iOnResponseFromServer(500, "Server error");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    public static String calculateDate(Duration duration) {
        String diff = "";
        try {
            if (duration.toMinutes() < 60) {
                diff = duration.toMinutes() + " minutes ago";
            } else if (duration.toHours() < 24) {
                diff = duration.toHours() + " hours ago";
            } else if (duration.toHours() >= 24 && duration.toHours() < 720) {
                diff = duration.toHours()/24 + " days ago";
            }else if (duration.toHours() >= 720 && duration.toHours() < 8760) {
                diff = duration.toHours()/720 + " month ago";
            } else if (duration.toHours() >= 8760) {
                diff = duration.toHours() / 8760 + " years ago";
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return diff;

    }

    public static boolean
    isValidPassword(String password)
    {

        // Regex to check valid password.
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the password is empty
        // return false
        if (password == null) {
            return false;
        }

        Matcher m = p.matcher(password);

        // Return if the password
        // matched the ReGex
        return m.matches();
    }
    public interface onResponseFromServer{
        void iOnResponseFromServer(int responseCode, String msg);
    }
}
