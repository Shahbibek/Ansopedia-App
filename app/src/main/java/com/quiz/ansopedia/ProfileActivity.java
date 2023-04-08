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
import com.google.firebase.auth.FirebaseAuth;
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
    ImageView ivBack;
    Button btnLogoutButton;
    TextView tvToolbar;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView tvEditProfile;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        initView();
        tvEditProfile = findViewById(R.id.tvEditProfile);
        try{
            if (Utility.userDetail == null) {
                Utility.getUserDetail(this);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        btnLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(ProfileActivity.this, SignInActivity.class));
//                finish();
                Utility.getLogout(ProfileActivity.this);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
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

//        tvEditSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(ProfileActivity.this, "Edit Setting Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });

//        tvBadges.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(ProfileActivity.this, BadgesActivity.class));
//                finish();
//            }
//        });

        tvLeadershipBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.putExtra(Constants.fragment, "LeaderBoardFragment");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        tvCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.putExtra(Constants.fragment, "QuizFragment");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        try {
            if (!Utility.userDetail.getAvatar().substring(33).equalsIgnoreCase("undefined")) {
                try {
                    Glide.with(this)
                            .load(Utility.userDetail.getAvatar())
                            .into(ivChangeImage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        btnLogoutButton = findViewById(R.id.btnLogoutButton);
        ivBack = findViewById(R.id.ivBack);
        ivChangeImage = findViewById(R.id.ivChangeImage);
        tvCoin = findViewById(R.id.tvCoin);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserName = findViewById(R.id.tvUserName);
        tvEditSetting = findViewById(R.id.tvEditSetting);
        tvBadges = findViewById(R.id.tvBadges);
        tvLeadershipBoard = findViewById(R.id.tvLeadershipBoard);
        tvCourses = findViewById(R.id.tvCourses);
        tvToolbar = findViewById(R.id.tvToolbar);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        finish();
    }

}