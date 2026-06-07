package com.example.repaso.repository;

import com.example.repaso.model.MovieDetail;
import com.example.repaso.model.MovieResponse;
import com.example.repaso.model.VideoResponse;

import retrofit2.Callback;

public class Repository {

    private static Repository instance;
    private final TMDBApi api;

    private Repository() {
        api = RetrofitClient.getClient().create(TMDBApi.class);
    }

    public static Repository getInstance() {
        if (instance == null) instance = new Repository();
        return instance;
    }

    public void getMovies(int page, Callback<MovieResponse> callback) {
        getMovies(page, false, callback);
    }
    public void getMovies(int page, boolean kidsMode, Callback<MovieResponse> callback) {
        if (kidsMode) {
            api.getCartoonMovies(page, "16").enqueue(callback);
        } else {
            api.getPopularMovies(page).enqueue(callback);
        }
    }

    public void getSeries(int page, Callback<MovieResponse> callback) {
        getSeries(page, false, callback);
    }


    public void getSeries(int page, boolean kidsMode, Callback<MovieResponse> callback) {
        if (kidsMode) {
            api.getCartoonSeries(page, "16").enqueue(callback);
        } else {
            api.getPopularSeries(page).enqueue(callback);
        }
    }

    public void searchMovies(String query, Callback<MovieResponse> callback) {
        api.searchMovies(query).enqueue(callback);
    }

    public void searchSeries(String query, Callback<MovieResponse> callback) {
        api.searchSeries(query).enqueue(callback);
    }

    public void getMovieDetail(int id, Callback<MovieDetail> callback) {
        api.getMovieDetail(id).enqueue(callback);
    }

    public void getMovieVideos(int id, Callback<VideoResponse> callback) {
        api.getMovieVideos(id).enqueue(callback);
    }

    public void getTvDetail(int id, Callback<MovieDetail> callback) {
        api.getTvDetail(id).enqueue(callback);
    }

    public void getTvVideos(int id, Callback<VideoResponse> callback) {
        api.getTvVideos(id).enqueue(callback);
    }
}
