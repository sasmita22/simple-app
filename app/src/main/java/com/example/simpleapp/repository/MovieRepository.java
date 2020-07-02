package com.example.simpleapp.repository;

import android.app.Application;

import com.example.simpleapp.Resource.ApiInterface;
import com.example.simpleapp.Resource.ApiService;
import com.example.simpleapp.Resource.LocalDb;
import com.example.simpleapp.model.LikedMovie;
import com.example.simpleapp.model.Movie;
import com.example.simpleapp.model.Response;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.rxjava3.core.Observable;

public class MovieRepository {
    private static final String apiKey = "8abf0610f63f148378b21a20d1a10f2d";
    private ApiInterface networkService;
    private LocalDb localDb;

    public MovieRepository(Application application) {
        localDb = LocalDb.getInstance(application);
        networkService = ApiService.getInstance();
    }

    public Observable<Response> getMoviesNowPlaying(){
        return networkService.getMoviesNowPlaying(apiKey);
    }

    public Maybe<List<LikedMovie>> getLikedMovies(){
        return localDb.getLikedMovieDao().getLikedMovies();
    }

    public Completable unlikeMovie(LikedMovie likedMovie){
        return localDb.getLikedMovieDao().unlikeMovie(likedMovie);
    }

    public Completable likeMovie(LikedMovie likedMovie){
        return localDb.getLikedMovieDao().likeMovie(likedMovie);
    }

    public Observable<Movie> getMovieById(Integer id){
        return networkService.getMovieById(id, apiKey);
    }

    public Maybe<LikedMovie> getLikedMovieById(Integer id){
        return localDb.getLikedMovieDao().getLikedMovie(id);
    }

}
