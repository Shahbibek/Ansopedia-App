package com.quiz.ansopedia.Utility;

import com.quiz.ansopedia.models.Contents;
import com.quiz.ansopedia.models.LoginModel;
import com.quiz.ansopedia.models.LoginRequestModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("user/login")
    Call<List<LoginModel>> getLogin(@Body LoginRequestModel loginRequestModel);

    @POST("user/register")
    Call<List<LoginModel>> getRegister(@Body LoginRequestModel loginRequestModel);

    @POST("user/logout")
    Call<List<LoginModel>> getLogout();

    @GET("contents")
    Call<List<Contents>> getContent();

    @POST("user/send-reset-password-email")
    Call<List<LoginModel>> sendEmailResetPassword(@Body LoginRequestModel loginRequestModel);

    @POST("user/verify-otp")
    Call<List<LoginModel>> sendOTPResetPassword(@Body LoginRequestModel loginRequestModel);

    @POST("user/changepassword")
    Call<List<LoginModel>> sendNewPasswordResetPassword(@Body LoginRequestModel loginRequestModel);

    @POST("contact")
    Call<List<LoginModel>> sendContactMessage(@Body LoginRequestModel loginRequestModel);
}
