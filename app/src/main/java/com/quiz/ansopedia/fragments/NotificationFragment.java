package com.quiz.ansopedia.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quiz.ansopedia.R;
import com.quiz.ansopedia.SignInActivity;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.adapter.ChapterAdapter;
import com.quiz.ansopedia.adapter.CourseAdapter;
import com.quiz.ansopedia.adapter.NotificationAdapter;
import com.quiz.ansopedia.models.Chapters;
import com.quiz.ansopedia.models.Notification;
import com.quiz.ansopedia.models.Subjects;
import com.quiz.ansopedia.retrofit.ContentApiImplementer;

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
        getNotification();
        return view;
    }

    public void getNotification() {
        Utility.showProgress(getContext());
        if (Utility.isNetConnected(getContext())) {
            try {
                ContentApiImplementer.getNotification(new Callback<List<Notification>>() {
                    @Override
                    public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                        Utility.dismissProgress(getContext());
                        if (response.code() == 200) {
                            notifications = (ArrayList<Notification>) response.body();
//                            Collections.reverse(notifications);
                            setRecyclerView();
                        }else if(response.code() == 404){
                            Utility.showAlertDialog(getContext(), "Failed", "Nothing to show");
                        }else if(response.code() == 500){
                            Utility.showAlertDialog(getContext(), "Failed", "Server error, Please Try Again");
                        }else{
                            Utility.showAlertDialog(getContext(), "Error", "Something went wrong, Please Try Again");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Notification>> call, Throwable t) {
                        Utility.dismissProgress(getContext());
                        t.printStackTrace();
                    }
                });
            } catch (Exception e) {
                Utility.dismissProgress(getContext());
                e.printStackTrace();
            }
        } else {
            Utility.dismissProgress(getContext());
            Utility.showAlertDialog(getContext(), "Error", "Please Connect to Internet");
        }
    }

    public void setRecyclerView() {
        adapter = new NotificationAdapter(getContext(), notifications);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvNotification.setLayoutManager(layoutManager);
        rvNotification.setAdapter(adapter);
        rvNotification.setHasFixedSize(true);
    }
}