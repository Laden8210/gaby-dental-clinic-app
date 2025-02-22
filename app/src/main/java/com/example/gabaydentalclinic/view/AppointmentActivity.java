package com.example.gabaydentalclinic.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.gabaydentalclinic.R;
import com.example.gabaydentalclinic.adapter.AppointmentAdapter;
import com.example.gabaydentalclinic.model.AppointmentService;
import com.example.gabaydentalclinic.model.RetrieveAppointment;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AppointmentActivity extends AppCompatActivity {

    private MaterialButton btnCancel;
    private TextView tvDate, tvTime, tvPayment, tvStatus;

    private LinearLayout llService;

    private RetrieveAppointment appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);


        btnCancel = findViewById(R.id.btnCancel);

        llService = findViewById(R.id.llService);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvPayment = findViewById(R.id.tvPayment);
        tvStatus = findViewById(R.id.tvStatus);


        appointment = getIntent().getParcelableExtra("appointment");

        if (appointment != null) {


            tvDate.setText(appointment.getAppointmentDate());
            tvTime.setText(appointment.getAppointmentTime());
//            tvPayment.setText("$" + getTotalPayment(appointment.getServices()));
            tvStatus.setText(getStatus(appointment.getStatus()));
//            makeServiceList(appointment.getServices());

            Log.d("AppointmentActivity", "Appointment Loaded: " + appointment.getAppointmentDate());
        } else {
            Toast.makeText(this, "No appointment data found", Toast.LENGTH_SHORT).show();
            Log.e("AppointmentActivity", "No appointment data received.");
        }

        btnCancel.setOnClickListener(view -> {
            Toast.makeText(this, "Appointment Canceled", Toast.LENGTH_SHORT).show();
        });
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



    private void makeServiceList( List<AppointmentService> services) {
        llService.removeAllViews(); // Clear previous views

        if (services.isEmpty()) {
            TextView tvNoService = new TextView(this);
            tvNoService.setText("No services available");
            tvNoService.setPadding(8, 8, 8, 8);
            tvNoService.setTextSize(14);
            llService.addView(tvNoService);
            return;
        }

        for (AppointmentService service : services) {
            TextView tvService = new TextView(this);
            tvService.setText("• " + service.getName() + " - ₱" + (service.getAmount() != null ? service.getAmount() : "0"));
            tvService.setPadding(8, 4, 8, 4);
            tvService.setTextSize(16);
            tvService.setTypeface(ResourcesCompat.getFont(this, R.font.poppinsitalic));
            llService.addView(tvService);
        }
    }


    private String getTotalPayment(List<AppointmentService> services) {
        double total = 0;
        for (AppointmentService service : services) {
            total += Double.valueOf(service.getAmount() != null ? service.getAmount() : "0");
        }
        return String.valueOf(total);
    }

}
