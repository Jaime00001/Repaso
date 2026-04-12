package com.example.repaso.model;

import java.util.List;

public class MovieDetail {
    public String title;
    public String name;
    public String overview;
    public String poster_path;
    public String backdrop_path;
    public int number_of_seasons;
    public int runtime;
    public List<Genre> genres;
    public List<Network> networks;

    public static class Genre {
        public String name;
    }

    public static class Network {
        public String name;
    }
}
