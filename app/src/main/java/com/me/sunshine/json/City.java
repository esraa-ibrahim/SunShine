package com.me.sunshine.json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class City implements Serializable {
    @SerializedName("id")
    private double id;

    @SerializedName("name")
    private String name;

    @SerializedName("coord")
    private Coord coord;

    @SerializedName("country")
    private String country;

    @SerializedName("population")
    private double population;

    public double getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coord getCoord() {
        return coord;
    }

    public String getCountry() {
        return country;
    }

    public double getPopulation() {
        return population;
    }
}