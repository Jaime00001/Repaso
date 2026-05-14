package com.example.repaso.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.repaso.model.Seguimiento;
import com.example.repaso.model.MovieResponse;
import com.example.repaso.model.MovieDetail;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

import retrofit2.Callback;

public class SeguimientoRepository {

    private final FirestoreRepository firestore;
    private final Repository tmdb;
    private ListenerRegistration registration;

    public SeguimientoRepository() {
        firestore = new FirestoreRepository();
        tmdb = Repository.getInstance();
    }

    public LiveData<List<Seguimiento>> obtenerTodos(String userId) {
        MutableLiveData<List<Seguimiento>> data = new MutableLiveData<>();
        if (registration != null) registration.remove();
        
        registration = firestore.getSeguimientos(userId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    if (value != null) {
                        data.setValue(value.toObjects(Seguimiento.class));
                    }
                });
        
        return data;
    }

    public LiveData<Seguimiento> obtenerPorId(String userId, int id) {
        MutableLiveData<Seguimiento> data = new MutableLiveData<>();
        firestore.getSeguimientoById(userId, id).get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                data.setValue(doc.toObject(Seguimiento.class));
            }
        });
        return data;
    }

    public void insertar(String userId, Seguimiento s) {
        firestore.addSeguimiento(userId, s);
    }

    public void eliminar(String userId, int id) {
        firestore.removeSeguimiento(userId, id);
    }

    public void buscarEnTMDB(String query, String tipo, Callback<MovieResponse> callback) {
        if ("tv".equals(tipo)) tmdb.searchSeries(query, callback);
        else tmdb.searchMovies(query, callback);
    }

    public void obtenerDetalleTMDB(int id, String tipo, Callback<MovieDetail> callback) {
        if ("tv".equals(tipo)) tmdb.getTvDetail(id, callback);
        else tmdb.getMovieDetail(id, callback);
    }
}
