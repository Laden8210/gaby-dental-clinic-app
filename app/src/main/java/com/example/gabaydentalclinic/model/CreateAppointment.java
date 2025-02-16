package com.example.gabaydentalclinic.model;

import java.util.Date;
import java.util.List;

public class CreateAppointment {

        private String client_id;
        private String appointment_date;
        private String appointment_time;
        private String notes;
        private List<Integer> serviceIds; // Change to List<Integer>

        public CreateAppointment(String client_id, String appointment_date, String appointment_time, String notes, List<Integer> serviceIds) {
            this.client_id = client_id;
            this.appointment_date = appointment_date;
            this.appointment_time = appointment_time;
            this.notes = notes;
            this.serviceIds = serviceIds;
        }
    }

