package com.example.gabaydentalclinic.model;



import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("user_id")
    private String userId;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return userId;
    }
}
