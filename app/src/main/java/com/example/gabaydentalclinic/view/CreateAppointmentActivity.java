package com.example.gabaydentalclinic.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gabaydentalclinic.R;
import com.example.gabaydentalclinic.model.APIResponse;
import com.example.gabaydentalclinic.model.CreateAppointment;
import com.example.gabaydentalclinic.model.DentalService;
import com.example.gabaydentalclinic.repository.ApiClient;
import com.example.gabaydentalclinic.service.IAppointmentService;
import com.example.gabaydentalclinic.service.IRetrieveService;
import com.example.gabaydentalclinic.util.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateAppointmentActivity extends AppCompatActivity {
    private LinearLayout llServiceList;
    private Button btnCreateAppointment;
    private TextInputEditText etAppointmentDate, etAppointmentTime, etNote;
    private List<CheckBox> checkBoxList = new ArrayList<>();
    private static final String TAG = "CreateAppointmentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);

        llServiceList = findViewById(R.id.llServiceList);
        btnCreateAppointment = findViewById(R.id.btnCreateAppointment);
        etAppointmentDate = findViewById(R.id.tietAppointmentDate);
        etAppointmentTime = findViewById(R.id.tietAppointmentTime);
        etNote = findViewById(R.id.tietNote);

        etAppointmentDate.setOnClickListener(v -> showDatePickerDialog());

        etAppointmentTime.setOnClickListener(v -> showTimePicker());

        // Fetch services from API
        fetchServices();

        // Handle appointment submission
        btnCreateAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAppointment();
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                CreateAppointmentActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String birthdate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    etAppointmentDate.setText(birthdate);
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void showTimePicker(){
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                CreateAppointmentActivity.this,
                (view, selectedHour, selectedMinute) -> {
                    String time = selectedHour + ":" + selectedMinute;
                    etAppointmentTime.setText(time);
                },
                hour, minute, true);

        timePickerDialog.show();
    }


    private void fetchServices() {
        Retrofit retrofit = ApiClient.getClient();
        IRetrieveService apiService = retrofit.create(IRetrieveService.class);

        Call<APIResponse<DentalService>> call = apiService.retrieveService();
        call.enqueue(new Callback<APIResponse<DentalService>>() {
            @Override
            public void onResponse(Call<APIResponse<DentalService>> call, Response<APIResponse<DentalService>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DentalService> services = response.body().getData();
                    displayData(services);
                } else {
                    Toast.makeText(CreateAppointmentActivity.this, "Failed to load services", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse<DentalService>> call, Throwable t) {
                Log.e(TAG, "API Call Failed: " + t.getMessage());
                Toast.makeText(CreateAppointmentActivity.this, "Error fetching services", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayData(List<DentalService> services) {
        llServiceList.removeAllViews();
        checkBoxList.clear();

        for (DentalService service : services) {
            CheckBox cbService = new CheckBox(this);
            cbService.setText(service.getName() + " - ₱" + service.getPrice());
            cbService.setTag(service.getId());
            llServiceList.addView(cbService);
            checkBoxList.add(cbService);
        }
    }

    private void submitAppointment() {
        String dateStr = etAppointmentDate.getText().toString().trim();
        String timeStr = etAppointmentTime.getText().toString().trim();
        String noteStr = etNote.getText().toString().trim();

        if (dateStr.isEmpty() || timeStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected services and convert to a comma-separated string
        List<String> selectedServices = new ArrayList<>();
        for (CheckBox cb : checkBoxList) {
            if (cb.isChecked()) {
                selectedServices.add(cb.getTag().toString());
            }
        }

        if (selectedServices.isEmpty()) {
            Toast.makeText(this, "Please select at least one service", Toast.LENGTH_SHORT).show();
            return;
        }

        String servicesString = TextUtils.join(",", selectedServices); // Convert list to CSV

        // Get user ID
        String userId = SessionManager.getInstance(this).getUserId();

        // Send data to API
        sendAppointmentToApi(userId, dateStr, timeStr, noteStr, servicesString);
    }

    private void sendAppointmentToApi(String clientId, String date, String time, String notes, String serviceIds) {
        Retrofit retrofit = ApiClient.getClient(); // Ensure this is properly configured
        IAppointmentService service = retrofit.create(IAppointmentService.class);

        Call<APIResponse<String>> call = service.createAppointment(clientId, date, time, notes, serviceIds);
        call.enqueue(new Callback<APIResponse<String>>() {
            @Override
            public void onResponse(Call<APIResponse<String>> call, Response<APIResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    APIResponse<String> apiResponse = response.body();
                    Log.d("CreateAppointment", "Response: " + new Gson().toJson(apiResponse));

                    if ("success".equalsIgnoreCase(apiResponse.getStatus())) {
                        Toast.makeText(CreateAppointmentActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CreateAppointmentActivity.this, "Failed: " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("CreateAppointment", "Server Error: " + response.errorBody());
                    Toast.makeText(CreateAppointmentActivity.this, "Server Error: Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse<String>> call, Throwable t) {
                Log.e("CreateAppointment", "Request Failed: " + t.getMessage());
                Toast.makeText(CreateAppointmentActivity.this, "Request failed. Please check your internet.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
