package com.quiz.ansopedia.retrofit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.quiz.ansopedia.R;
import com.quiz.ansopedia.Utility.ApiInterceptor;
import com.quiz.ansopedia.Utility.ApiService;
import com.quiz.ansopedia.models.Contents;
import com.quiz.ansopedia.models.LoginModel;
import com.quiz.ansopedia.models.LoginRequestModel;
import com.quiz.ansopedia.models.Notification;
import com.quiz.ansopedia.models.UserDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContentApiImplementer {
    private static Retrofit retrofit;
    public static String BASE_URL = "https://api.ansopedia.com/api/";
//    private static String BASE_URL = "http://192.168.185.148:8000/api/";

    private static Retrofit getRetrofit() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(new ApiInterceptor())
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static void getLogin(LoginRequestModel loginRequestModel, Callback<List<LoginModel>> cb) {
        ApiService apiService = getRetrofit().create(ApiService.class);
        Call<List<LoginModel>> call = apiService.getLogin(loginRequestModel);
        call.enqueue(cb);
    }

    public static void getRegister(LoginRequestModel loginRequestModel, Callback<List<LoginModel>> cb) {
        ApiService apiService = getRetrofit().create(ApiService.class);
        Call<List<LoginModel>> call = apiService.getRegister(loginRequestModel);
        call.enqueue(cb);
    }

    public static void getLogout(Callback<List<LoginModel>> cb) {
        ApiService apiService = getRetrofit().create(ApiService.class);
        Call<List<LoginModel>> call = apiService.getLogout();
        call.enqueue(cb);
    }

    public static void getContent(Callback<List<Contents>> cb) {
        ApiService apiService = getRetrofit().create(ApiService.class);
        Call<List<Contents>> call = apiService.getContent();
        call.enqueue(cb);
    }

    public static void sendEmailResetPassword(LoginRequestModel loginRequestModel, Callback<List<LoginModel>> cb) {
        ApiService apiService = getRetrofit().create(ApiService.class);
        Call<List<LoginModel>> call = apiService.sendEmailResetPassword(loginRequestModel);
        call.enqueue(cb);
    }

    public static void sendOTPResetPassword(LoginRequestModel loginRequestModel, Callback<List<LoginModel>> cb) {
        ApiService apiService = getRetrofit().create(ApiService.class);
        Call<List<LoginModel>> call = apiService.sendOTPResetPassword(loginRequestModel);
        call.enqueue(cb);
    }

    public static void sendNewPasswordResetPassword(LoginRequestModel loginRequestModel, Callback<List<LoginModel>> cb) {
        ApiService apiService = getRetrofit().create(ApiService.class);
        Call<List<LoginModel>> call = apiService.sendNewPasswordResetPassword(loginRequestModel);
        call.enqueue(cb);
    }

    public static void sendContactMessage(LoginRequestModel loginRequestModel, Callback<List<LoginModel>> cb) {
        ApiService apiService = getRetrofit().create(ApiService.class);
        Call<List<LoginModel>> call = apiService.sendContactMessage(loginRequestModel);
        call.enqueue(cb);
    }

    public static void uploadImage(MultipartBody.Part image, Callback<List<LoginModel>> cb) {
        ApiService apiService = getRetrofit().create(ApiService.class);
        Call<List<LoginModel>> call = apiService.uploadImage(image);
        call.enqueue(cb);
    }

    public static void getUserDetail(Callback<List<UserDetail>> cb) {
        ApiService apiService = getRetrofit().create(ApiService.class);
        Call<List<UserDetail>> call = apiService.getUserDetail();
        call.enqueue(cb);
    }

    public static void updateUserDetail(UserDetail userDetail, Callback<List<LoginModel>> cb) {
        ApiService apiService = getRetrofit().create(ApiService.class);
        Call<List<LoginModel>> call = apiService.updateUserDetail(userDetail);
        call.enqueue(cb);
    }

    public static void getNotification(Callback<List<Notification>> cb) {
        ApiService apiService = getRetrofit().create(ApiService.class);
        Call<List<Notification>> call = apiService.getNotification();
        call.enqueue(cb);
    }

    public static void getRankers(Callback<List<UserDetail>> cb) {
        ApiService apiService = getRetrofit().create(ApiService.class);
        Call<List<UserDetail>> call = apiService.getRankers();
        call.enqueue(cb);
    }
}
