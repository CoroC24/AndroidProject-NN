package com.example.nonequi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.nonequi.databinding.ActivityHomeBinding;
import com.google.android.material.appbar.MaterialToolbar;


public class Home extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private TextView textViewTemp;

    DBConnection DB;
    sessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        DB = new DBConnection(this);
        sessionManagement = new sessionManagement(Home.this);

        MaterialToolbar toolBar = binding.topAppBar;
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment exitDialog = new ExitAlertDialog();
                exitDialog.show(getSupportFragmentManager(), "Exit");
            }
        });

        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.logout) {
                    Intent intent = new Intent(getApplicationContext(), Welcome.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    sessionManagement.removeSession();
                    /*SharedPreferences preferences = getSharedPreferences(stringPreferences, MODE_PRIVATE);
                    preferences.edit().clear().apply();*/
                }
                return false;
            }
        });



    }



    @Override
    public void  onBackPressed() {
        /*DialogFragment exitDialog = new ExitAlertDialog();
        exitDialog.show(getSupportFragmentManager(), "Exit");*/

        finishAffinity();
    }
}