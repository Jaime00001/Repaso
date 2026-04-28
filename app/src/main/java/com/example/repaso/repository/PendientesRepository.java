package com.example.repaso.repository;

import android.content.Context;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

import com.example.repaso.model.Pendiente;

import java.util.List;

public class PendientesRepository {

    private final PendienteDao dao;

    public PendientesRepository(Context context) {
        dao = AppDatabase.obtener(context).pendienteDao();
    }

    public LiveData<List<Pendiente>> obtenerTodos() {
        return dao.obtenerTodos();
    }

    public void insertar(Pendiente pendiente) {
        Executors.newSingleThreadExecutor().execute(() -> dao.insertar(pendiente));
    }

    public void eliminar(int id) {
        Executors.newSingleThreadExecutor().execute(() -> dao.eliminar(id));
    }
}
