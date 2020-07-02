package com.example.simpleapp.Resource;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.simpleapp.model.LikedMovie;

@Database(entities = LikedMovie.class, version = 1, exportSchema = false)
public abstract class LocalDb extends RoomDatabase {
    private static LocalDb instance;

    public static LocalDb getInstance(Application application) {
        if (instance == null){
            instance = Room.databaseBuilder(
                    application,
                    LocalDb.class,
                    "moviedb")
                .build();
        }
        return instance;
    }

    public abstract LikedMovieDao getLikedMovieDao();
}
