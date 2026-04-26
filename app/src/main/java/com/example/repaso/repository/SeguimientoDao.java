package com.example.repaso.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.repaso.model.Seguimiento;

import java.util.List;

@Dao
public interface SeguimientoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertar(Seguimiento s);

    @Query("SELECT * FROM seguimientos ORDER BY fecha DESC")
    LiveData<List<Seguimiento>> obtenerTodos();

    @Query("DELETE FROM seguimientos WHERE id = :id")
    void eliminar(int id);

    @Query("SELECT * FROM seguimientos WHERE id = :id LIMIT 1")
    LiveData<Seguimiento> obtenerPorId(int id);
}
