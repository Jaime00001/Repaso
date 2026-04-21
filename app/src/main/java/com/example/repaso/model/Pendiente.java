package com.example.repaso.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pendientes")
public class Pendiente {
    @PrimaryKey
    public int id;
    public String titulo;
    public String imagenPath;
    public String tipo; // "movie" o "tv"
}
