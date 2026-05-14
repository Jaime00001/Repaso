package com.example.repaso.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.repaso.model.Pendiente;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

public class PendientesRepository {

    private final FirestoreRepository firestore;
    private ListenerRegistration registration;

    public PendientesRepository() {
        firestore = new FirestoreRepository();
    }

    public LiveData<List<Pendiente>> obtenerTodos(String userId) {
        MutableLiveData<List<Pendiente>> data = new MutableLiveData<>();
        
        if (registration != null) registration.remove();
        
        registration = firestore.getPendientes(userId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    if (value != null) {
                        data.setValue(value.toObjects(Pendiente.class));
                    }
                });
        
        return data;
    }

    public void insertar(String userId, Pendiente pendiente) {
        firestore.addPendiente(userId, pendiente);
    }

    public void eliminar(String userId, int id) {
        firestore.removePendiente(userId, id);
    }
}
