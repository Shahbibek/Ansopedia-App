package com.quiz.ansopedia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.android.material.textfield.TextInputEditText;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.models.LoginModel;
import com.quiz.ansopedia.models.UserDetail;
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
    TextInputEditText tvDesignation;
    TextInputEditText tvPhoneNo;
    TextInputEditText address1;
    SharedPreferences preferences;
    Button btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button btnUpdate;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        RelativeLayout svMain = findViewById(R.id.svMain);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnEdit = findViewById(R.id.btnEdit);
        ImageView ivBack = findViewById(R.id.ivBack);
        Button ivChangeBtn = findViewById(R.id.ivChangeBtn);
        ivChangeImg = findViewById(R.id.ivChangeImg);
        address1 = findViewById(R.id.address1);
        tvPhoneNo = findViewById(R.id.tvPhoneNo);
        tvDesignation = findViewById(R.id.tvDesignation);

        if (preferences.getBoolean(Constants.isImageAdded, false)) {
            try {
                GlideUrl url = new GlideUrl(ContentApiImplementer.BASE_URL + "user/avatar", new LazyHeaders.Builder()
                        .addHeader("Authorization", "Bearer " + Constants.TOKEN)
                        .build());

                Glide.with(this).load(url).into(ivChangeImg);
                address1.setText(Utility.userDetail.getName());
                tvDesignation.setText(Utility.userDetail.getDesignation());
                tvPhoneNo.setText(Utility.userDetail.getMobile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
                updateUserDetail();
                btnUpdate.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
            }
        });
//        ###################### Edit and Save Open and Shut Start ################################
        tvPhoneNo.setEnabled(false);
        tvDesignation.setEnabled(false);
        address1.setEnabled(false);
        btnUpdate.setVisibility(View.GONE);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvPhoneNo.setEnabled(true);
                tvDesignation.setEnabled(true);
                address1.setEnabled(true);
                btnUpdate.setVisibility(View.VISIBLE);
                btnEdit.setVisibility(View.GONE);
            }
        });

//        ###################### Edit and Save Open and Shut End ################################

        svMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideSoftKeyboard(UpdateProfileActivity.this);
            }
        });

    }

    // Select Image method
    private void selectImage() {

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
                                    Intent data) {

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
                if (cursor != null) {
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
            } catch (IOException e) {
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
        System.out.println(part_image.lastIndexOf("/") + 1);
        String name = part_image.substring(part_image.lastIndexOf("/") + 1);
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
                } else if (response.code() == 500) {
                    Utility.showAlertDialog(UpdateProfileActivity.this, "Failed", "Server Error, Please Try Again");
                } else if (response.code() == 400) {
                    Utility.showAlertDialog(UpdateProfileActivity.this, "Failed", "File not found, Please Try Again..");
                } else {
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

    private void updateUserDetail() {
        Utility.showProgress(this);
        UserDetail userDetail = new UserDetail();
        userDetail.setName(address1.getText().toString());
        userDetail.setDesignation(tvDesignation.getText().toString());
        userDetail.setMobile(tvPhoneNo.getText().toString());

        if (Utility.isNetConnected(this)) {
            try {
                ContentApiImplementer.updateUserDetail(userDetail, new Callback<List<LoginModel>>() {
                    @Override
                    public void onResponse(Call<List<LoginModel>> call, Response<List<LoginModel>> response) {
                        if (response.code() == 200) {
                            LoginModel loginModel = (LoginModel) response.body().get(0);
                            if (loginModel.getStatus().equalsIgnoreCase("success")) {
                                Utility.showAlertDialog(UpdateProfileActivity.this, loginModel.getStatus(), loginModel.getMessage());
                            }
                        }else if(response.code() == 304){
                            LoginModel loginModel = (LoginModel) response.body().get(0);
                            Utility.showAlertDialog(UpdateProfileActivity.this, loginModel.getStatus(), loginModel.getMessage());
                        }else if(response.code() == 422){
                            LoginModel loginModel = (LoginModel) response.body().get(0);
                            Utility.showAlertDialog(UpdateProfileActivity.this, loginModel.getStatus(), loginModel.getMessage());
                        }else if(response.code() == 500){
                            LoginModel loginModel = (LoginModel) response.body().get(0);
                            Utility.showAlertDialog(UpdateProfileActivity.this, loginModel.getStatus(), loginModel.getMessage());
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Utility.showAlertDialog(this, "Error", "Please Connect to Internet");
        }
    }

}