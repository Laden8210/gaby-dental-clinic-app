package com.example.gabaydentalclinic.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gabaydentalclinic.R;
import com.example.gabaydentalclinic.adapter.AppointmentAdapter;
import com.example.gabaydentalclinic.api.PostCallback;
import com.example.gabaydentalclinic.api.PostTask;
import com.example.gabaydentalclinic.model.APIResponse;
import com.example.gabaydentalclinic.model.RetrieveAppointment;
import com.example.gabaydentalclinic.util.SessionManager;
import com.example.gabaydentalclinic.view.CreateAppointmentActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class AppointmentsFragment extends Fragment implements PostCallback {

    private RecyclerView rvAppointments;
    private ExtendedFloatingActionButton fabAddAppointment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointments, container, false);
        rvAppointments = view.findViewById(R.id.rvAppointments);

        rvAppointments.setLayoutManager(new LinearLayoutManager(getContext()));

        fabAddAppointment = view.findViewById(R.id.fabAddAppointment);
        fabAddAppointment.setOnClickListener(v -> {
           getContext().startActivity(new Intent(getContext(), CreateAppointmentActivity.class));
        });

        fetchData();
        return view;
    }

    private void fetchData() {
        try {
            JSONObject postData = new JSONObject();
            postData.put("client_id", SessionManager.getInstance(getContext()).getUserId());
            new PostTask(getContext(), this, "Loading appointments...", "retrieve-appointment.php").execute(postData);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostSuccess(String responseData) {

        Gson gson = new Gson();

        APIResponse<RetrieveAppointment> apiResponse = gson.fromJson(responseData, new TypeToken<APIResponse<RetrieveAppointment>>(){}.getType());

        rvAppointments.setAdapter(new AppointmentAdapter(getContext(), apiResponse.getData()));


    }

    @Override
    public void onPostError(String errorMessage) {

    }
}