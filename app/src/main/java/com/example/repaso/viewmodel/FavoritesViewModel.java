package com.example.repaso.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.repaso.model.FavoriteItem;
import com.example.repaso.repository.FirestoreRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {
    private final MutableLiveData<List<FavoriteItem>> favorites = new MutableLiveData<>();
    private final MutableLiveData<ApiState> state = new MutableLiveData<>();
    private final FirestoreRepository repo = new FirestoreRepository();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<FavoriteItem>> getFavorites() {
        return favorites;
    }

    public LiveData<ApiState> getState() {
        return state;
    }

    public void loadFavorites() {
        state.setValue(ApiState.LOADING);
        String uid = auth.getUid();
        if (uid == null) {
            state.setValue(ApiState.ERROR);
            return;
        }
        repo.getFavorites(uid).addOnSuccessListener(query -> {
            List<FavoriteItem> list = new ArrayList<>();
            for (var doc : query) {
                FavoriteItem item = doc.toObject(FavoriteItem.class);
                list.add(item);
            }
            favorites.setValue(list);
            state.setValue(ApiState.SUCCESS);
        }).addOnFailureListener(e -> state.setValue(ApiState.ERROR));
    }
    public Task<Void> addFavorite(FavoriteItem item) {
        String uid = auth.getUid();
        if (uid == null) return null;
        return repo.addFavorite(uid, item);
    }
    public Task<Void> removeFavorite(String mediaId) {
        String uid = auth.getUid();
        if (uid == null) return null;
        return repo.removeFavorite(uid, mediaId);
    }
}
