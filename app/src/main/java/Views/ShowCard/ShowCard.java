package Views.ShowCard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.nonequi.DBConnection;
import com.google.android.material.appbar.MaterialToolbar;

import java.time.*;

public class ShowCard extends AppCompatActivity {

    private ActivityShowCardBinding binding;
    private TextView serial, name, month, year, cvc;
    private int rangeMonth, maxMonth, minMonth, rangeYear, minYear, maxYear, currentYear, rangeSerial, minSerial, maxSerial, rangeCVC, maxCVC, minCVC;

    CardCredentials cardCredentials;
    DBConnection DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowCardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        serial = binding.textViewSerial1;
        cvc = binding.textViewCVC;
        name = binding.textViewNameCard;
        month = binding.textViewMonth;
        year = binding.textViewYear;

        cardCredentials = new CardCredentials();
        DB = new DBConnection(this);

        creditCardCredentials();
        retrieveCardCredentials();

        MaterialToolbar toolBar = binding.topAppBarSC;
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //Method to give action to back button

    @Override
    public void  onBackPressed() {
        finish();
    }


    // ----------------------------------------------------------------

    public void creditCardCredentials() {
        maxMonth = 12;
        minMonth = 1;
        rangeMonth = maxMonth - minMonth + 1;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int currentYearNow = LocalDate.now().getYear();
            currentYear = currentYearNow - 2000;
        } else {
            currentYear = 22;
        }

        minYear = currentYear;
        maxYear = currentYear + 10;
        rangeYear = maxYear - minYear + 1;

        int monthRandom = ((int) (Math.random() * rangeMonth) + minMonth);
        int yearRandom = ((int)(Math.random() * rangeYear) + minYear);

        String monthRandomString = String.valueOf(monthRandom);
        String yearRandomString = String.valueOf(yearRandom);

        name.setText(DBConnection.users.getName());
        month.setText(monthRandomString) ;
        year.setText(yearRandomString);


        minSerial = 1000;
        maxSerial = 9999;
        rangeSerial = maxSerial - minSerial + 1;

        String serial1Random = String.valueOf((int)(Math.random() * rangeSerial) + minSerial);
        String serial2Random = String.valueOf((int)(Math.random() * rangeSerial) + minSerial);
        String serial3Random = String.valueOf((int)(Math.random() * rangeSerial) + minSerial);
        String serial4Random = String.valueOf((int)(Math.random() * rangeSerial) + minSerial);

        String completeSerial = serial1Random + "  " + serial2Random + "  " + serial3Random + "  " + serial4Random;

        minCVC = 100;
        maxCVC = 999;
        rangeCVC = maxCVC - minCVC + 1;

        String CVCRandom = String.valueOf((int)(Math.random() * rangeCVC) + minCVC);

        Boolean checkUserHaveCard = DB.checkIfUserHaveCreditCard(DBConnection.users.getNumber());

        if(checkUserHaveCard == false) {
            DB.insertDataCreditCard(DBConnection.users.getNumber(), DBConnection.users.getName(), completeSerial, CVCRandom, monthRandomString, yearRandomString);
        }
    }

    public void retrieveCardCredentials(){
        DB.retrieveDataOfCreditCard(DBConnection.users.getNumber());

        name.setText(DBConnection.users.getName());
        serial.setText(DBConnection.cardCredentials.getSerial());
        cvc.setText(DBConnection.cardCredentials.getCvc());
        month.setText(DBConnection.cardCredentials.getMonth());
        year.setText(DBConnection.cardCredentials.getYear());
    }
}