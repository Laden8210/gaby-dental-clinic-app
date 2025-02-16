package com.example.gabaydentalclinic.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gabaydentalclinic.R;
import com.example.gabaydentalclinic.model.ResponseModel;
import com.example.gabaydentalclinic.repository.CreateUserRepository;
import com.example.gabaydentalclinic.service.ICreateUserService;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RegisterActivity extends AppCompatActivity {

    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;
    private boolean isProfileImage = true;

    private TextInputLayout tFirstName, tLastName, tAge, tSex, tMobileNumber, tAddress, tOccupation, tEmail, tPassword, tConfirmPassword;
    private Button btnRegister, btnUploadProfileImage, btnUploadIDImage;
    private ImageView ivProfileImage, ivIDImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize UI elements
        tFirstName = findViewById(R.id.tFirstName);
        tLastName = findViewById(R.id.tLastName);
        tAge = findViewById(R.id.tAge);
        tSex = findViewById(R.id.tSex);
        tMobileNumber = findViewById(R.id.tMobileNumber);
        tAddress = findViewById(R.id.tAddress);
        tEmail = findViewById(R.id.tEmail);
        tPassword = findViewById(R.id.tPassword);
        tConfirmPassword = findViewById(R.id.tConfirmPassword);
        tOccupation = findViewById(R.id.tOccupation);

        btnRegister = findViewById(R.id.btnRegister);
        btnUploadProfileImage = findViewById(R.id.btnUploadProfilePicture);
        btnUploadIDImage = findViewById(R.id.btnUploadIdImage);
        ivProfileImage = findViewById(R.id.ivProfilePicturePreview);
        ivIDImage = findViewById(R.id.ivIdImagePreview);

        btnUploadProfileImage.setOnClickListener(view -> {
            isProfileImage = true;
            showImagePickerDialog();
        });

        btnUploadIDImage.setOnClickListener(view -> {
            isProfileImage = false;
            showImagePickerDialog();
        });

        btnRegister.setOnClickListener(this::registerAction);
    }

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image")
                .setItems(new String[]{"Take Photo", "Choose from Gallery"}, (dialogInterface, i) -> {
                    if (i == 0) {
                        openCamera();
                    } else {
                        openGallery();
                    }
                })
                .show();
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_GALLERY) {
                Uri selectedImageUri = data.getData();
                setImage(selectedImageUri);
            } else if (requestCode == REQUEST_CAMERA && data.getExtras() != null) {
                Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
                setImage(capturedImage);
            }
        }
    }

    private void setImage(Object image) {
        if (image instanceof Uri) {
            if (isProfileImage) {
                ivProfileImage.setImageURI((Uri) image);
            } else {
                ivIDImage.setImageURI((Uri) image);
            }
        } else if (image instanceof Bitmap) {
            if (isProfileImage) {
                ivProfileImage.setImageBitmap((Bitmap) image);
            } else {
                ivIDImage.setImageBitmap((Bitmap) image);
            }
        }
    }

    private void registerAction(View view) {
        String firstName = tFirstName.getEditText().getText().toString().trim();
        String lastName = tLastName.getEditText().getText().toString().trim();
        String age = tAge.getEditText().getText().toString().trim();
        String sex = tSex.getEditText().getText().toString().trim();
        String mobileNumber = tMobileNumber.getEditText().getText().toString().trim();
        String address = tAddress.getEditText().getText().toString().trim();
        String email = tEmail.getEditText().getText().toString().trim();
        String password = tPassword.getEditText().getText().toString().trim();
        String confirmPassword = tConfirmPassword.getEditText().getText().toString().trim();
        String occupation = tOccupation.getEditText().getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || age.isEmpty() || sex.isEmpty() ||
                mobileNumber.isEmpty() || address.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty() || occupation.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        ICreateUserService apiService = CreateUserRepository.getApiService();

        RequestBody firstNameBody = RequestBody.create(okhttp3.MediaType.parse("text/plain"), firstName);
        RequestBody lastNameBody = RequestBody.create(okhttp3.MediaType.parse("text/plain"), lastName);
        RequestBody ageBody = RequestBody.create(okhttp3.MediaType.parse("text/plain"), age);
        RequestBody sexBody = RequestBody.create(okhttp3.MediaType.parse("text/plain"), sex);
        RequestBody mobileNumberBody = RequestBody.create(okhttp3.MediaType.parse("text/plain"), mobileNumber);
        RequestBody addressBody = RequestBody.create(okhttp3.MediaType.parse("text/plain"), address);
        RequestBody emailBody = RequestBody.create(okhttp3.MediaType.parse("text/plain"), email);
        RequestBody passwordBody = RequestBody.create(okhttp3.MediaType.parse("text/plain"), password);
        RequestBody occupationBody = RequestBody.create(okhttp3.MediaType.parse("text/plain"), occupation);

        MultipartBody.Part profileImagePart = null;
        MultipartBody.Part idImagePart = null;

        if (ivProfileImage.getDrawable() != null) {
            profileImagePart = getImagePart("profileImage", ivProfileImage);
        }
        if (ivIDImage.getDrawable() != null) {
            idImagePart = getImagePart("idImage", ivIDImage);
        }

        Call<ResponseModel> call = apiService.registerUser(
                firstNameBody, lastNameBody, ageBody, sexBody,
                mobileNumberBody, addressBody, emailBody, passwordBody, occupationBody,
                profileImagePart, idImagePart
        );

        call.enqueue(new retrofit2.Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, retrofit2.Response<ResponseModel> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("RegisterActivity", "Raw Response: " + new Gson().toJson(response.body()));
                        Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                        if (response.body().getStatus().equalsIgnoreCase("success")) {
                            finish();
                        }
                    } else {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "null";
                        Log.e("RegisterActivity", "Error Response: " + errorBody);
                        Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("RegisterActivity", "onFailure: " + t.getMessage(),t);
            }
        });
    }

    private MultipartBody.Part getImagePart(String name, ImageView imageView) {
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] data = bos.toByteArray();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), data);
        return MultipartBody.Part.createFormData(name, name + ".jpg", requestBody);
    }

}
