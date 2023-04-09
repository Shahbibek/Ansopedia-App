package com.quiz.ansopedia.Utility;

import com.quiz.ansopedia.api.ApiResponse;
import com.quiz.ansopedia.models.Contents;
import com.quiz.ansopedia.models.LoginModel;
import com.quiz.ansopedia.models.LoginRequestModel;
import com.quiz.ansopedia.models.Notification;
import com.quiz.ansopedia.models.UserDetail;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface ApiService {
    @POST("user/login")
//    Call<List<LoginModel>> getLogin(@Body LoginRequestModel loginRequestModel);
    Call<ApiResponse<LoginModel>> getLogin(@Body LoginRequestModel loginRequestModel);

    @POST("user/register")
//    Call<List<LoginModel>> getRegister(@Body LoginRequestModel loginRequestModel);
    Call<ApiResponse<LoginModel>> getRegister(@Body LoginRequestModel loginRequestModel);

    @POST("user/logout")
    Call<List<LoginModel>> getLogout();

    @GET("contents")
//    Call<List<Contents>> getContent();
    Call<ApiResponse<List<Contents>>> getContent();

    @POST("user/send-reset-password-email")
//    Call<List<LoginModel>> sendEmailResetPassword(@Body LoginRequestModel loginRequestModel);
    Call<ApiResponse<LoginModel>> sendEmailResetPassword(@Body LoginRequestModel loginRequestModel);

    @POST("user/verify-otp")
//    Call<List<LoginModel>> sendOTPResetPassword(@Body LoginRequestModel loginRequestModel);
    Call<ApiResponse<LoginModel>> sendOTPResetPassword(@Body LoginRequestModel loginRequestModel);

    @POST("user/reset-password")
    Call<ApiResponse<LoginModel>> sendNewPasswordResetPassword(@Body LoginRequestModel loginRequestModel);

    @POST("contact")
//    Call<List<LoginModel>> sendContactMessage(@Body LoginRequestModel loginRequestModel);
    Call<ApiResponse<LoginModel>> sendContactMessage(@Body LoginRequestModel loginRequestModel);

    @Multipart
    @PUT("user/avatar")
//    Call<List<LoginModel>> uploadImage(@Part MultipartBody.Part image);
    Call<ApiResponse<LoginModel>> uploadImage(@Part MultipartBody.Part image);

    @GET("user")
//    Call<List<UserDetail>> getUserDetail();
    Call<ApiResponse<UserDetail>> getUserDetail();

    @PUT("user")
//    Call<List<LoginModel>> updateUserDetail(@Body UserDetail userDetail);
    Call<ApiResponse<LoginModel>> updateUserDetail(@Body UserDetail userDetail);

    @GET("notify")
//    Call<List<Notification>> getNotification();
    Call<ApiResponse<List<Notification>>> getNotification();

    @GET("leaders")
//    Call<List<UserDetail>> getRankers();
    Call<ApiResponse<List<UserDetail>>> getRankers();

    @GET("user/sign-in-with-google")
    Call<ApiResponse<LoginModel>> signInWithGoogle();
}
