package com.example.gabaydentalclinic.repository;

import com.example.gabaydentalclinic.config.ApiAddress;
import com.example.gabaydentalclinic.service.ICreateUserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateUserRepository {

    private static Retrofit retrofit = null;

    public static ICreateUserService getApiService() {
        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiAddress.API_ADDRESS)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(ICreateUserService.class);
    }
}
