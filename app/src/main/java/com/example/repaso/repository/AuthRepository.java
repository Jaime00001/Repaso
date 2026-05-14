package com.example.repaso.repository;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthRepository {

    private final FirebaseAuth auth;

    public AuthRepository() {
        auth = FirebaseAuth.getInstance();
    }

    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onError(String message);
    }

    public void login(String email, String password, AuthCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> callback.onSuccess(auth.getCurrentUser()))
                .addOnFailureListener(e -> callback.onError(mapError(e)));
    }

    public void register(String email, String password, String username, AuthCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        com.example.repaso.model.UserProfile profile = new com.example.repaso.model.UserProfile(user.getUid(), username, email);
                        new FirestoreRepository().saveUserProfile(profile);
                    }
                    callback.onSuccess(user);
                })
                .addOnFailureListener(e -> callback.onError(mapError(e)));
    }

    public void loginWithGoogle(String idToken, AuthCallback callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnSuccessListener(result -> {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        com.example.repaso.model.UserProfile profile = new com.example.repaso.model.UserProfile(
                                user.getUid(),
                                user.getDisplayName() != null ? user.getDisplayName() : "Usuario de Google",
                                user.getEmail()
                        );
                        new FirestoreRepository().saveUserProfile(profile);
                    }
                    callback.onSuccess(user);
                })
                .addOnFailureListener(e -> callback.onError(mapError(e)));
    }

    public void resetPassword(String email, AuthCallback callback) {
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onError(mapError(e)));
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public void logout() {
        auth.signOut();
    }

    private String mapError(Exception e) {
        if (e == null || e.getMessage() == null) return "Error desconocido.";
        return e.getMessage();
    }
}
