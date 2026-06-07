package com.example.repaso.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.repaso.repository.AuthRepository;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import java.util.regex.Pattern;

public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository repo;
    private final MutableLiveData<AuthState> authState = new MutableLiveData<>();
    private final MutableLiveData<String> resetPasswordStatus = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repo = new AuthRepository();
    }

    public MutableLiveData<AuthState> getAuthState() {
        return authState;
    }

    public MutableLiveData<String> getResetPasswordStatus() {
        return resetPasswordStatus;
    }

    public FirebaseUser getCurrentUser() {
        return repo.getCurrentUser();
    }

    public void logout() {
        repo.logout();
    }

    public void login(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            authState.setValue(AuthState.error("El correo es obligatorio."));
            return;
        }
        if (password == null || password.isEmpty()) {
            authState.setValue(AuthState.error("La contraseña es obligatoria."));
            return;
        }

        authState.setValue(AuthState.loading());
        repo.login(email.trim(), password, new AuthRepository.AuthCallback() {
            @Override public void onSuccess(FirebaseUser user) {
                authState.postValue(AuthState.success(user));
            }
            @Override public void onError(String message) {
                authState.postValue(AuthState.error(message));
            }
        });
    }

    public void register(String email, String password, String confirmPassword, String username, Timestamp dateOfBirth, String mode) {
        String error = validateRegister(email, password, confirmPassword);
        if (username == null || username.trim().isEmpty()) {
            authState.setValue(AuthState.error("El nombre de usuario es obligatorio."));
            return;
        }
        if (error != null) {
            authState.setValue(AuthState.error(error));
            return;
        }

        authState.setValue(AuthState.loading());
        repo.register(email.trim(), password, username.trim(), dateOfBirth, mode, new AuthRepository.AuthCallback() {
            @Override public void onSuccess(FirebaseUser user) {
                authState.postValue(AuthState.success(user));
            }
            @Override public void onError(String message) {
                authState.postValue(AuthState.error(message));
            }
        });
    }


    public void loginWithGoogle(String idToken) {
        if (idToken == null || idToken.trim().isEmpty()) {
            authState.setValue(AuthState.error("No se pudo obtener el token de Google."));
            return;
        }

        authState.setValue(AuthState.loading());
        repo.loginWithGoogle(idToken, new AuthRepository.AuthCallback() {
            @Override public void onSuccess(FirebaseUser user) {
                authState.postValue(AuthState.success(user));
            }
            @Override public void onError(String message) {
                authState.postValue(AuthState.error(message));
            }
        });
    }

    public void resetPassword(String email) {
        if (email == null || !isValidEmail(email)) {
            resetPasswordStatus.setValue("Introduce un correo válido.");
            return;
        }

        repo.resetPassword(email.trim(), new AuthRepository.AuthCallback() {
            @Override public void onSuccess(FirebaseUser user) {
                resetPasswordStatus.postValue("Correo de recuperación enviado.");
            }
            @Override public void onError(String message) {
                resetPasswordStatus.postValue("Error: " + message);
            }
        });
    }

    private String validateRegister(String email, String password, String confirmPassword) {
        if (!isValidEmail(email)) return "Correo electrónico no válido.";
        if (password == null || password.length() < 8) return "La contraseña debe tener al menos 8 caracteres.";
        if (!Pattern.compile("[a-zA-Z]").matcher(password).find()) return "La contraseña debe contener al menos una letra.";
        if (!Pattern.compile("[0-9]").matcher(password).find()) return "La contraseña debe contener al menos un número.";
        if (!password.equals(confirmPassword)) return "Las contraseñas no coinciden.";
        return null;
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && Pattern.compile(emailPattern).matcher(email).matches();
    }
}
