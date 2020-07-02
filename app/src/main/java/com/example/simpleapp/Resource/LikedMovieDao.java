package com.example.simpleapp.Resource;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.simpleapp.model.LikedMovie;
import com.example.simpleapp.model.Movie;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.DELETE;


@Dao
public interface LikedMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable likeMovie(LikedMovie likedMovie);

    @Query("select * from likedmovie")
    Maybe<List<LikedMovie>> getLikedMovies();

    @Query("select * from likedmovie where id = :id")
    Maybe<LikedMovie> getLikedMovie(Integer id);

    @Delete
    Completable unlikeMovie(LikedMovie likedMovie);
}
