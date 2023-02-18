package com.quiz.ansopedia.Utility;

import com.quiz.ansopedia.models.LoginModel;
import com.quiz.ansopedia.models.LoginRequestModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("user/login")
    Call<List<LoginModel>> getLogin(@Body LoginRequestModel loginRequestModel);

    @POST("user/register")
    Call<List<LoginModel>> getRegister(@Body LoginRequestModel loginRequestModel);

    @POST("user/logout")
    Call<List<LoginModel>> getLogout();
}
