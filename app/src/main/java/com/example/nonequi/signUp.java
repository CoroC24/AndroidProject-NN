package com.example.nonequi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nonequi.databinding.ActivitySignUpBinding;


public class signUp extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private EditText username, password, rpassword, email;

    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        username = binding.InputUserNameSP.getEditText();
        password = binding.InputPasswordSP.getEditText();
        rpassword = binding.InputRPasswordSP.getEditText();
        email = binding.InputEmailSP.getEditText();

        DB = new DBHelper(this);

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String rpass = rpassword.getText().toString();
                String mail = email.getText().toString();

                if(user.isEmpty() || pass.isEmpty() || rpass.isEmpty() || mail.isEmpty()) {
                    Toast.makeText(signUp.this, "Please enter all credentials", Toast.LENGTH_SHORT).show();

                } else {
                    if(pass.equals(rpass)) {
                        Boolean checkUser = DB.checkUsername(user);

                        if(checkUser == false) {
                            Boolean insert = DB.insertData(user, pass, mail);

                            if(insert == true) {
                                Toast.makeText(signUp.this, "Register successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Home.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(signUp.this, "Register failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(signUp.this, "This user already registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}