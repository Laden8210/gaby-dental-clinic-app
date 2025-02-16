package com.example.gabaydentalclinic.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class AppointmentService implements Parcelable {

    @SerializedName("service_id")
    private int serviceId;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("amount")
    private String amount;

    protected AppointmentService(Parcel in) {
        serviceId = in.readInt();
        name = in.readString();
        description = in.readString();
        amount = in.readString();
    }

    public static final Creator<AppointmentService> CREATOR = new Creator<AppointmentService>() {
        @Override
        public AppointmentService createFromParcel(Parcel in) {
            return new AppointmentService(in);
        }

        @Override
        public AppointmentService[] newArray(int size) {
            return new AppointmentService[size];
        }
    };

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(serviceId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(amount);
    }
}
