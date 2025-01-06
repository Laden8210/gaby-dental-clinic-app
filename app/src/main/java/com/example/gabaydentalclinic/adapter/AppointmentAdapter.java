package com.example.gabaydentalclinic.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gabaydentalclinic.R;
import com.example.gabaydentalclinic.view.AppointmentActivity;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private Context context;

    public AppointmentAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AppointmentAdapter.AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointments, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.AppointmentViewHolder holder, int position) {
        holder.ivAppointment.setOnClickListener(v -> {
            context.startActivity(new Intent(context, AppointmentActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivAppointment;
        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAppointment = itemView.findViewById(R.id.ivAction);
        }
    }
}
