package com.example.repaso.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.repaso.model.Movie;
import com.example.repaso.model.MovieResponse;
import com.example.repaso.model.Pendiente;
import com.example.repaso.repository.PendientesRepository;
import com.example.repaso.repository.Repository;
import com.example.repaso.repository.FirestoreRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeliculasViewModel extends AndroidViewModel {

    private MutableLiveData<List<Movie>> items = new MutableLiveData<>();
    private MutableLiveData<ApiState> state = new MutableLiveData<>();
    private Repository repo = Repository.getInstance();
    private PendientesRepository pendientesRepository;
    private int page = 1;

    public PeliculasViewModel(@NonNull Application application) {
        super(application);
        pendientesRepository = new PendientesRepository();
    }

    public LiveData<List<Movie>> getItems() { return items; }
    public LiveData<ApiState> getState() { return state; }

    private final FirestoreRepository firestoreRepo = new FirestoreRepository();
    private final com.google.firebase.auth.FirebaseAuth firebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance();

    public void load() {
        state.setValue(ApiState.LOADING);
        String uid = firebaseAuth.getUid();
        if (uid == null) {
            state.setValue(ApiState.ERROR);
            return;
        }
        firestoreRepo.getUserProfile(uid).get()
            .addOnSuccessListener(document -> {
                boolean kidsMode = false;
                if (document.exists()) {
                    String mode = document.getString("mode");
                    kidsMode = "kids".equalsIgnoreCase(mode);
                }
                repo.getMovies(page, kidsMode, new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            items.setValue(response.body().results);
                            state.setValue(ApiState.SUCCESS);
                        } else {
                            state.setValue(ApiState.ERROR);
                        }
                    }
                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        state.setValue(ApiState.ERROR);
                    }
                });
            })
            .addOnFailureListener(e -> state.setValue(ApiState.ERROR));
    }

    public void anadirAPendientes(Pendiente p) {
        pendientesRepository.insertar(p.userId, p);
    }

    public void nextPage() { page++; }
}
