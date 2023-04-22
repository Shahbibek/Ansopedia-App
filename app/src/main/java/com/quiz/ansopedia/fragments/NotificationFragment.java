package com.quiz.ansopedia.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.quiz.ansopedia.R;
import com.quiz.ansopedia.SignInActivity;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.adapter.ChapterAdapter;
import com.quiz.ansopedia.adapter.CourseAdapter;
import com.quiz.ansopedia.adapter.NotificationAdapter;
import com.quiz.ansopedia.api.ApiResponse;
import com.quiz.ansopedia.models.Chapters;
import com.quiz.ansopedia.models.Notification;
import com.quiz.ansopedia.models.Subjects;
import com.quiz.ansopedia.retrofit.ContentApiImplementer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationFragment extends Fragment {

    RecyclerView rvNotification;
    ArrayList<Notification> notifications;
    NotificationAdapter adapter;
    TextView tvNoData;

    public NotificationFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notification, container, false);
        rvNotification = view.findViewById(R.id.rvNotification);
        tvNoData = view.findViewById(R.id.tvNoData);
        getNotification();
        return view;
    }

    public void getNotification() {
        Utility.showProgress(getContext());
        if (Utility.isNetConnected(getContext())) {
            try {
                ContentApiImplementer.getNotification(new Callback<ApiResponse<List<Notification>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<Notification>>> call, Response<ApiResponse<List<Notification>>> response) {
                        Utility.dismissProgress(getContext());
                        if (response.isSuccessful()) {
                            notifications = (ArrayList<Notification>) response.body().getData();
//                            Collections.reverse(notifications);
                            setRecyclerView();
                        }
                        if(response.errorBody() != null){
                            try {
                                JSONObject Error = new JSONObject(response.errorBody().string());
                                Utility.showAlertDialog(getContext(), Error.getString("status") , Error.getString("message"));
                            } catch (Exception e) {
                                Utility.dismissProgress(getContext());
                                e.printStackTrace();
                                FirebaseCrashlytics.getInstance().recordException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<Notification>>> call, Throwable t) {
                        Utility.dismissProgress(getContext());
//                        t.printStackTrace();
                    }
                });
//                ContentApiImplementer.getNotification(new Callback<List<Notification>>() {
//                    @Override
//                    public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
//                        Utility.dismissProgress(getContext());
//                        if (response.code() == 200) {
//                            notifications = (ArrayList<Notification>) response.body();
////                            Collections.reverse(notifications);
//                            setRecyclerView();
//                        }else if(response.code() == 404){
//                            Utility.showAlertDialog(getContext(), "Failed", "Nothing to show");
//                        }else if(response.code() == 500){
//                            Utility.showAlertDialog(getContext(), "Failed", "Server error, Please Try Again");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<Notification>> call, Throwable t) {
//                        Utility.dismissProgress(getContext());
//                        t.printStackTrace();
//                    }
//                });
            } catch (Exception e) {
                Utility.dismissProgress(getContext());
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        } else {
            Utility.dismissProgress(getContext());
            Utility.showAlertDialog(getContext(), "Error", "Please Connect to Internet..!!");
        }
    }

    public void setRecyclerView() {
        if(notifications.isEmpty()){
            tvNoData.setVisibility(View.VISIBLE);
        }else{
            adapter = new NotificationAdapter(getContext(), notifications);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rvNotification.setLayoutManager(layoutManager);
            rvNotification.setAdapter(adapter);
            rvNotification.setHasFixedSize(true);
        }

    }
}