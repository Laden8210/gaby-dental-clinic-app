package com.example.gabaydentalclinic.service;


import com.example.gabaydentalclinic.model.ResponseModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ICreateUserService {
    @Multipart
    @POST("create-user.php") // Adjust to match your API endpoint
    Call<ResponseModel> registerUser(
            @Part("first_name") RequestBody firstName,
            @Part("last_name") RequestBody lastName,
            @Part("age") RequestBody age,
            @Part("sex") RequestBody sex,
            @Part("mobile_number") RequestBody mobileNumber,
            @Part("address") RequestBody address,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("occupation") RequestBody occupation,
            @Part MultipartBody.Part profileImage,
            @Part MultipartBody.Part idImage
    );
}
