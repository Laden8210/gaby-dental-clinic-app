package com.example.gabaydentalclinic.service;

import com.example.gabaydentalclinic.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ILoginUserService {
    @FormUrlEncoded
    @POST("login-user.php")
    Call<LoginResponse> loginUser(
            @Field("emailOrPhone") String emailOrPhone,
            @Field("password") String password
    );
}
