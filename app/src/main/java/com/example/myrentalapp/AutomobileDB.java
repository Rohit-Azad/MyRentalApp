package com.example.myrentalapp;

import java.io.Serializable;
import java.util.ArrayList;

public class AutomobileDB implements Serializable {

    String type;
    String make;
    String model;
    String seats;
    String quantity;
    String pricePerHour;
    String pricePerDay;
    String[] colors;

    public String getMake() {
        return make;
    }
    public String getType()
    {
        return type;
    }

    public String getModel() {
        return model;
    }

    public String getPricePerHour() {
        return pricePerHour;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getSeats() {
        return seats;
    }

    public String getPricePerDay() {
        return pricePerDay;
    }

    public AutomobileDB()
    {

    }
}
