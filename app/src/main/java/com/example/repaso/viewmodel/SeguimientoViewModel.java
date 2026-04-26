package com.example.repaso.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.repaso.model.Seguimiento;
import com.example.repaso.repository.SeguimientoRepository;

import java.util.List;

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
}
