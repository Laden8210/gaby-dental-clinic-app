package com.example.gabaydentalclinic.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_LOCK_ENABLED = "lock_enabled";

    private static SessionManager instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private SessionManager(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    public void saveUserId(String userId) {
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public boolean isLoggedIn() {
        return getUserId() != null;
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

    public void setLockEnabled(boolean isEnabled) {
        editor.putBoolean(KEY_LOCK_ENABLED, isEnabled);
        editor.apply();
    }

    public boolean isLockEnabled() {
        return sharedPreferences.getBoolean(KEY_LOCK_ENABLED, false);
    }
}
