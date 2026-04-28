package com.example.repaso.repository;

import android.content.Context;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

import com.example.repaso.model.Seguimiento;
import com.example.repaso.model.MovieResponse;
import com.example.repaso.model.MovieDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SeguimientoRepository {

    private final SeguimientoDao dao;
    private final TMDBApi api;

    public SeguimientoRepository(Context context) {
        dao = AppDatabase.obtener(context).seguimientoDao();
        api = RetrofitClient.getClient().create(TMDBApi.class);
    }

    public LiveData<List<Seguimiento>> obtenerTodos() {
        return dao.obtenerTodos();
    }

    public LiveData<Seguimiento> obtenerPorId(int id) {
        return dao.obtenerPorId(id);
    }

    public void insertar(Seguimiento s) {
        Executors.newSingleThreadExecutor().execute(() -> dao.insertar(s));
    }

    public void eliminar(int id) {
        Executors.newSingleThreadExecutor().execute(() -> dao.eliminar(id));
    }

    public void buscarEnTMDB(String query, String tipo, Callback<MovieResponse> callback) {
        Call<MovieResponse> call = "tv".equals(tipo) ? api.searchSeries(query) : api.searchMovies(query);
        call.enqueue(callback);
    }

    public void obtenerDetalleTMDB(int id, String tipo, Callback<MovieDetail> callback) {
        Call<MovieDetail> call = "tv".equals(tipo) ? api.getTvDetail(id) : api.getMovieDetail(id);
        call.enqueue(callback);
    }
}
