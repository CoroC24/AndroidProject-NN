package Views.SendMoney;

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

import com.example.nonequi.Users;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import kotlin.collections.ArraysKt;

public class SendMoney extends AppCompatActivity {

    private ActivitySendMoneyBinding binding;
    private TextView textViewMoney;
    public EditText phoneNumber, moneyToSend;
    public String date;

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

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(calendar.getTime());
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
            binding.InputMoneyToSendSM.setError(null);
            return true;
        }
    }

    private boolean moneyCorrect() {
        String money = moneyToSend.getText().toString();

        if(money.isEmpty()) {
            binding.InputMoneyToSendSM.setError("Field cannot be empty");
            return false;

        } else if(money.startsWith("0")) {
            Snackbar.make(findViewById(android.R.id.content), R.string.send_cero, Snackbar.LENGTH_SHORT).show();
            binding.InputMoneyToSendSM.setError("Do you want to send 0$?");
            return false;

        } else if(moneyStringToInt() < 2500) {
            Snackbar.make(findViewById(android.R.id.content), R.string.minimum_transaction, Snackbar.LENGTH_SHORT).show();
            binding.InputMoneyToSendSM.setError("Minimum transaction: 2500$");
            return false;

        } else if(money.length() > 9) {
            Snackbar.make(findViewById(android.R.id.content), R.string.many_money, Snackbar.LENGTH_SHORT).show();
            binding.InputMoneyToSendSM.setError("Do you have that much money?");
            return false;

        } else if(!checkMoneyAvailable()) {
            return false;
        }else {
            binding.InputMoneyToSendSM.setError(null);
            return true;
        }
    }

    private int moneyStringToInt() {
        String money = moneyToSend.getText().toString();

        int moneyInt = 0;

        if (!money.isEmpty()) {
            moneyInt = Integer.parseInt(money);
        }
        return moneyInt;
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
            int remainingMoney = (moneyDBInt - moneyInt) - 2000;

            String moneyOfPersonToTransaction = DBConnection.users.getMoneyTransaction();
            int moneyOPTT = Integer.parseInt(moneyOfPersonToTransaction);

            //The variable moneyOPTT is a abbreviation of Money of person to transaction
            int addMoneyOfTransaction = moneyOPTT + moneyInt;

            Boolean consult = DB.setRemainingMoney(remainingMoney);
            Boolean consult2 = DB.setIncomingMoney(addMoneyOfTransaction);

            if(consult == true && consult2 == true) {
                Toast.makeText(SendMoney.this, R.string.transaction_success, Toast.LENGTH_SHORT).show();

                DB.insertDataHistory(DBConnection.users.getNumber(), DBConnection.users.getName(), DBConnection.users.getNumberToReceiver(), DBConnection.users.getNameToReceiver(), money, date);
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