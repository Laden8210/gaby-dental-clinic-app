package com.example.gabaydentalclinic.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class DentalService implements Parcelable {

    private String id;
    private String name;
    private String description;
    private double price;

    public DentalService() {
    }

    protected DentalService(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
    }

    public static final Creator<DentalService> CREATOR = new Creator<DentalService>() {
        @Override
        public DentalService createFromParcel(Parcel in) {
            return new DentalService(in);
        }

        @Override
        public DentalService[] newArray(int size) {
            return new DentalService[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(price);
    }
}
