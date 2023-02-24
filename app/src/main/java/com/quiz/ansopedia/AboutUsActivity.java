package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

public class AboutUsActivity extends AppCompatActivity {
    ShapeableImageView ivBibek;
    ShapeableImageView ivSikhat;
    ShapeableImageView ivSanjay;
    ShapeableImageView ivNayan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        ivBibek = findViewById(R.id.ivBibek);
        ivSikhat = findViewById(R.id.ivSikhat);
        ivSanjay = findViewById(R.id.ivSanjay);
        ivNayan = findViewById(R.id.ivNayan);
//        ######################## Load Image Start ###########################
        Glide.with(AboutUsActivity.this).load("https://res.cloudinary.com/ddhtmkllj/image/upload/v1677252637/samples/About%20Us/shikat_mjxapo.png").into(ivSikhat);
        Glide.with(AboutUsActivity.this).load("https://res.cloudinary.com/ddhtmkllj/image/upload/v1677251023/samples/About%20Us/bibek_sah_unbavx.jpg").into(ivBibek);
        Glide.with(AboutUsActivity.this).load("https://res.cloudinary.com/ddhtmkllj/image/upload/v1677251023/samples/About%20Us/bibek_sah_unbavx.jpg").into(ivSanjay);
        Glide.with(AboutUsActivity.this).load("https://res.cloudinary.com/ddhtmkllj/image/upload/v1677251023/samples/About%20Us/bibek_sah_unbavx.jpg").into(ivNayan);

//        ######################## Load Image End ###########################

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}