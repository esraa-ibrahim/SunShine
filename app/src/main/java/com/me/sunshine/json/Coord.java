package com.me.sunshine.json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Coord implements Serializable{
    @SerializedName("lon")
    private double lon;

    @SerializedName("lat")
    private double lat;

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }
}