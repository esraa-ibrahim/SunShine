package com.me.sunshine.json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BaseWeatherForecastJson implements Serializable{
    @SerializedName("city")
    private City city;

    @SerializedName("cod")
    private String cod;

    @SerializedName("message")
    private double message;

    @SerializedName("cnt")
    private double cnt;

    @SerializedName("list")
    private List<Day> list;

    public City getCity() {
        return city;
    }

    public String getCod() {
        return cod;
    }

    public double getMessage() {
        return message;
    }

    public double getCnt() {
        return cnt;
    }

    public List<Day> getDaysList() {
        return list;
    }
}