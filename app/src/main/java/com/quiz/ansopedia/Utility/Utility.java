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
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.quiz.ansopedia.MainActivity;
import com.quiz.ansopedia.R;
import com.quiz.ansopedia.SignInActivity;
import com.quiz.ansopedia.models.LoginModel;
import com.quiz.ansopedia.models.LoginRequestModel;
import com.quiz.ansopedia.retrofit.ContentApiImplementer;

import org.json.JSONObject;

import java.util.List;

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
                                    } else {
                                        showAlertDialog(context, "Error", "Something went wrong, Please try again");
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<LoginModel>> call, Throwable t) {
                                    dismissProgress(context);
                                    showAlertDialog(context, "Error", "Something went wrong, Please try again");
                                }
                            });
                        } catch (Exception e) {
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
                        Utility.dismissProgress(context);
                        if (response.code() == 200) {
                            LoginModel loginModel = (LoginModel) response.body().get(0);
                            if (loginModel.getStatus().equalsIgnoreCase("success")) {
                                Constants.TOKEN = loginModel.getToken();
                                preferences.edit().putBoolean(Constants.isLogin, true).apply();
                                preferences.edit().putString(Constants.token, loginModel.getToken()).apply();
                            } else {
                                Utility.showAlertDialog(context, loginModel.getStatus(), loginModel.getMessage());
                            }
                        } else {
                            Utility.showAlertDialog(context, "Error", "Something went wrong, Please Try Again");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<LoginModel>> call, Throwable t) {
                        Utility.dismissProgress(context);
                        t.printStackTrace();
                        Utility.showAlertDialog(context, "Error", "Something went wrong, Please Try Again");
                    }
                });
            } catch (Exception e) {
                Utility.dismissProgress(context);
                e.printStackTrace();
            }
        } else {
            Utility.dismissProgress(context);
            Utility.showAlertDialog(context, "Error", "Please Connect to Internet");
        }
    }
}
