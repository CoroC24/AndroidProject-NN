package Interfaces;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nonequi.DBConnection;
import com.example.nonequi.R;
import com.example.nonequi.databinding.ActivitySendMoneyBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import Login.Users;
import kotlin.collections.ArraysKt;

public class SendMoney extends AppCompatActivity {

    private ActivitySendMoneyBinding binding;
    private TextView textViewMoney;
    public EditText phoneNumber, moneyToSend;

    DBConnection DB;
    Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendMoneyBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        DB = new DBConnection(this);
        users = new Users();

        textViewMoney = binding.textViewMoneySM;
        phoneNumber = binding.InputPhoneNumberSM.getEditText();
        moneyToSend = binding.InputMoneyToSendSM.getEditText();

        textViewMoney.setText(DBConnection.users.getMoney());

        MaterialToolbar toolBar = binding.topAppBarSM;
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        binding.sendMoneyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(validateData()) {
                    DialogFragment transactionDialog = new TransactionAlertDialog();
                    transactionDialog.show(getSupportFragmentManager(), "Transaction");
                }
            }
        });
    }

    //Method to give action to back button

    @Override
    public void  onBackPressed() {
        finish();
    }

    // Methods to check if data entered is correct

    private boolean numberExists() {
        String number = phoneNumber.getText().toString();
        Boolean checkNumberExists = DB.checkIfNumberExists(number);

        if(!checkNumberExists) {
            return false;
        }else {
            return true;
        }
    }

    private boolean numberCorrect() {
        String number = phoneNumber.getText().toString();

        if(number.isEmpty()) {
            binding.InputPhoneNumberSM.setError("Field cannot be empty");
            return false;

        } else if(!numberExists()){
            Snackbar.make(findViewById(android.R.id.content), R.string.number_not_exists, Snackbar.LENGTH_SHORT).show();
            binding.InputPhoneNumberSM.setError("Number not found");
            return false;

        } else if(number.equals(DBConnection.users.getNumber())) {
            Snackbar.make(findViewById(android.R.id.content), R.string.your_number, Snackbar.LENGTH_SHORT).show();
            binding.InputPhoneNumberSM.setError("This is your number");
            return false;

        } else {
            DBConnection.users.setNumberToReceiver(number);
            DB.retrieveDataTransaction(number);

            binding.InputPhoneNumberSM.setError(null);
            return true;
        }
    }

    private boolean checkMoneyAvailable() {
        String money = moneyToSend.getText().toString();
        String moneyDB = DBConnection.users.getMoney();

        int moneyInt = Integer.parseInt(money);
        int moneyDBInt = Integer.parseInt(moneyDB);

        if(moneyInt > moneyDBInt) {
            Snackbar.make(findViewById(android.R.id.content), R.string.insufficient_money, Snackbar.LENGTH_SHORT).show();
            binding.InputMoneyToSendSM.setError("Insufficient money");
            return false;
        } else {
            return true;
        }
    }

    private boolean moneyCorrect() {
        String money = moneyToSend.getText().toString();

        if(money.isEmpty()) {
            binding.InputMoneyToSendSM.setError("Field cannot be empty");
            return false;

        } else if(!checkMoneyAvailable()) {
            return false;
        }else {
            return true;
        }
    }

    private boolean validateData() {
        Boolean[] result = new Boolean[] {numberCorrect(), moneyCorrect()};

        return !ArraysKt.contains(result, false);
    }

    //Method to make the transaction

    public void makeTransaction() {
        String money = moneyToSend.getText().toString();
        String moneyDB = DBConnection.users.getMoney();

        int moneyInt = Integer.parseInt(money);
        int moneyDBInt = Integer.parseInt(moneyDB);

        if(checkMoneyAvailable() == true) {
            int remainingMoney = moneyDBInt - moneyInt;

            String moneyOfPersonToTransaction = DBConnection.users.getMoneyTransaction();
            int moneyOPTT = Integer.parseInt(moneyOfPersonToTransaction);

            int addMoneyOfTransaction = moneyOPTT + moneyInt;

            Boolean consult = DB.setRemainingMoney(remainingMoney);
            Boolean consult2 = DB.setIncomingMoney(addMoneyOfTransaction);

            if(consult == true && consult2 == true) {
                Toast.makeText(SendMoney.this, R.string.transaction_success, Toast.LENGTH_SHORT).show();

                DB.insertDataHistory(DBConnection.users.getNumber(), DBConnection.users.getName(), DBConnection.users.getNumberToReceiver(), DBConnection.users.getNameToReceiver(), money);
                DB.retrieveData(DBConnection.users.getNumber());
                textViewMoney.setText(DBConnection.users.getMoney());
            } else {
                Toast.makeText(SendMoney.this, R.string.transaction_failed, Toast.LENGTH_SHORT).show();
            }

            clearFields();
        }
    }

    //Method to clear fields

    public void clearFields() {
        binding.InputMoneyToSendSM.setError(null);
        binding.InputPhoneNumberSM.setError(null);
        moneyToSend.setText("");
        phoneNumber.setText("");
    }
}