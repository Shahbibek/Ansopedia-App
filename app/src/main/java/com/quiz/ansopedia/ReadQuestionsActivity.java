package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.adapter.ReadQuestionAdapter;
import com.quiz.ansopedia.models.Chapters;
import com.quiz.ansopedia.models.Questions;

import java.util.ArrayList;

public class ReadQuestionsActivity extends AppCompatActivity {
    BottomNavigationView bnHome;
    RecyclerView rvChapter;
    TextView tvSubject;
    ImageView ivBack;
    TextView tvChapter;
    Toolbar toolbar;
    Chapters chapters;
    ReadQuestionAdapter adapter;
    View vt, vt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_questions);
        initView();
//        #######################################   Set Color Start ######################################

        vt.setBackgroundColor(Color.parseColor(Constants.COLOR));
        vt1.getBackground().setTint(Color.parseColor(Constants.COLOR));
//        #######################################   Set Color Start ######################################
        chapters = new Gson().fromJson(getIntent().getStringExtra("chapter"), Chapters.class);
        tvSubject.setText(Utility.toCapitalizeFirstLetter(getIntent().getStringExtra("subject")));
        tvChapter.setText(Utility.toCapitalizeFirstLetter(chapters.getChapter_name()));
        if (chapters.getQuestions() != null) {
            setRecyclerView();
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        bnHome = findViewById(R.id.bnHome);
        rvChapter = findViewById(R.id.rvChapter);
        tvSubject = findViewById(R.id.tvSubject);
        ivBack = findViewById(R.id.ivBack);
        tvChapter = findViewById(R.id.tvChapter);
        toolbar = findViewById(R.id.toolbar);
        vt = findViewById(R.id.vt);
        vt1 = findViewById(R.id.vt1);

    }

    private void setRecyclerView() {
        adapter = new ReadQuestionAdapter(this, (ArrayList<Questions>) chapters.getQuestions());
        rvChapter.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvChapter.setLayoutManager(layoutManager);
        rvChapter.setAdapter(adapter);
        rvChapter.setItemViewCacheSize(10);
    }
}