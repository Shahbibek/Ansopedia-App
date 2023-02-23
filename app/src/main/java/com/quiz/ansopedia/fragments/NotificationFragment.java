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
import com.quiz.ansopedia.adapter.ChapterAdapter;
import com.quiz.ansopedia.adapter.CourseAdapter;
import com.quiz.ansopedia.models.Chapters;
import com.quiz.ansopedia.models.Subjects;

import java.util.ArrayList;


public class NotificationFragment extends Fragment {

    RecyclerView rvNotification;

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
        return view;
    }

}