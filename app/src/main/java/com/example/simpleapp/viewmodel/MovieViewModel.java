package com.example.simpleapp.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.simpleapp.model.LikedMovie;
import com.example.simpleapp.model.Movie;
import com.example.simpleapp.model.Response;
import com.example.simpleapp.model.ResponseResult;
import com.example.simpleapp.repository.MovieRepository;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.functions.Action;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieViewModel extends AndroidViewModel {
    private static final String TAG = "MovieViewModel";
    private MutableLiveData<ResponseResult<List<Movie>>> liveData = new MutableLiveData<>();
    private MovieRepository repository;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    io.reactivex.disposables.CompositeDisposable roomCompositeDisposable = new io.reactivex.disposables.CompositeDisposable();

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = new MovieRepository(application);
    }

    public MutableLiveData<ResponseResult<List<Movie>>> getLiveData() {
        return liveData;
    }

    public void fetchMovieNowPlaying() {
        compositeDisposable.add(
                repository.getMoviesNowPlaying()
                        .subscribeOn(Schedulers.io())
                        .flatMap(new Function<Response, ObservableSource<Movie>>() {
                            @Override
                            public ObservableSource<Movie> apply(Response response) throws Throwable {
                                return Observable.fromIterable(response.getResults());
                            }
                        })
                        .filter(new Predicate<Movie>() {
                            @Override
                            public boolean test(Movie movie) throws Throwable {
                                return Double.parseDouble(movie.getVoteAverage()) >= 7.0d;
                            }
                        })
                        .map(new Function<Movie, Movie>() {
                            @Override
                            public Movie apply(Movie movie) throws Throwable {
                                        LikedMovie likedMovie = repository.getLikedMovieById(movie.getId())
                                                                    .blockingGet();

                                        if (likedMovie != null){
                                            movie.setLiked(true);
                                        }
                                        return movie;
                            }
                        })
                        .toList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Movie>>() {
                            @Override
                            public void accept(List<Movie> movieList) throws Throwable {
                                liveData.setValue(new ResponseResult<List<Movie>>(movieList));
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                Log.d(TAG, "error: " + throwable.getMessage());
                                liveData.setValue(new ResponseResult<List<Movie>>(throwable));
                            }
                        })
        );
    }

    public void fetchLikedMovie() {
        roomCompositeDisposable.add(
                repository.getLikedMovies()
                        .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                        .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                        .subscribe(new io.reactivex.functions.Consumer<List<LikedMovie>>() {
                            @Override
                            public void accept(List<LikedMovie> likedMovies) throws Exception {
                                fetchMoviesLikedFromService(Observable.just(likedMovies));
                            }
                        })
        );

    }

    private void fetchMoviesLikedFromService(Observable<List<LikedMovie>> observable) {
        compositeDisposable.add(
                observable.subscribeOn(Schedulers.io())
                        .flatMap(new Function<List<LikedMovie>, ObservableSource<LikedMovie>>() {
                            @Override
                            public ObservableSource<LikedMovie> apply(List<LikedMovie> likedMovies) throws Throwable {
                                return Observable.fromIterable(likedMovies);
                            }
                        })
                        .flatMap(new Function<LikedMovie, ObservableSource<Movie>>() {
                            @Override
                            public ObservableSource<Movie> apply(LikedMovie likedMovie) throws Throwable {
                                return repository.getMovieById(likedMovie.getId()).subscribeOn(Schedulers.io());
                            }
                        })
                        .map(new Function<Movie, Movie>() {
                            @Override
                            public Movie apply(Movie movie) throws Throwable {
                                movie.setLiked(true);
                                return movie;
                            }
                        })
                        .toList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Movie>>() {
                            @Override
                            public void accept(List<Movie> movieList) throws Throwable {
                                liveData.setValue(new ResponseResult<>(movieList));
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                liveData.setValue(new ResponseResult<>(throwable));
                            }
                        })
        );
    }

    public void likesMovie(int movieId) {
        LikedMovie likedMovie = new LikedMovie(movieId);

        roomCompositeDisposable.add(
                repository.likeMovie(likedMovie)
                        .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                        .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Toast.makeText(getApplication(), "Liked", Toast.LENGTH_SHORT).show();
                        })
        );

    }

    public void unlikeMovie(int movieId) {
        LikedMovie likedMovie = new LikedMovie(movieId);

        roomCompositeDisposable.add(
                repository.unlikeMovie(likedMovie)
                        .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                        .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Toast.makeText(getApplication(), "Unliked", Toast.LENGTH_SHORT).show();
                        })
        );
    }

    public void onDestroy() {
        compositeDisposable.dispose();
        roomCompositeDisposable.dispose();
    }
}
