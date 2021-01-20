package com.muqit.KharredoAdminPanel.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SessionManager {
    Editor mEditor;
    private final SharedPreferences mPrefs;

    public SessionManager(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.mPrefs = defaultSharedPreferences;
        this.mEditor = defaultSharedPreferences.edit();
    }

    public void setStringData(String str, String str2) {
        this.mEditor.putString(str, str2);
        this.mEditor.commit();
    }

    public String getStringData(String str) {
        return this.mPrefs.getString(str, "");
    }

    public void setBooleanData(String str, Boolean bool) {
        this.mEditor.putBoolean(str, bool.booleanValue());
        this.mEditor.commit();
    }

    public boolean getBooleanData(String str) {
        return this.mPrefs.getBoolean(str, false);
    }

    public void setIntData(String str, int i) {
        this.mEditor.putInt(str, i);
        this.mEditor.commit();
    }

    public int getIntData(String str) {
        return this.mPrefs.getInt(str, 0);
    }

    public void logoutUser() {
        this.mEditor.clear();
        this.mEditor.commit();
    }
}
