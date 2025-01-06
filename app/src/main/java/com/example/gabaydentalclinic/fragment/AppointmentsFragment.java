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
import com.example.gabaydentalclinic.view.CreateAppointmentActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;


public class AppointmentsFragment extends Fragment {

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

        rvAppointments.setAdapter(new AppointmentAdapter(getContext()));
        rvAppointments.setLayoutManager(new LinearLayoutManager(getContext()));

        fabAddAppointment = view.findViewById(R.id.fabAddAppointment);
        fabAddAppointment.setOnClickListener(v -> {
           getContext().startActivity(new Intent(getContext(), CreateAppointmentActivity.class));
        });
        return view;
    }
}