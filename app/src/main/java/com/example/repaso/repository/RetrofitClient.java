package com.example.repaso.repository;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        String language = PreferencesManager.getInstance().getLanguage();
                        
                        okhttp3.HttpUrl originalHttpUrl = chain.request().url();
                        okhttp3.HttpUrl newUrl = originalHttpUrl.newBuilder()
                                .addQueryParameter("language", language)
                                .build();

                        Request newRequest = chain.request().newBuilder()
                                .url(newUrl)
                                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3MTcxY2FhNjdiZWZhNDNmNWViZmRmNmYzYzQzM2U4NiIsIm5iZiI6MTc3MjQ0MjE0NC44NTIsInN1YiI6IjY5YTU1MjIwZWNlZTIzMzMyY2MxN2RiMiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.yUeSzAhXVVjqfIzYWgo7EMQ7eZeOs51W21cf2g4JEco")
                                .build();
                        return chain.proceed(newRequest);
                    }).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
