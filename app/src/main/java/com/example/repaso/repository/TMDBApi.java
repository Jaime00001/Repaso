package com.example.repaso.repository;

import com.example.repaso.model.MovieDetail;
import com.example.repaso.model.MovieResponse;
import com.example.repaso.model.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBApi {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("page") int page);

    @GET("tv/popular")
    Call<MovieResponse> getPopularSeries(@Query("page") int page);

    @GET("movie/{id}")
    Call<MovieDetail> getMovieDetail(@Path("id") int id);

    @GET("movie/{id}/videos")
    Call<VideoResponse> getMovieVideos(@Path("id") int id);

    @GET("tv/{id}")
    Call<MovieDetail> getTvDetail(@Path("id") int id);

    @GET("tv/{id}/videos")
    Call<VideoResponse> getTvVideos(@Path("id") int id);

    @GET("search/movie")
    Call<MovieResponse> searchMovies(@Query("query") String query);

    @GET("search/tv")
    Call<MovieResponse> searchSeries(@Query("query") String query);
}
