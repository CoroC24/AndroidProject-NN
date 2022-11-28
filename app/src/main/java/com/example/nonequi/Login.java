package com.example.nonequi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.core.splashscreen.SplashScreen;


public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void signIn(View view) {
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
    }

    public void signUp(View view) {
        Intent intent = new Intent(this, signUp.class);
        startActivity(intent);
    }
}