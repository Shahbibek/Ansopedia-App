package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.quiz.ansopedia.Utility.Utility;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView tvEditProfile;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Button btnLogoutButton = (Button)findViewById(R.id.btnLogoutButton);
        ImageView ivBack = findViewById(R.id.ivBack);
        tvEditProfile = findViewById(R.id.tvEditProfile);

        btnLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.getLogout(ProfileActivity.this);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, UpdateProfileActivity.class));
            }
        });
    }

}