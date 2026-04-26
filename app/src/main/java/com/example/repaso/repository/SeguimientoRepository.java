package com.example.repaso.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.repaso.model.Seguimiento;

import java.util.List;

public class SeguimientoRepository {

    private final SeguimientoDao dao;

    public SeguimientoRepository(Context context) {
        dao = AppDatabase.obtener(context).seguimientoDao();
    }

    public LiveData<List<Seguimiento>> obtenerTodos() {
        return dao.obtenerTodos();
    }

    public LiveData<Seguimiento> obtenerPorId(int id) {
        return dao.obtenerPorId(id);
    }

    public void insertar(Seguimiento s) {
        AsyncTask.execute(() -> dao.insertar(s));
    }

    public void eliminar(int id) {
        AsyncTask.execute(() -> dao.eliminar(id));
    }
}
