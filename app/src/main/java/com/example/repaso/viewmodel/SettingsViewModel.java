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
        username.setValue(prefs.getUsername());
        language.setValue(prefs.getLanguage());
        wifiOnly.setValue(prefs.isWifiOnly());
        darkMode.setValue(prefs.isDarkMode());
    }

    public void savePreferences() {
        if (username.getValue() != null) prefs.setUsername(username.getValue());
        if (language.getValue() != null) prefs.setLanguage(language.getValue());
        if (wifiOnly.getValue() != null) prefs.setWifiOnly(wifiOnly.getValue());
        if (darkMode.getValue() != null) prefs.setDarkMode(darkMode.getValue());
    }

    public void resetPreferences() {
        prefs.resetPreferences();
        loadPreferences();
    }
}
