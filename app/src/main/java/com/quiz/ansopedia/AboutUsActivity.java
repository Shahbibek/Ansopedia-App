package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class AboutUsActivity extends AppCompatActivity {
    ShapeableImageView ivBibek;
    ShapeableImageView ivSikhat;
    ShapeableImageView ivSanjay;
    ShapeableImageView ivNayan;
    ImageView ivBibekFB, ivBibekLN, ivBibekGH;
    ImageView ivSanjayFB, ivSanjayLN, ivSanjayGH;
    ImageView ivNayanLN, ivNayanFB, ivNayanGH;
    ImageView ivShikatFB, ivShikatLN, ivShikatGH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        InitView();
        ivBibek = findViewById(R.id.ivBibek);
        ivSikhat = findViewById(R.id.ivSikhat);
        ivSanjay = findViewById(R.id.ivSanjay);
        ivNayan = findViewById(R.id.ivNayan);
        LoadImage();

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        ################################# Linked Tags start Bibek #############################

        ivBibekFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.facebook.com/bibek.sah.121"));
                startActivity(intent);
            }
        });
        ivBibekLN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.linkedin.com/in/bibek-sah81/"));
                startActivity(intent);
            }
        });
        ivBibekGH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://github.com/Shahbibek"));
                startActivity(intent);
            }
        });

//        ################################# Linked Tags end Bibek #############################

//        ################################# Linked Tags start Eftekar #############################
        ivShikatFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.facebook.com/eftakhar.mahmud.shikat"));
                startActivity(intent);
            }
        });
        ivShikatLN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://linkedin.com/in/eftakhar-mahmud"));
                startActivity(intent);
            }
        });
        ivShikatGH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://github.com/Shikat-Mahmud"));
                startActivity(intent);
            }
        });
//        ################################# Linked Tags end Eftekhar #############################

//        ################################# Linked Tags start Sanjay #############################
        ivSanjayFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.facebook.com/sanjaysknepali"));
                startActivity(intent);
            }
        });
        ivSanjayLN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.linkedin.com/in/sknepali/"));
                startActivity(intent);
            }
        });
        ivSanjayGH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://github.com/sknepali"));
                startActivity(intent);
            }
        });
//        ################################# Linked Tags end Sanjay #############################

//        ################################# Linked Tags start Nayan Daii #############################
        ivNayanFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.facebook.com/profile.php?id=100065911200545"));
                startActivity(intent);
            }
        });
        ivNayanLN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.linkedin.com/in/nayan-raj-shah-73499a208/"));
                startActivity(intent);
            }
        });
        ivNayanGH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://github.com/Nayan343"));
                startActivity(intent);
            }
        });
//        ################################# Linked Tags end nayan Daii #############################

    }

    private void InitView() {
        ivBibekFB = findViewById(R.id.ivBibekFB);
        ivBibekLN = findViewById(R.id.ivBibekLN);
        ivBibekGH = findViewById(R.id.ivBibekGH);
        ivShikatFB = findViewById(R.id.ivShikatFB);
        ivShikatGH = findViewById(R.id.ivShikatGH);
        ivShikatLN= findViewById(R.id.ivShikatLN);
        ivSanjayFB = findViewById(R.id.ivSanjayFB);
        ivSanjayLN = findViewById(R.id.ivSanjayLN);
        ivSanjayGH = findViewById(R.id.ivSanjayGH);
        ivNayanFB = findViewById(R.id.ivNayanFB);
        ivNayanLN = findViewById(R.id.ivNayanLN);
        ivNayanGH = findViewById(R.id.ivNayanGH);
    }

    private void LoadImage() {
        try{
            //        ######################## Load Image Start ###########################
            Glide.with(AboutUsActivity.this).load("https://res.cloudinary.com/ddhtmkllj/image/upload/v1677252637/samples/About%20Us/shikat_mjxapo.png").into(ivSikhat);
            Glide.with(AboutUsActivity.this).load("https://res.cloudinary.com/ddhtmkllj/image/upload/v1677251023/samples/About%20Us/bibek_sah_unbavx.jpg").into(ivBibek);
            Glide.with(AboutUsActivity.this).load("https://res.cloudinary.com/ddhtmkllj/image/upload/v1677526304/samples/About%20Us/sanjay_sah_new_yjwhnb.jpg").into(ivSanjay);
            Glide.with(AboutUsActivity.this).load("https://res.cloudinary.com/ddhtmkllj/image/upload/v1677509942/samples/About%20Us/nayan_sah_orh5q7.png").into(ivNayan);

            //        ######################## Load Image End ###########################
        }catch(Exception e){
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        };
    }
}