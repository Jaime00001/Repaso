package com.example.repaso.repository;

import com.example.repaso.model.Comment;
import com.example.repaso.model.Pendiente;
import com.example.repaso.model.Seguimiento;
import com.example.repaso.model.UserProfile;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.SetOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public class FirestoreRepository {

    private final FirebaseFirestore db;

    public FirestoreRepository() {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }

    public void saveUserProfile(UserProfile profile) {
        db.collection("users").document(profile.uid).set(profile);
    }

    public DocumentReference getUserProfile(String uid) {
        return db.collection("users").document(uid);
    }

    public CollectionReference getPendientes(String uid) {
        return db.collection("users").document(uid).collection("pendientes");
    }

    public void addPendiente(String uid, Pendiente pendiente) {
        db.collection("users").document(uid).collection("pendientes")
                .document(String.valueOf(pendiente.id))
                .set(pendiente);
    }

    public void removePendiente(String uid, int id) {
        db.collection("users").document(uid).collection("pendientes")
                .document(String.valueOf(id))
                .delete();
    }

    public CollectionReference getSeguimientos(String uid) {
        return db.collection("users").document(uid).collection("seguimientos");
    }

    public void addSeguimiento(String uid, Seguimiento seguimiento) {
        DocumentReference ref;
        if (seguimiento.id > 0) {
            ref = db.collection("users").document(uid).collection("seguimientos")
                    .document(String.valueOf(seguimiento.id));
        } else {
            ref = db.collection("users").document(uid).collection("seguimientos").document();
            seguimiento.id = ref.getId().hashCode();
        }
        ref.set(seguimiento);
    }

    public void removeSeguimiento(String uid, int id) {
        db.collection("users").document(uid).collection("seguimientos")
                .document(String.valueOf(id))
                .delete();
    }

    public DocumentReference getSeguimientoById(String uid, int id) {
        return db.collection("users").document(uid).collection("seguimientos")
                .document(String.valueOf(id));
    }

    public Query getComments(int tmdbId) {
        return db.collection("multimedia").document(String.valueOf(tmdbId))
                .collection("comments")
                .orderBy("createdAt", Query.Direction.DESCENDING);
    }

    public void addComment(int tmdbId, Comment comment) {
        DocumentReference ref = db.collection("multimedia").document(String.valueOf(tmdbId))
                .collection("comments").document();
        comment.id = ref.getId();
        ref.set(comment);
    }

    public com.google.android.gms.tasks.Task<Void> addFavorite(String uid, com.example.repaso.model.FavoriteItem fav) {
        fav.setAddedAt(com.google.firebase.Timestamp.now());
        return db.collection("users").document(uid)
                .collection("favorites").document(fav.getId())
                .set(fav, com.google.firebase.firestore.SetOptions.merge());
    }

    public com.google.android.gms.tasks.Task<Void> removeFavorite(String uid, String mediaId) {
        return db.collection("users").document(uid)
                .collection("favorites").document(mediaId)
                .delete();
    }
    public com.google.android.gms.tasks.Task<com.google.firebase.firestore.QuerySnapshot> getFavorites(String uid) {
        return db.collection("users").document(uid)
                .collection("favorites")
                .orderBy("addedAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get();
    }

}
