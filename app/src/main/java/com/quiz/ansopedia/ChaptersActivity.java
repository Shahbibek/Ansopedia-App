package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.adapter.ChapterAdapter;
import com.quiz.ansopedia.models.Chapters;
import com.quiz.ansopedia.models.Subjects;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ChaptersActivity extends AppCompatActivity {
    BottomNavigationView bnHome;
    RecyclerView rvChapter;
    SeekBar sbProgress;
    TextView tvSubject;
    ImageView ivBack;
    Subjects subject;
    ChapterAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);
        initView();
        subject = new Gson().fromJson(getIntent().getStringExtra("subject"), Subjects.class);
        tvSubject.setText(Utility.toCapitalizeFirstLetter(subject.getSubject_name()));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setRecyclerView();
    }

    private void initView() {
        bnHome = findViewById(R.id.bnHome);
        rvChapter = findViewById(R.id.rvChapter);
        sbProgress = findViewById(R.id.sbProgress);
        tvSubject = findViewById(R.id.tvSubject);
        ivBack = findViewById(R.id.ivBack);
    }

    private void setRecyclerView() {
        rvChapter.setHasFixedSize(true);
        rvChapter.setItemViewCacheSize(10);
        adapter = new ChapterAdapter(this,(ArrayList<Chapters>) subject.getChapters(), subject.getSubject_name());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvChapter.setAdapter(adapter);
        rvChapter.setLayoutManager(layoutManager);
    }
}