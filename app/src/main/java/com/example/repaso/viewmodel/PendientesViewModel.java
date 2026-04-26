package com.example.repaso.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.repaso.model.Pendiente;
import com.example.repaso.repository.PendientesRepository;

import java.util.List;

public class PendientesViewModel extends AndroidViewModel {

    private PendientesRepository repository;
    private LiveData<List<Pendiente>> pendientes;

    public PendientesViewModel(@NonNull Application application) {
        super(application);
        repository = new PendientesRepository(application);
        pendientes = repository.obtenerTodos();
    }

    public LiveData<List<Pendiente>> getPendientes() {
        return pendientes;
    }

    public void eliminar(int id) {
        repository.eliminar(id);
    }
}
