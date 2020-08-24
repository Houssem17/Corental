package com.esprit.corental.Utils.database;

import android.content.Context;

import com.esprit.corental.Entities.Evenement;
import com.esprit.corental.Utils.dao.Dao;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Evenement.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase instance;
    public abstract Dao produitDao();
    public static AppDataBase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "room_test_db")

                    .allowMainThreadQueries()
                    .build();

        }
        return instance;
    }
    }

