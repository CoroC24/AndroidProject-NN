package Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nonequi.DBConnection;
import com.example.nonequi.R;
import com.example.nonequi.databinding.ActivitySignInBinding;
import com.google.android.material.snackbar.Snackbar;

import kotlin.collections.ArraysKt;

public class SignIn extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private TextView clickableText;
    private EditText phoneNumber, password;

    DBConnection DB;
    sessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        phoneNumber = binding.InputPhoneNumber.getEditText();
        password = binding.InputPassword.getEditText();
        clickableText = findViewById(R.id.signUpClickableText);

        DB = new DBConnection(this);
        sessionManagement = new sessionManagement(SignIn.this);

        // Setting click listener
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
                String number = phoneNumber.getText().toString();
                String pass = password.getText().toString();

                if(validateData()) {
                    Boolean consult = DB.checkUserPassword(number, pass);

                    DBConnection.users.setNumber(number);
                    DB.retrieveData(number);

                    if(consult == true) {
                        Toast.makeText(SignIn.this, R.string.login_successfully, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        phoneNumber.setText("");
                        password.setText("");

                        sessionManagement.saveSession(true);

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

    private boolean numberExists() {
        String number = phoneNumber.getText().toString();
        Boolean checkUserExists = DB.checkIfNumberExists(number);

        if(!checkUserExists) {
            return false;
        }else {
            return true;
        }
    }

    private boolean passCorrect() {
        String pass = password.getText().toString();
        String number = phoneNumber.getText().toString();

        Boolean checkPassCorrect = DB.checkUserPassword(number, pass);

        if(pass.isEmpty()) {
            binding.InputPassword.setError("Field cannot be empty");
            return false;

        } else if (numberExists()) {
            if (!checkPassCorrect) {
//                Toast.makeText(SignIn.this, R.string.incorrect_pass, Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(android.R.id.content), R.string.incorrect_pass, Toast.LENGTH_SHORT).show();
                binding.InputPassword.setError("Incorrect Password");
                return false;
            }
        } else {
            binding.InputPassword.setError(null);
            //return true;
        }
        return true;
    }

    private boolean numberCorrect() {
        String number = phoneNumber.getText().toString();
        //Boolean checkUserExists = DB.checkIfUserExists(user);

        if(number.isEmpty()) {
            binding.InputPhoneNumber.setError("Field cannot be empty");
            return false;

        } else if(!numberExists()){
            Snackbar.make(findViewById(android.R.id.content), R.string.user_not_exists, Toast.LENGTH_SHORT).show();
            binding.InputPhoneNumber.setError("Number not found");
            return false;

        } else {
            binding.InputPhoneNumber.setError(null);
            return true;
        }
    }

    private boolean validateData() {
        Boolean[] result = new Boolean[] {numberCorrect(), passCorrect()};

        return !ArraysKt.contains(result, false);
    }


    //Method to get the number entered by the user
}