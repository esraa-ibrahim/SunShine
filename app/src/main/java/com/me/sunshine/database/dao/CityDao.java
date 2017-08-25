package com.me.sunshine.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.me.sunshine.database.entities.City;

import java.util.List;

/**
 * Created by Esraa on 8/2/2017.
 */

@Dao
public interface CityDao {

    @Query("SELECT * from city where name LIKE '%' || :cityName || '%'")
    List<City> findByCityName(String cityName);

    @Query("SELECT * from city where uid = :cityId")
    City findByCityId(int cityId);

    @Query("SELECT * from city")
    List<City> getAll();

    @Insert
    void insertAll(City... cities);

    @Insert
    long insert(City city);
}
