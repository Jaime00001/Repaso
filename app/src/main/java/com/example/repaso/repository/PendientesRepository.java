package com.example.repaso.repository;

import android.content.Context;
import android.os.AsyncTask;

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
        AsyncTask.execute(() -> dao.insertar(pendiente));
    }

    public void eliminar(int id) {
        AsyncTask.execute(() -> dao.eliminar(id));
    }
}
