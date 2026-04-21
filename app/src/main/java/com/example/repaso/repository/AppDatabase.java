package com.example.repaso.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.repaso.model.Pendiente;

@Database(entities = {Pendiente.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instancia;

    public abstract PendienteDao pendienteDao();

    public static synchronized AppDatabase obtener(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "repaso_db"
            ).build();
        }
        return instancia;
    }
}
