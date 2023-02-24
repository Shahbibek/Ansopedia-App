package com.quiz.ansopedia;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.models.LoginModel;
import com.quiz.ansopedia.retrofit.ContentApiImplementer;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @RequiresApi(api = Build.VERSION_CODES.O)
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
                String part_image;
                String[] imageProjection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(filePath, imageProjection, null, null, null);
                if(cursor != null) {
                    cursor.moveToFirst();
                    int indexImage = cursor.getColumnIndex(imageProjection[0]);
                    part_image = cursor.getString(indexImage);
                    System.out.println(part_image);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] imageInByte = stream.toByteArray();
                    long lengthbmp = imageInByte.length;
                    System.out.println(lengthbmp);
                    if (lengthbmp <= 1000000) {
                        uploadImage(part_image);
                    } else {
                        Utility.showAlertDialog(UpdateProfileActivity.this, "Failed", "File Size must be less than 1000 KB !!");
                    }
                }
            }
            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // Upload the image to the remote database
    public void uploadImage(String part_image) {
        Utility.showProgress(this);
        File imageFile = new File(part_image);
        System.out.println(imageFile);
        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), imageFile);
        System.out.println(part_image.lastIndexOf("/")+1);
        String name = part_image.substring(part_image.lastIndexOf("/")+1);
        System.out.println(name);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("avatar", name, reqBody);

        ContentApiImplementer.uploadImage(partImage, new Callback<List<LoginModel>>() {
            @Override
            public void onResponse(Call<List<LoginModel>> call, Response<List<LoginModel>> response) {
                Utility.dismissProgress(UpdateProfileActivity.this);
                if (response.code() == 200) {
                    LoginModel loginModel = (LoginModel) response.body().get(0);
                    if (loginModel.getStatus().equalsIgnoreCase("success")) {
                        Utility.showAlertDialog(UpdateProfileActivity.this, loginModel.getStatus(), loginModel.getMessage());
                    }
                } else if(response.code() == 500) {
                    Utility.showAlertDialog(UpdateProfileActivity.this, "Failed", "Server Error, Please Try Again");
                }else if(response.code() == 400){
                    Utility.showAlertDialog(UpdateProfileActivity.this, "Failed", "File not found, Please Try Again..");
                }else{
                    Utility.showAlertDialog(UpdateProfileActivity.this, "Failed", "Something went wrong, Please Try Again");
                }
            }

            @Override
            public void onFailure(Call<List<LoginModel>> call, Throwable t) {
                Utility.dismissProgress(UpdateProfileActivity.this);
                t.printStackTrace();
            }
        });
    }

}