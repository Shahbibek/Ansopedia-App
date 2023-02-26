package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.adapter.ReadQuestionAdapter1;
import com.quiz.ansopedia.models.Chapters;
import com.quiz.ansopedia.models.Questions;

import java.util.ArrayList;

public class ReadQuestionsActivity extends AppCompatActivity {
    BottomNavigationView bnHome;
    static RecyclerView rvChapter;
    TextView tvSubject;
    ImageView ivBack;
    TextView tvChapter;
    Toolbar toolbar;
    Chapters chapters;
    private static ReadQuestionAdapter1 adapter1;
//    View vt, vt1;
    ImageView nextquestion;
    ImageView previousquestion;
    RelativeLayout rlTopic;
    public static ArrayList<Questions> questions = new ArrayList<>();

    private static ReadQuestionsActivity readQuestionsActivity = new ReadQuestionsActivity();
    public static int lower = 0, upper = 10;
    public static int diff = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_questions);
        initView();
        readQuestionsActivity = this;
//        #######################################   Set Color Start ######################################

//        rlTopic.setBackgroundColor(Color.parseColor(Constants.COLOR));
//        vt.setBackgroundColor(Color.parseColor(Constants.COLOR));
        rlTopic.getBackground().setTint(Color.parseColor(Constants.COLOR));
//        nextquestion.setBackgroundColor(Color.parseColor(Constants.COLOR));
//        previousquestion.getBackground().setTint(Color.parseColor(Constants.COLOR));
//        #######################################   Set Color Start ######################################
        chapters = new Gson().fromJson(getIntent().getStringExtra("chapter"), Chapters.class);
        tvSubject.setText(Utility.toCapitalizeFirstLetter(getIntent().getStringExtra("subject")));
        tvChapter.setText(Utility.toCapitalizeFirstLetter(chapters.getChapter_name()));
        questions = (ArrayList<Questions>) chapters.getQuestions();
        if (chapters.getQuestions() != null) {
            setRecyclerView(lower,upper);
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                lower = 0;
                upper = 10;
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
        rlTopic = findViewById(R.id.rlTopic);
        nextquestion = findViewById(R.id.nextquestion);
        previousquestion = findViewById(R.id.previousquestion);
//        vt = findViewById(R.id.vt);
//        vt1 = findViewById(R.id.vt1);
    }

    public static void setRecyclerView( int i1, int i2) {
        adapter1 = new ReadQuestionAdapter1(readQuestionsActivity, getArrayList(i1,i2));
        rvChapter.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(readQuestionsActivity, LinearLayoutManager.VERTICAL, false);
        rvChapter.setLayoutManager(layoutManager);
        rvChapter.setAdapter(adapter1);
        rvChapter.setItemViewCacheSize(15);
    }

    public static ArrayList<Questions> getArrayList(int i1, int i2){
        ArrayList<Questions> tempArrayList = new ArrayList<>();
        for (int i = i1; i < i2; i++) {
            tempArrayList.add(questions.get(i));
        }
        return tempArrayList;
    }
}