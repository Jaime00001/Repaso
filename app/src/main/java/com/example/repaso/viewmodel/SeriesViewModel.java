package com.example.repaso.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.repaso.model.Movie;
import com.example.repaso.model.MovieResponse;
import com.example.repaso.repository.Repository;
import com.example.repaso.repository.FirestoreRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeriesViewModel extends ViewModel {

    private MutableLiveData<List<Movie>> items = new MutableLiveData<>();
    private MutableLiveData<ApiState> state = new MutableLiveData<>();
    private Repository repo = Repository.getInstance();
    private FirestoreRepository firestoreRepo = new FirestoreRepository();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private int page = 1;

    public LiveData<List<Movie>> getItems() { return items; }
    public LiveData<ApiState> getState() { return state; }

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
                repo.getSeries(page, kidsMode, new Callback<MovieResponse>() {
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

    public void nextPage() { page++; }
}

