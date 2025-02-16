package com.example.gabaydentalclinic.api;

public interface GetCallback {

    void onGetSuccess(String responseData);

    void onGetError(String errorMessage);
}
