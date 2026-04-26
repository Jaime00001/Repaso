package com.example.repaso.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "seguimientos")
public class Seguimiento {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String titulo;
    public String imagenPath;
    public String fecha; // "YYYY-MM-DD"
    public String tipo;  // "movie" o "tv"
    public int tmdbId;
    public float puntuacion;
}
