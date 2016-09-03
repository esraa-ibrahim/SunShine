package com.me.sunshine.json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Temp implements Serializable{
    @SerializedName("day")
    private double day;

    @SerializedName("min")
    private double min;

    @SerializedName("max")
    private double max;

    @SerializedName("night")
    private double night;

    @SerializedName("eve")
    private double eve;

    @SerializedName("morn")
    private double morn;

    public double getDay() {
        return day;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getNight() {
        return night;
    }

    public double getEve() {
        return eve;
    }

    public double getMorn() {
        return morn;
    }
}