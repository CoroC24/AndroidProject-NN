package com.example.nonequi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nonequi.databinding.ActivitySignInBinding;
import kotlin.collections.ArraysKt;

public class SignIn extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private TextView clickableText;
    private EditText username, password;

    DBConnection DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        username = binding.InputUsername.getEditText();
        password = binding.InputPassword.getEditText();
        clickableText = findViewById(R.id.signUpClickableText);
        DB = new DBConnection(this);

        clickableText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), signUp.class);
                startActivity(intent);
            }
        });

        binding.signInButtonSI.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(validateData()) {
                    Boolean consult = DB.checkUserPassword(user, pass);

                    if(consult == true) {
                        Toast.makeText(SignIn.this, R.string.login_successfully, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);

                        username.setText("");
                        password.setText("");

                    } else {
                        Toast.makeText(SignIn.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    //Method to give action to back button

    @Override
    public void  onBackPressed() {
        Intent intent = new Intent(this, Welcome.class);
        startActivity(intent);
    }


    // Methods to check if data entered is correct

    public boolean userExists() {
        String user = username.getText().toString();
        Boolean checkUserExists = DB.checkIfUserExists(user);

        if(!checkUserExists) {
            Toast.makeText(SignIn.this, R.string.user_not_exists, Toast.LENGTH_SHORT).show();
            binding.InputUsername.setError("User not found");
            return false;
        }else {
            return true;
        }
    }

    public boolean passCorrect() {
        String pass = password.getText().toString();
        String user = username.getText().toString();

        Boolean checkPassCorrect = DB.checkUserPassword(user, pass);

        if(pass.isEmpty()) {
            binding.InputPassword.setError("Field cannot be empty");
            return false;

        } else if (userExists()) {
            if (!checkPassCorrect) {
                Toast.makeText(SignIn.this, R.string.incorrect_pass, Toast.LENGTH_SHORT).show();
                binding.InputPassword.setError("Password Incorrect");
                return false;
            }
        } else {
            binding.InputPassword.setError(null);
//            return true;
        }
        return true;
    }

    public boolean userCorrect() {
        String user = username.getText().toString();
        //Boolean checkUserExists = DB.checkIfUserExists(user);

        if(user.isEmpty()) {
            binding.InputUsername.setError("Field cannot be empty");
            return false;

        } else if(!userExists()){
            Toast.makeText(SignIn.this, R.string.user_not_exists, Toast.LENGTH_SHORT).show();
            binding.InputUsername.setError("User not found");
            return false;

        } else {
            binding.InputUsername.setError(null);
            return true;
        }
    }

    private boolean validateData() {
        Boolean[] result = new Boolean[] {userCorrect(), passCorrect()};

        return !ArraysKt.contains(result, false);
    }
}