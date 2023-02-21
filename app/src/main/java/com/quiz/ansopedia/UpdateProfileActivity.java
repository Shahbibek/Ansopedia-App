package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.quiz.ansopedia.Utility.Utility;

public class UpdateProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button btnUpdate;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        RelativeLayout svMain = findViewById(R.id.svMain);
        btnUpdate = findViewById(R.id.btnUpdate);
        ImageView ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(UpdateProfileActivity.this);
                Toast.makeText(UpdateProfileActivity.this, "Update button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        svMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {Utility.hideSoftKeyboard(UpdateProfileActivity.this);}
        });

    }

}