package Login;

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

import com.example.nonequi.R;

import Interfaces.Home;
import saveInSharedPreferences.sessionManagement;


public class Welcome extends AppCompatActivity {

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

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
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