package com.example.repaso.repository;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    private static final String PREF_NAME = "AppPreferences";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_WIFI_ONLY = "wifi_only";
    private static final String KEY_DARK_MODE = "dark_mode";

    // Default values
    public static final String DEFAULT_LANGUAGE = "es";
    public static final boolean DEFAULT_WIFI_ONLY = false;
    public static final boolean DEFAULT_DARK_MODE = false;

    private static PreferencesManager instance;
    private final SharedPreferences sharedPreferences;

    private PreferencesManager(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void init(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context);
        }
    }

    public static PreferencesManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("PreferencesManager is not initialized, call init() first.");
        }
        return instance;
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public void setUsername(String username) {
        sharedPreferences.edit().putString(KEY_USERNAME, username).apply();
    }

    public String getLanguage() {
        return sharedPreferences.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE);
    }

    public void setLanguage(String language) {
        sharedPreferences.edit().putString(KEY_LANGUAGE, language).apply();
    }

    public boolean isWifiOnly() {
        return sharedPreferences.getBoolean(KEY_WIFI_ONLY, DEFAULT_WIFI_ONLY);
    }

    public void setWifiOnly(boolean wifiOnly) {
        sharedPreferences.edit().putBoolean(KEY_WIFI_ONLY, wifiOnly).apply();
    }

    public boolean isDarkMode() {
        return sharedPreferences.getBoolean(KEY_DARK_MODE, DEFAULT_DARK_MODE);
    }

    public void setDarkMode(boolean darkMode) {
        sharedPreferences.edit().putBoolean(KEY_DARK_MODE, darkMode).apply();
    }

    public void resetPreferences() {
        sharedPreferences.edit().clear().apply();
    }
}
