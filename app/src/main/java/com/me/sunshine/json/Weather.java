package com.me.sunshine.json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Weather implements Serializable{
    @SerializedName("id")
    private double id;

    @SerializedName("main")
    private String main;

    @SerializedName("description")
    private String description;

    @SerializedName("icon")
    private String icon;

    public double getId() {
        return id;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }
}