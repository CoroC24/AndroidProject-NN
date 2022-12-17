package Views.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

import com.example.nonequi.DBConnection;
import com.example.nonequi.R;
import com.example.nonequi.databinding.ActivitySignUpBinding;
import com.google.android.material.snackbar.Snackbar;

import kotlin.collections.ArraysKt;
import saveInSharedPreferences.sessionManagement;

public class signUp extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private EditText phoneNumber, username, password, rpassword, email;
    private TextView clickableText;
    public String date;

    DBConnection DB;
    saveInSharedPreferences.sessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        phoneNumber = binding.InputPhoneNumberSP.getEditText();
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

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(calendar.getTime());

        binding.signUpButtonSP.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String number = phoneNumber.getText().toString();
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String rpass = rpassword.getText().toString();
                String mail = email.getText().toString();


                 if(validateData()) {
                    if(pass.equals(rpass)) {
                        Boolean insert = DB.insertData(number, user, pass, mail, date);

                        if(insert == true) {
                            Toast.makeText(signUp.this, R.string.register_successfully, Toast.LENGTH_SHORT).show();
                            phoneNumber.setText("");
                            username.setText("");
                            password.setText("");
                            rpassword.setText("");
                            email.setText("");

                            Intent intent = new Intent(getApplicationContext(), SignIn.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

//                            sessionManagement.saveSession(true);

                        } else {
                            Toast.makeText(signUp.this, R.string.register_failed, Toast.LENGTH_SHORT).show();
                        }
                    } else {
//                        Toast.makeText(signUp.this, R.string.password_same, Toast.LENGTH_SHORT).show();
                        Snackbar.make(findViewById(android.R.id.content), R.string.password_same, Snackbar.LENGTH_SHORT).show();
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

    private boolean emailCorrect() {
        String mail = email.getText().toString();
        Boolean checkEmailExists = DB.checkIfEmailExists(mail);

        if (mail.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), R.string.field_empty, Snackbar.LENGTH_SHORT).show();
            binding.InputEmailSP.setError("Field cannot be empty");
            return false;

        } else if(checkEmailExists == true) {
//            Toast.makeText(signUp.this, R.string.email_used, Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(android.R.id.content), R.string.email_used, Snackbar.LENGTH_SHORT).show();
            binding.InputEmailSP.setError("This email has already used");
            return false;

        } else if(!PatternsCompat.EMAIL_ADDRESS.matcher(mail).matches()) {
            binding.InputEmailSP.setError("Please enter a valid email address");
//            Toast.makeText(signUp.this, R.string.valid_email, Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(android.R.id.content), R.string.valid_email, Snackbar.LENGTH_SHORT).show();
            return false;

        } else {
            binding.InputEmailSP.setError(null);
            return true;
        }
    }

    private boolean passCorrect() {
        String pass = password.getText().toString();
        Pattern passRegex = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_,.:!¡?¿<>])(?=\\S+$).{8,}$");

        if(pass.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), R.string.field_empty, Snackbar.LENGTH_SHORT).show();
            binding.InputPasswordSP.setError("Field cannot be empty");
            return false;

        } else if(!passRegex.matcher(pass).matches()) {

            passRegex();
            binding.InputPasswordSP.setError("Password is too weak");
            return false;

        } else {
            binding.InputPasswordSP.setError(null);
            return true;
        }
    }

    private boolean userCorrect() {
        String user = username.getText().toString();
        Pattern userRegex = Pattern.compile("^.{8,}$");

        if(user.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), R.string.field_empty, Snackbar.LENGTH_SHORT).show();
            binding.InputUserNameSP.setError("Field cannot be empty");
            return false;

        } else if(!userRegex.matcher(user).matches()) {
            Snackbar.make(findViewById(android.R.id.content), R.string.user_length, Snackbar.LENGTH_SHORT).show();
            userRegex();
            binding.InputUserNameSP.setError("Please enter a valid name");
            return false;

        } /*else if(checkUserExists){
//            Toast.makeText(signUp.this, R.string.user_used, Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(android.R.id.content), R.string.user_used, Toast.LENGTH_SHORT).show();
            binding.InputUserNameSP.setError("This user has already used");
            return false;

        }*/ else {
            binding.InputUserNameSP.setError(null);
            return true;
        }
    }

    private boolean numberCorrect() {
        String number = phoneNumber.getText().toString();
        Boolean checkNumberExists = DB.checkIfNumberExists(number);

        if(number.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), R.string.field_empty, Snackbar.LENGTH_SHORT).show();
            binding.InputPhoneNumberSP.setError("Field cannot be empty");
            return false;

        } else if(checkNumberExists) {
            Snackbar.make(findViewById(android.R.id.content), R.string.phone_number_used, Snackbar.LENGTH_SHORT).show();
            binding.InputPhoneNumberSP.setError("This number has already used");
            return false;

        } else if(number.length() != 10){
            Snackbar.make(findViewById(android.R.id.content), R.string.phone_number_length, Snackbar.LENGTH_SHORT).show();
            binding.InputPhoneNumberSP.setError("Please enter a valid phone number");
            return false;
        } else {
            binding.InputPhoneNumberSP.setError(null);
            return true;
        }
    }

    private boolean rPassCorrect() {
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
        Boolean[] result = new Boolean[] {this.emailCorrect(), this.passCorrect(), this.userCorrect(), this.numberCorrect(), this.rPassCorrect()};

        return !ArraysKt.contains(result, false);
    }

    private void passRegex() {
        String pass = password.getText().toString();

        if(!pass.matches(".*[a-z].*")) {
//            Toast.makeText(signUp.this, R.string.pass_regex_minus, Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(android.R.id.content), R.string.pass_regex_minus,Snackbar.LENGTH_SHORT).show();
            return;
        }
        if(!pass.matches(".*[A-Z].*")) {
//            Toast.makeText(signUp.this, R.string.pass_regex_mayus, Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(android.R.id.content), R.string.pass_regex_mayus, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if(!pass.matches(".*[0-9].*")) {
//            Toast.makeText(signUp.this, R.string.pass_regex_number, Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(android.R.id.content), R.string.pass_regex_number, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if(!pass.matches(".*[@#$%^&+=_,.:!¡?¿<>].*")) {
//            Toast.makeText(signUp.this, R.string.pass_regex_special_char, Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(android.R.id.content), R.string.pass_regex_special_char, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if(!pass.matches(".*[{8,}].*")) {
//            Toast.makeText(signUp.this, R.string.pass_regex_length, Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(android.R.id.content), R.string.pass_regex_length, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if(pass.matches(".*[(?=\\S+$)].*")) {
//            Toast.makeText(signUp.this, R.string.pass_regex_spaces, Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(android.R.id.content), R.string.pass_regex_spaces, Snackbar.LENGTH_SHORT).show();
            return;
        }

    }

    private void userRegex() {
        String user = username.getText().toString();

        if (user.matches(".*[0-9].*")) {
            Snackbar.make(findViewById(android.R.id.content), R.string.user_regex_number, Snackbar.LENGTH_SHORT).show();
        }
    }
}