package com.quiz.ansopedia.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.quiz.ansopedia.R;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.adapter.DatabaseSubjectAdapter;
import com.quiz.ansopedia.models.Subjects;
import com.quiz.ansopedia.sqlite.CoursesHelper;

import java.util.ArrayList;

public class QuizFragment extends Fragment {

    RecyclerView rvCourses;

    public QuizFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_quiz, container, false);
        rvCourses = view.findViewById(R.id.rvCourses);
        CoursesHelper db = new CoursesHelper(getContext());
        Constants.subjectsArrayList = db.readData(getContext());
        setRecyclerView();
        return view;
    }

    private void setRecyclerView() {
        DatabaseSubjectAdapter adapter = new DatabaseSubjectAdapter(getContext(), Constants.subjectsArrayList);
        rvCourses.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCourses.setAdapter(adapter);
    }
}