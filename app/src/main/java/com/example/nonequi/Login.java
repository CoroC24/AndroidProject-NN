package com.example.nonequi;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnticipateInterpolator;

import androidx.core.splashscreen.SplashScreen;


public class Login extends AppCompatActivity {

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

    public void signIn(View view) {
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
    }

    public void signUp(View view) {
        Intent intent = new Intent(this, signUp.class);
        startActivity(intent);
    }
}