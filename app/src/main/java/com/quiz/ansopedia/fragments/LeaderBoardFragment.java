package com.quiz.ansopedia.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quiz.ansopedia.R;

public class LeaderBoardFragment extends Fragment {

    RecyclerView rvLeaderBoard;
    public LeaderBoardFragment() {
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
      View view = inflater.inflate(R.layout.fragment_leader_board, container, false);
      rvLeaderBoard = view.findViewById(R.id.rvLeaderBoard);
      return view;
    }
}