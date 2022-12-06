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

import kotlin.collections.ArraysKt;

public class signUp extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private EditText username, password, rpassword, email;
    private TextView clickableText;

    DBConnection DB;
    sessionManagement sessionManagement;

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

        DB = new DBConnection(this);
        sessionManagement = new sessionManagement(signUp.this);

        clickableText = findViewById(R.id.signInClickableText);

        clickableText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent);
            }
        });

        binding.signUpButtonSP.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String rpass = rpassword.getText().toString();
                String mail = email.getText().toString();


                 if(validateData()) {
                    if(pass.equals(rpass)) {
                        Boolean insert = DB.insertData(user, pass, mail);

                        if(insert == true) {
                            Toast.makeText(signUp.this, R.string.register_successfully, Toast.LENGTH_SHORT).show();
                            username.setText("");
                            password.setText("");
                            rpassword.setText("");
                            email.setText("");
                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            sessionManagement.saveSession(true);

                        } else {
                            Toast.makeText(signUp.this, R.string.register_failed, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(signUp.this, R.string.password_same, Toast.LENGTH_SHORT).show();
                        rpassword.setText("");
                        binding.InputRPasswordSP.setError("Incorrect Password");
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


    //Methods to check if data entered are correct

    public boolean emailCorrect() {
        String mail = email.getText().toString();
        Boolean checkEmailExists = DB.checkIfEmailExists(mail);

        if (mail.isEmpty()) {
            binding.InputEmailSP.setError("Field cannot be empty");
            return false;

        } else if(checkEmailExists == true) {
            Toast.makeText(signUp.this, R.string.email_used, Toast.LENGTH_SHORT).show();
            binding.InputEmailSP.setError("This email has already used");
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
        Pattern passRegex = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_,.:!¡?¿<>])(?=\\S+$).{8,}$");

        if(pass.isEmpty()) {
            binding.InputPasswordSP.setError("Field cannot be empty");
            return false;

        } else if(!passRegex.matcher(pass).matches()) {

            binding.InputPasswordSP.setError("Password is too weak");
            //Toast.makeText(this, "Password must contain at least one upper and lower case character, one number, one special character and 8 or more characters", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            binding.InputPasswordSP.setError(null);
            return true;
        }
    }

    public boolean userCorrect() {
        String user = username.getText().toString();
        Boolean checkUserExists = DB.checkIfUserExists(user);
        Pattern userRegex = Pattern.compile("^(?=\\S+$).{4,}$");

        if(user.isEmpty()) {
            binding.InputUserNameSP.setError("Field cannot be empty");
            return false;

        } else if(!userRegex.matcher(user).matches()) {
            binding.InputUserNameSP.setError("Please enter a valid username");
            return false;

        } else if(checkUserExists == true){
            Toast.makeText(signUp.this, R.string.user_used, Toast.LENGTH_SHORT).show();
            binding.InputUserNameSP.setError("User already used");
            return false;

        } else {
            binding.InputUserNameSP.setError(null);
            return true;
        }
    }

    public boolean rPassCorrect() {
        String rpass = rpassword.getText().toString();

        if(rpass.isEmpty()) {
            binding.InputRPasswordSP.setError("Field cannot be empty");
            return false;
        } else {
            binding.InputRPasswordSP.setError(null);
            return true;
        }
    }

    private boolean validateData() {
        Boolean[] result = new Boolean[] {this.emailCorrect(), this.passCorrect(), this.userCorrect(), this.rPassCorrect()};

        return !ArraysKt.contains(result, false);
    }

    private boolean passRegex() {
        Pattern passRegex = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_,.:!¡?¿<>])(?=\\S+$).{8,}$");
        Pattern passRegex2 = Pattern.compile("^(?=.*[a-z][A-Z])");
        Pattern passRegex3 = Pattern.compile("^(?=.*[0-9])");
        Pattern passRegex4 = Pattern.compile("^(?=.*[@#$%^&+=_,.:!¡?¿<>])");
        Pattern passRegex5 = Pattern.compile("^(?=\\S+$)");
        Pattern passRegex6 = Pattern.compile("^.{8,}$");

        return true;
    }
}