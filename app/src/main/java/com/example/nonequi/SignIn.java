package com.example.nonequi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.nonequi.databinding.ActivitySignInBinding;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySignInBinding binding = ActivitySignInBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
    }
}