package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.quiz.ansopedia.Utility.Constants;
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
//    for ToolbarColor
//    View vt1, vt;
    RelativeLayout rlTopic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);
        initView();

        rlTopic.getBackground().setTint(Color.parseColor(Constants.COLOR));
//        vt.setBackgroundColor(Color.parseColor(Constants.COLOR));
//        vt1.getBackground().setTint(Color.parseColor(Constants.COLOR));
        subject = new Gson().fromJson(getIntent().getStringExtra("subject"), Subjects.class);
        tvSubject.setText(Utility.toCapitalizeFirstLetter(subject.getSubject_name()));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (subject.getChapters().size() != 0) {
            setRecyclerView();
        }
    }

    private void initView() {
        bnHome = findViewById(R.id.bnHome);
        rvChapter = findViewById(R.id.rvChapter);
        sbProgress = findViewById(R.id.sbProgress);
        tvSubject = findViewById(R.id.tvSubject);
        ivBack = findViewById(R.id.ivBack);
        rlTopic = findViewById(R.id.rlTopic);
//        vt = findViewById(R.id.vt);
//        vt1 = findViewById(R.id.vt1);
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