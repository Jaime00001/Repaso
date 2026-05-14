package com.example.repaso.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.repaso.repository.PreferencesManager;

public class SettingsViewModel extends ViewModel {

    public MutableLiveData<String> username = new MutableLiveData<>();
    public MutableLiveData<String> language = new MutableLiveData<>();
    public MutableLiveData<Boolean> wifiOnly = new MutableLiveData<>();
    public MutableLiveData<Boolean> darkMode = new MutableLiveData<>();

    private final PreferencesManager prefs;

    public SettingsViewModel() {
        prefs = PreferencesManager.getInstance();
        loadPreferences();
    }

    public void loadPreferences() {
        String uid = com.google.firebase.auth.FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            new com.example.repaso.repository.FirestoreRepository().getUserProfile(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("displayName");
                            if (name != null) username.setValue(name);
                        }
                    });
        }
        language.setValue(prefs.getLanguage());
        wifiOnly.setValue(prefs.isWifiOnly());
        darkMode.setValue(prefs.isDarkMode());
    }

    public void savePreferences() {
        String uid = com.google.firebase.auth.FirebaseAuth.getInstance().getUid();
        if (uid != null && username.getValue() != null) {
            com.example.repaso.repository.FirestoreRepository repo = new com.example.repaso.repository.FirestoreRepository();
            repo.getUserProfile(uid).update("displayName", username.getValue());
        }
        if (language.getValue() != null) prefs.setLanguage(language.getValue());
        if (wifiOnly.getValue() != null) prefs.setWifiOnly(wifiOnly.getValue());
        if (darkMode.getValue() != null) prefs.setDarkMode(darkMode.getValue());
    }

    public void resetPreferences() {
        prefs.resetPreferences();
        loadPreferences();
    }
}
