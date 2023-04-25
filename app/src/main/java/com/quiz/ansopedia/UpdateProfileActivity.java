package com.quiz.ansopedia;

import static com.quiz.ansopedia.Utility.Constants.contents;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.quiz.ansopedia.Utility.Constants;
import com.quiz.ansopedia.Utility.Utility;
import com.quiz.ansopedia.api.ApiResponse;
import com.quiz.ansopedia.models.Contents;
import com.quiz.ansopedia.models.LoginModel;
import com.quiz.ansopedia.models.UserDetail;
import com.quiz.ansopedia.retrofit.ContentApiImplementer;

import org.json.JSONException;
import org.json.JSONObject;

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

    private static final int WRITE_STORAGE_REQUEST_CODE = 10;
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
    TextInputLayout t1, t2, t3;

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
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);

        try {
            if (!Utility.userDetail.getAvatar().substring(33).equalsIgnoreCase("undefined")) {

                Glide.with(this).load(Utility.userDetail.getAvatar()).into(ivChangeImg);
            }
            address1.setText(Utility.userDetail.getName());
            tvDesignation.setText(Utility.userDetail.getDesignation());
            tvPhoneNo.setText(Utility.userDetail.getMobile());
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }

        ivChangeBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
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
                String userName = address1.getText().toString().trim();
                String userPhone = tvPhoneNo.getText().toString().trim();
                String userDesignation = tvDesignation.getText().toString().trim();
                t1.setErrorEnabled(false);
                t2.setErrorEnabled(false);
                t3.setErrorEnabled(false);
                Utility.hideSoftKeyboard(UpdateProfileActivity.this);
                if (isValidateCredentials()) {
                    try{
                        updateUserDetail();
                        btnEdit.setVisibility(View.VISIBLE);
                        btnUpdate.setVisibility(View.GONE);
                    }catch (Exception e){
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                } else {
                    try{
                        if ((userName.isEmpty())) {
                            t1.setErrorEnabled(true);
                            t1.setError("* Please Enter Name");
                        }else if (!userName.matches("[a-zA-Z ]+")) {
                            t1.setErrorEnabled(true);
                            t1.setError("* Invalid Name");
                        }else if (userPhone.isEmpty()) {
                            t2.setErrorEnabled(true);
                            t2.setError("* Please Enter Phone Number");
                        }else if (!userPhone.matches(".{10,10}")) {
                            t2.setErrorEnabled(true);
                            t2.setError("* Mobile no must be 10 digit");
                        }else if (userDesignation.isEmpty()) {
                            t3.setErrorEnabled(true);
                            t3.setError("* Please Enter your designation");
                        }else if (!userDesignation.matches("[a-zA-Z ]+")) {
                            t3.setErrorEnabled(true);
                            t3.setError("* Invalid designation, please enter character only");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                }
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void selectImage() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Defining Implicit Intent to mobile gallery
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(
                    Intent.createChooser(
                            intent,
                            "Select Image from here..."),
                    PICK_IMAGE_REQUEST);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE_REQUEST_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_STORAGE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            }
        }
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
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                    byte[] imageInByte = stream.toByteArray();
                    long lengthbmp = imageInByte.length;
                    System.out.println(lengthbmp);
                    if (lengthbmp <= 1000000) {
                        uploadImage(part_image);
                    } else {
                        Utility.showAlertDialog(UpdateProfileActivity.this, "Failed", "File Size must be less than 1 MB !!");
                    }
                }
            } catch (Exception e) {
                // Log the exception
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        }
    }

    // Upload the image to the remote database
    public void uploadImage(String part_image) {
        try {
            Utility.showProgress(this);
            File imageFile = new File(part_image);
            System.out.println(imageFile);
            RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), imageFile);
            System.out.println(part_image.lastIndexOf("/") + 1);
            String name = part_image.substring(part_image.lastIndexOf("/") + 1);
            System.out.println(name);
            MultipartBody.Part partImage = MultipartBody.Part.createFormData("avatar", name, reqBody);

            ContentApiImplementer.uploadImage(partImage, new Callback<ApiResponse<LoginModel>>() {
                @Override
                public void onResponse(Call<ApiResponse<LoginModel>> call, Response<ApiResponse<LoginModel>> response) {

                    if (response.isSuccessful()) {
//                        LoginModel loginModel = (LoginModel) response.body().getData();
                        Utility.getUserDetailAwait(UpdateProfileActivity.this, new Utility.onResponseFromServer() {
                            @Override
                            public void iOnResponseFromServer(int responseCode, String msg) {
                                Utility.dismissProgress(UpdateProfileActivity.this);
                                Utility.showAlertDialog(UpdateProfileActivity.this, response.body().getStatus().toString().trim(), response.body().getMessage().toString().trim());
                            }
                        });
                    }
                    if (response.errorBody() != null) {
                        try {
                            Utility.dismissProgress(UpdateProfileActivity.this);
                            JSONObject Error = new JSONObject(response.errorBody().string());
                            Utility.showAlertDialog(UpdateProfileActivity.this, Error.getString("status").toString().trim(), Error.getString("message").toString().trim());
                        } catch (Exception e) {
                            e.printStackTrace();
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<LoginModel>> call, Throwable t) {
                    Utility.dismissProgress(UpdateProfileActivity.this);
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void updateUserDetail() {
        Utility.showProgress(this);
        UserDetail userDetail = new UserDetail();
        userDetail.setName(address1.getText().toString());
        userDetail.setDesignation(tvDesignation.getText().toString());
        userDetail.setMobile(tvPhoneNo.getText().toString());

        if (Utility.isNetConnected(this)) {
            try {
                ContentApiImplementer.updateUserDetail(userDetail, new Callback<ApiResponse<LoginModel>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<LoginModel>> call, Response<ApiResponse<LoginModel>> response) {
//                        Utility.dismissProgress(UpdateProfileActivity.this);
                        if (response.isSuccessful()) {
//                            LoginModel loginModel = (LoginModel) response.body().getData();
                            Utility.getUserDetailAwait(UpdateProfileActivity.this, new Utility.onResponseFromServer() {
                                @Override
                                public void iOnResponseFromServer(int responseCode, String msg) {
                                    Utility.dismissProgress(UpdateProfileActivity.this);
                                    Utility.showAlertDialog(UpdateProfileActivity.this, response.body().getStatus().toString().trim(), response.body().getMessage().toString().trim());
//                                        startActivity(new Intent(UpdateProfileActivity.this, MainActivity.class));
//                                        finish();
                                }
                            });
                        }
                        if (response.errorBody() != null) {
                            try {
                                JSONObject Error = new JSONObject(response.errorBody().string());
                                Utility.showAlertDialog(UpdateProfileActivity.this, Error.getString("status"), Error.getString("message"));
                            } catch (Exception e) {
                                Utility.dismissProgress(UpdateProfileActivity.this);
                                e.printStackTrace();
                                FirebaseCrashlytics.getInstance().recordException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<LoginModel>> call, Throwable t) {
                        Utility.dismissProgress(UpdateProfileActivity.this);
                        t.printStackTrace();
                    }
                });
            } catch (Exception e) {
                Utility.dismissProgress(UpdateProfileActivity.this);
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        } else {
            Utility.dismissProgress(UpdateProfileActivity.this);
            Utility.showAlertDialog(this, "Error", "Please Connect to Internet");
        }
    }

    private boolean isValidateCredentials() {
        String userName = address1.getText().toString();
        String userPhone = tvPhoneNo.getText().toString();
        String userDesignation = tvDesignation.getText().toString();

        if (!userName.isEmpty()
                && userName.matches("[a-zA-Z ]+")
                && !userPhone.isEmpty()
                && !userDesignation.isEmpty()
                && userPhone.matches(".{10,10}")
                && userDesignation.matches("[a-zA-Z ]+")) {
            return true;
        } else {
            return false;
        }
    }

}