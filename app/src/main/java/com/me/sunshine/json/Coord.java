package com.me.sunshine.json;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;


public class Coord implements Serializable {
    @SerializedName("lon")
    private double lon;

    @SerializedName("lat")
    private double lat;

    public Coord() {

    }

    public Coord(JSONObject json) {

        this.lon = json.optDouble("lon");
        this.lat = json.optDouble("lat");

    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }
}