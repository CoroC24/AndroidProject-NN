package com.example.nonequi;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.Toast;

import androidx.core.splashscreen.SplashScreen;


public class Welcome extends AppCompatActivity {

    private static final String stringPreferences = "com.example.nonequi";
    private static final String preferencesStatusRPass = "status.session";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getSplashScreen().setOnExitAnimationListener(splashScreenView -> {
                final ObjectAnimator slideLeft = ObjectAnimator.ofFloat(
                        splashScreenView,
                        View.TRANSLATION_X,
                        0f,
                        -splashScreenView.getWidth()
                );
                slideLeft.setInterpolator(new AnticipateInterpolator());
                slideLeft.setDuration(350L);

                slideLeft.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        splashScreenView.remove();
                    }
                });

                slideLeft.start();
            });
        }

        splashScreen.setKeepOnScreenCondition(() -> false );
    }

    protected void onStart() {

        super.onStart();

        checkStatusLogged();
    }

    public void signIn(View view) {
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
    }

    public void signUp(View view) {
        Intent intent = new Intent(this, signUp.class);
        startActivity(intent);
    }

    //Method to give action to back button

    @Override
    public void  onBackPressed() {
        finishAffinity();
    }

    // Method to check if user is logged

    private void checkStatusLogged() {
        sessionManagement sessionManagement = new sessionManagement(Welcome.this);
        Boolean status = sessionManagement.getSession();

        if(status == true) {
            Intent intent = new Intent(Welcome.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}