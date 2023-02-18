package com.quiz.ansopedia.Utility;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Log.d("TAG", "intercept: " + String.valueOf(request.url()));
        Request newRequest = request.newBuilder()
                .header("Authorization", "Bearer "+Constants.TOKEN)
                .build();
        return chain.proceed(newRequest);
    }
}
