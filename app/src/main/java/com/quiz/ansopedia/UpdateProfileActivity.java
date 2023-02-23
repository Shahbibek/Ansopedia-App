package com.quiz.ansopedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.quiz.ansopedia.Utility.Utility;

import java.io.IOException;

public class UpdateProfileActivity extends AppCompatActivity {

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;
    ImageView ivChangeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button btnUpdate;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        RelativeLayout svMain = findViewById(R.id.svMain);
        btnUpdate = findViewById(R.id.btnUpdate);
        ImageView ivBack = findViewById(R.id.ivBack);
        Button ivChangeBtn = findViewById(R.id.ivChangeBtn);
        ivChangeImg = findViewById(R.id.ivChangeImg);

        ivChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

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

    // Select Image method
    private void selectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                ivChangeImg.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

}