package com.example.skrij.gravytruck.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by skrij on 28/05/2017.
 */

public class SystemPrefsHelper {

    public static final String PREFS_NAME = "MyPrefsFile";
    private SharedPreferences settings;
    private SharedPreferences.Editor mEditor;


    private static SystemPrefsHelper ourInstance;

    public static SystemPrefsHelper getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new SystemPrefsHelper(context.getApplicationContext());
        return ourInstance;
    }

    private SystemPrefsHelper(Context context) {
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getSystemPrefString(String key) {
        return settings.getString(key, null);
    }

    public void setSystemPrefString(String key, String value) {
        if (mEditor == null)
            mEditor = settings.edit();

        mEditor.putString(key, value);
        mEditor.apply();
    }

    public void clearData() {
        if (mEditor == null)
            mEditor = settings.edit();

        mEditor.clear().apply();
    }
}
