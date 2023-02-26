package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.retrofit.ContentApiImplementer;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    TextView tvUserEmail;
    TextView tvUserName;
    CircleImageView ivChangeImage;
    TextView tvCoin;
    TextView tvEditSetting;
    TextView tvBadges;
    TextView tvLeadershipBoard;
    TextView tvCourses;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView tvEditProfile;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        Button btnLogoutButton = (Button)findViewById(R.id.btnLogoutButton);
        ImageView ivBack = findViewById(R.id.ivBack);
        ivChangeImage = findViewById(R.id.ivChangeImage);
        tvCoin = findViewById(R.id.tvCoin);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserName = findViewById(R.id.tvUserName);
        tvEditSetting = findViewById(R.id.tvEditSetting);
        tvBadges = findViewById(R.id.tvBadges);
        tvLeadershipBoard = findViewById(R.id.tvLeadershipBoard);
        tvCourses = findViewById(R.id.tvCourses);
        tvEditProfile = findViewById(R.id.tvEditProfile);
        if (Utility.userDetail == null) {
            Utility.getUserDetail(this);
        }
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
        try {
            tvUserName.setText(Utility.toCapitalizeFirstLetter(Utility.userDetail.getName()));
            tvUserEmail.setText(Utility.userDetail.getEmail());
            tvCoin.setText(String.valueOf(Utility.userDetail.getCoins()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvEditSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "Edit Setting Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        tvBadges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "Badges Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        tvLeadershipBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.putExtra(Constants.fragment, "LeaderBoardFragment");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        tvCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.putExtra(Constants.fragment, "QuizFragment");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        if (preferences.getBoolean(Constants.isImageAdded, false)) {
            try {
                GlideUrl url = new GlideUrl(ContentApiImplementer.BASE_URL + "user/avatar", new LazyHeaders.Builder()
                        .addHeader("Authorization", "Bearer " + Constants.TOKEN)
                        .build());

                Glide.with(this).load(url).into(ivChangeImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}