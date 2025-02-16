package com.example.gabaydentalclinic.service;

import com.example.gabaydentalclinic.model.APIResponse;
import com.example.gabaydentalclinic.model.DentalService;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IRetrieveService {
    @GET("retrieve-service.php")
    Call<APIResponse<DentalService>> retrieveService(); // Fixed method signature
}
