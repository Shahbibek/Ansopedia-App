package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.adapter.ChapterAdapter;
import com.quiz.ansopedia.models.Chapters;
import com.quiz.ansopedia.models.Subjects;
import com.quiz.ansopedia.sqlite.CoursesHelper;

import java.util.ArrayList;

public class ChaptersActivity extends AppCompatActivity {
    BottomNavigationView bnHome;
    RecyclerView rvChapter;
    SeekBar sbProgress;
    TextView tvSubject;
    ImageView ivBack;
    ImageView ivSaveCourse;
    Subjects subject;
    ChapterAdapter adapter;
//    for ToolbarColor
//    View vt1, vt;
    RelativeLayout rlTopic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);
        try {
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
            for (Subjects s :
                    Constants.subjectsArrayList) {
                if (s.getSubject_name().equalsIgnoreCase(subject.getSubject_name())) {
                    ivSaveCourse.setVisibility(View.GONE);
                }
            }
            ivSaveCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveSubject();
                }
            });
        } catch (Exception e){
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void initView() {
        bnHome = findViewById(R.id.bnHome);
        rvChapter = findViewById(R.id.rvChapter);
        sbProgress = findViewById(R.id.sbProgress);
        tvSubject = findViewById(R.id.tvSubject);
        ivBack = findViewById(R.id.ivBack);
        rlTopic = findViewById(R.id.rlTopic);
        ivSaveCourse = findViewById(R.id.ivSaveCourse);
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

    private void saveSubject(){
        CoursesHelper db = new CoursesHelper(this);
        db.addNewData(subject.getSubject_name() ,getIntent().getStringExtra("subject"));
        Constants.subjectsArrayList = new ArrayList<>();
        Constants.subjectsArrayList = db.readData(this);
        for (Subjects s :
                Constants.subjectsArrayList) {
            if (s.getSubject_name().equalsIgnoreCase(subject.getSubject_name())) {
//                Utility.showAlertDialog(this, "Success", subject.getSubject_name() + "is added to local Database");
                new AlertDialog.Builder(this)
                        .setTitle("Success")
                        .setMessage(subject.getSubject_name() +" is added successfully to courses !!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                ivSaveCourse.setVisibility(View.GONE);
//                                Intent intent = new Intent(ChaptersActivity.this,
//                                        ChaptersActivity.class);
//                                    intent.putExtra("subject", getIntent().getStringExtra("subject"));
//                                startActivity(intent);
//                                finish();
                            }
                        })
                        .create().show();
            }
        }
    }
}