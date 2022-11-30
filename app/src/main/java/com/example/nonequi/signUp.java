package com.example.nonequi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.Pattern;
import com.example.nonequi.databinding.ActivitySignUpBinding;

public class signUp extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private EditText username, password, rpassword, email;
    private TextView clickableText;

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

        clickableText = findViewById(R.id.signInClickableText);

        clickableText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent);
            }
        });

        binding.signInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String rpass = rpassword.getText().toString();
                String mail = email.getText().toString();

                if(emailCorrect() == true && passCorrect() == true && rPassCorrect() == true && userCorrect() == true) {
                    if(pass.equals(rpass)) {
                        Boolean checkExists = DB.checkIfExists(user, mail);

                        if(checkExists == true) {
                            Toast.makeText(signUp.this, "This user already registered", Toast.LENGTH_SHORT).show();
                        } else {
                            Boolean insert = DB.insertData(user, pass, mail);

                            if(insert == true) {
                                Toast.makeText(signUp.this, "Register successfully", Toast.LENGTH_SHORT).show();
                                username.setText("");
                                password.setText("");
                                rpassword.setText("");
                                email.setText("");
                                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(signUp.this, "Register failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(signUp.this, "Passwords must be the same in all fields.", Toast.LENGTH_SHORT).show();
                        rpassword.setText("");
                        binding.InputRPasswordSP.setError("Incorrect password");
                    }
                }
            }
        });
    }

    public boolean emailCorrect() {
        String mail = email.getText().toString();
        if (mail.isEmpty()) {
            binding.InputEmailSP.setError("Field cannot be empty");
            return false;

        } else if(!PatternsCompat.EMAIL_ADDRESS.matcher(mail).matches()) {
            binding.InputEmailSP.setError("Please enter a valid email address");
            return false;
        } else {
            binding.InputEmailSP.setError(null);
            return true;
        }
    }

    public boolean passCorrect() {
        String pass = password.getText().toString();
        Pattern passRegex = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.])(?=\\S+$).{8,}$");

        if (pass.isEmpty()) {
            binding.InputPasswordSP.setError("Field cannot be empty");
            return false;

        } else if(!passRegex.matcher(pass).matches()) {
            binding.InputPasswordSP.setError("Password is too weak");
            return false;
        } else {
            binding.InputPasswordSP.setError(null);
            return true;
        }
    }

    public boolean userCorrect() {
        String user = username.getText().toString();

        if(user.isEmpty()) {
            binding.InputUserNameSP.setError("Field cannot be empty");
            return false;
        } else {
            return true;
        }
    }

    public boolean rPassCorrect() {
        String rpass = rpassword.getText().toString();

        if(rpass.isEmpty()) {
            binding.InputRPasswordSP.setError("Field cannot be empty");
            return false;
        } else {
            return true;
        }
    }
}