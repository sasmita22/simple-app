package com.example.simpleapp.Resource;

import com.example.simpleapp.model.Movie;
import com.example.simpleapp.model.Response;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("now_playing")
    Observable<Response> getMoviesNowPlaying(@Query("api_key") String apiKey);

    @GET("{id}")
    Observable<Movie> getMovieById(@Path("id") Integer id, @Query("api_key") String apiKey);
}
