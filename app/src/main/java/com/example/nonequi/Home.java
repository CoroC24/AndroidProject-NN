package com.example.nonequi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nonequi.databinding.ActivityHomeBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;


public class Home extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private TextView textViewName, textViewCash;

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

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

        textViewName = binding.textViewName;
        textViewCash = binding.textViewCash;

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
                }
                return false;
            }
        });

        textViewName.setText(DBConnection.usuarios.getName());
        textViewCash.setText(DBConnection.usuarios.getCash());

    }



    @Override
    public void  onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.back_button_pressed_message, Snackbar.LENGTH_SHORT);

            snackbar.show();
        }

        mBackPressed = System.currentTimeMillis();
    }
}