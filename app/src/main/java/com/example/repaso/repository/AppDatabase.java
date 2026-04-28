package com.example.repaso.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.repaso.model.Pendiente;
import com.example.repaso.model.Seguimiento;

@Database(entities = {Pendiente.class, Seguimiento.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instancia;

    public abstract PendienteDao pendienteDao();
    public abstract SeguimientoDao seguimientoDao();

    public static synchronized AppDatabase obtener(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "repaso_db"
            ).fallbackToDestructiveMigration().build();
        }
        return instancia;
    }
}
