package com.example.nonequi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;


public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public void  onBackPressed() {
        DialogFragment exitDialog = new ExitAlertDialog();
        exitDialog.show(getSupportFragmentManager(), "Exit");
    }
}