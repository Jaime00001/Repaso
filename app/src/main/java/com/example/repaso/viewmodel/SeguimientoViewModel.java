package com.example.repaso.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.repaso.model.Seguimiento;
import com.example.repaso.model.MovieResponse;
import com.example.repaso.model.MovieDetail;
import com.example.repaso.repository.SeguimientoRepository;

import java.util.List;

import retrofit2.Callback;

public class SeguimientoViewModel extends AndroidViewModel {

    private SeguimientoRepository repository;
    private LiveData<List<Seguimiento>> seguimientos;

    public SeguimientoViewModel(@NonNull Application application) {
        super(application);
        repository = new SeguimientoRepository(application);
        seguimientos = repository.obtenerTodos();
    }

    public LiveData<List<Seguimiento>> getSeguimientos() {
        return seguimientos;
    }

    public LiveData<Seguimiento> getSeguimientoById(int id) {
        return repository.obtenerPorId(id);
    }

    public void insertar(Seguimiento s) {
        repository.insertar(s);
    }

    public void eliminar(int id) {
        repository.eliminar(id);
    }

    public void buscarEnTMDB(String query, String tipo, Callback<MovieResponse> callback) {
        repository.buscarEnTMDB(query, tipo, callback);
    }

    public void obtenerDetalleTMDB(int id, String tipo, Callback<MovieDetail> callback) {
        repository.obtenerDetalleTMDB(id, tipo, callback);
    }
}
