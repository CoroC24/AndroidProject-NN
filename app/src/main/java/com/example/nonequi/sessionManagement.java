package com.example.nonequi;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class sessionManagement extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String sharedPreferencesName = "session";
    String sharedPreferencesLogged = "session_logged";


    public sessionManagement(Context context) {
        sharedPreferences = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(Boolean status) {

        editor.putBoolean(sharedPreferencesLogged, status).commit();
    }

    public boolean getSession() {
        return sharedPreferences.getBoolean(sharedPreferencesLogged, false);
    }

    public void removeSession() {
        editor.putBoolean(sharedPreferencesLogged, false).commit();
        editor.clear();
    }
}
