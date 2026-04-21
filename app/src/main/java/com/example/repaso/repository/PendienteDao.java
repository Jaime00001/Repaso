package com.example.repaso.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.repaso.model.Pendiente;

import java.util.List;

@Dao
public interface PendienteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertar(Pendiente pendiente);

    @Query("SELECT * FROM pendientes")
    LiveData<List<Pendiente>> obtenerTodos();

    @Query("DELETE FROM pendientes WHERE id = :id")
    void eliminar(int id);
}
