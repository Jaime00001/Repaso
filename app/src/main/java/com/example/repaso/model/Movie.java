package com.example.repaso.model;

public class Movie {
    public int id;
    public String title;
    public String name;
    public String poster_path;
    public String backdrop_path;
    public String overview;
    public String release_date;
    public String first_air_date;
    public float vote_average;

    public String getDisplayTitle() {
        return title != null ? title : name;
    }

    public String getDisplayDate() {
        return release_date != null ? release_date : first_air_date;
    }
}
