package com.example.gabaydentalclinic.api;

public interface PostCallback {
    void onPostSuccess(String responseData);

    void onPostError(String errorMessage);
}
