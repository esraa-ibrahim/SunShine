package com.me.sunshine.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.me.sunshine.database.dao.CityDao;
import com.me.sunshine.database.entities.City;

/**
 * Singlton class for creating db instance
 */

@Database(entities = {City.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase db;

    public static AppDatabase getInstance(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context,
                    AppDatabase.class, "database_sunshine").build();
        }
        return db;
    }

    public abstract CityDao cityDao();
}
