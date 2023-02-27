package com.quiz.ansopedia.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.quiz.ansopedia.R;
import com.quiz.ansopedia.SignInActivity;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.adapter.RankersAdapter;
import com.quiz.ansopedia.models.UserDetail;
import com.quiz.ansopedia.retrofit.ContentApiImplementer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderBoardFragment extends Fragment {

    RecyclerView rvLeaderBoard;
    ShapeableImageView ivRankerImage;
    TextView tvRankCount, tvRankerName, tvRankerScore;
    ArrayList<UserDetail> arrayList = new ArrayList<>();
    RankersAdapter adapter;
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
      ivRankerImage = view.findViewById(R.id.ivRankerImage);
      tvRankCount = view.findViewById(R.id.tvRankCount);
      tvRankerName = view.findViewById(R.id.tvRankerName);
      tvRankerScore = view.findViewById(R.id.tvRankerScore);
      getRankers();
      return view;
    }

    private void getRankers() {
        if (Utility.isNetConnected(getContext())) {
            Utility.showProgress(getContext());
            ContentApiImplementer.getRankers(new Callback<List<UserDetail>>() {
                @Override
                public void onResponse(Call<List<UserDetail>> call, Response<List<UserDetail>> response) {
                    Utility.dismissProgress(getContext());
                    if (response.code() == 200) {
                        arrayList = (ArrayList<UserDetail>) response.body();
                        setRecyclerView();
                    }else if(response.code() == 500) {
                        Utility.showAlertDialog(getContext(), "Failed", "Server Error, Please Try Again");
                    }else{
                        Utility.showAlertDialog(getContext(), "Error", "Something went wrong, Please Try Again");
                    }
                }

                @Override
                public void onFailure(Call<List<UserDetail>> call, Throwable t) {
                    Utility.dismissProgress(getContext());
                    t.printStackTrace();
                }
            });
        }
    }

    private void setRecyclerView() {
        adapter = new RankersAdapter(getContext(), arrayList);
        rvLeaderBoard.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLeaderBoard.setAdapter(adapter);
    }
}