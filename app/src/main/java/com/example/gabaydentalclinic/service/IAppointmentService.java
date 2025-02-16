package com.example.gabaydentalclinic.service;
import com.example.gabaydentalclinic.model.APIResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IAppointmentService {
    @FormUrlEncoded
    @POST("create-appointment.php")
    Call<APIResponse<String>> createAppointment(
            @Field("client_id") String clientId,
            @Field("appointment_date") String appointmentDate,
            @Field("appointment_time") String appointmentTime,
            @Field("notes") String notes,
            @Field("serviceIds") String serviceIds // Send as comma-separated string
    );
}
