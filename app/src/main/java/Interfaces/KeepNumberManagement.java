package Interfaces;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class KeepNumberManagement extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String sharedPreferencesName = "number";
    String sharedPreferencesKeepNumber = "keep_number";

    public KeepNumberManagement(Context context) {
        sharedPreferences = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveNumberManagement(String number) {
        editor.putString(sharedPreferencesKeepNumber, number).commit();
    }

    public String getNumberManagement() {
        return sharedPreferences.getString(sharedPreferencesKeepNumber, null);
    }

    public void removeNumberManagement() {
        editor.putString(sharedPreferencesKeepNumber, null).commit();
        editor.clear();
    }
}
