package com.example.gabaydentalclinic.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RetrieveAppointment implements Parcelable {

    @SerializedName("appointment_id")
    private int appointmentId;

    @SerializedName("appointment_date")
    private String appointmentDate;

    @SerializedName("appointment_time")
    private String appointmentTime;

    @SerializedName("notes")
    private String notes;

    @SerializedName("status")
    private int status;

    @SerializedName("services")
    private List<AppointmentService> services;

    public RetrieveAppointment() {
    }

    protected RetrieveAppointment(Parcel in) {
        appointmentId = in.readInt();
        appointmentDate = in.readString();
        appointmentTime = in.readString();
        notes = in.readString();
        status = in.readInt();
    }

    public static final Creator<RetrieveAppointment> CREATOR = new Creator<RetrieveAppointment>() {
        @Override
        public RetrieveAppointment createFromParcel(Parcel in) {
            return new RetrieveAppointment(in);
        }

        @Override
        public RetrieveAppointment[] newArray(int size) {
            return new RetrieveAppointment[size];
        }
    };

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<AppointmentService> getServices() {
        return services;
    }

    public void setServices(List<AppointmentService> services) {
        this.services = services;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(appointmentId);
        dest.writeString(appointmentDate);
        dest.writeString(appointmentTime);
        dest.writeString(notes);
        dest.writeInt(status);
    }
}
