package com.me.sunshine.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Locale;

/**
 * Created by Esraa on 8/2/2017.
 */

@Entity(tableName = "city", indices = {@Index("name")})
public class City {
    @PrimaryKey
    private int uid;

    private String name;

    @ColumnInfo(name = "country_name")
    private String countryName;

    @ColumnInfo(name = "lat")
    private double latitude;

    @ColumnInfo(name = "long")
    private double longitude;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s, %s", name, countryName);
    }
}
