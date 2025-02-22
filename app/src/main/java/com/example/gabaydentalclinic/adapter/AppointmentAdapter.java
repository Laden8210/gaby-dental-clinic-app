package com.example.gabaydentalclinic.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gabaydentalclinic.R;
import com.example.gabaydentalclinic.model.AppointmentService;
import com.example.gabaydentalclinic.model.RetrieveAppointment;
import com.example.gabaydentalclinic.view.AppointmentActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.List;


public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private Context context;

    private List<RetrieveAppointment> appointments = new ArrayList<>();

    public AppointmentAdapter(Context context, List<RetrieveAppointment> appointments) {
        this.context = context;
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public AppointmentAdapter.AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointments, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.AppointmentViewHolder holder, int position) {

        RetrieveAppointment appointment = appointments.get(position);


        List<AppointmentService> services = appointment.getServices();

        makeServiceList(holder, services);

        holder.ivAppointment.setOnClickListener(v -> {

            Intent intent = new Intent(context, AppointmentActivity.class);

            intent.putExtra("appointment", appointment);

            context.startActivity(intent);
        });

        holder.tvTotalPayment.setText("Total Payment: ₱" + getTotalPayment(services));

        holder.tvStatus.setText(getStatus(appointment.getStatus()));
        String formattedDateTime = formatDateTime(appointment.getAppointmentDate(), appointment.getAppointmentTime());
        holder.tvDateTime.setText(formattedDateTime);
        holder.tvNote.setText(appointment.getNotes());
    }


    private String formatDateTime(String date, String time) {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date parsedDate = dateFormat.parse(date);

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
            Date parsedTime = timeFormat.parse(time);

            SimpleDateFormat finalDateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            String formattedDate = finalDateFormat.format(parsedDate);

            SimpleDateFormat finalTimeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
            String formattedTime = finalTimeFormat.format(parsedTime);

            return formattedDate + " - " + formattedTime;
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid Date";
        }
    }

    private String getStatus(int status) {
        switch (status) {
            case 0:
                return "Pending";
            case 1:
                return "Confirmed";
            case 2:
                return "Completed";
            case -1:
                return "Cancelled";

            default:
                return "Unknown";
        }
    }

    private String getTotalPayment(List<AppointmentService> services) {
        double total = 0;
        for (AppointmentService service : services) {
            total += Double.valueOf(service.getAmount() != null ? service.getAmount() : "0");
        }
        return String.valueOf(total);
    }

    private void makeServiceList(AppointmentViewHolder holder, List<AppointmentService> services) {
        holder.llService.removeAllViews(); // Clear previous views

        if (services.isEmpty()) {
            TextView tvNoService = new TextView(context);
            tvNoService.setText("No services available");
            tvNoService.setPadding(8, 8, 8, 8);
            tvNoService.setTextSize(14);
            holder.llService.addView(tvNoService);
            return;
        }

        for (AppointmentService service : services) {
            TextView tvService = new TextView(context);
            tvService.setText("• " + service.getName() + " - ₱" + (service.getAmount() != null ? service.getAmount() : "0"));
            tvService.setPadding(8, 4, 8, 4);
            tvService.setTextSize(16);
            tvService.setTypeface(ResourcesCompat.getFont(context, R.font.poppinsitalic));

            holder.llService.addView(tvService);
        }
    }


    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivAppointment;
        private LinearLayout llService;
        private TextView tvTotalPayment;
        private TextView tvStatus;
        private TextView tvDateTime;
        private TextView tvNote;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAppointment = itemView.findViewById(R.id.ivAction);
            llService = itemView.findViewById(R.id.llService);

            tvTotalPayment = itemView.findViewById(R.id.tvTotalPayment);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvNote = itemView.findViewById(R.id.tvNote);
        }
    }
}
