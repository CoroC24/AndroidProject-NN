package Views.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nonequi.DBConnection;
import com.example.nonequi.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import Views.History.History;
import Views.SendMoney.SendMoney;
import Views.ShowCard.ShowCard;
import Views.Login.Welcome;
import saveInSharedPreferences.KeepNumberManagement;
import saveInSharedPreferences.sessionManagement;


public class Home extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private TextView textViewName, textViewMoney;
    private ImageButton transferMoneyButton, showCardButton, historyButton;

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    DBConnection DB;
    saveInSharedPreferences.sessionManagement sessionManagement;
    KeepNumberManagement keepNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        DB = new DBConnection(this);
        sessionManagement = new sessionManagement(Home.this);
        keepNumber = new KeepNumberManagement(this);

        textViewName = binding.textViewName;
        textViewMoney = binding.textViewMoney;



        MaterialToolbar toolBar = binding.topAppBar;
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment exitDialog = new ExitAlertDialog();
                exitDialog.show(getSupportFragmentManager(), "Exit");
            }
        });

        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.logout) {
                    Intent intent = new Intent(getApplicationContext(), Welcome.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    sessionManagement.removeSession();
                    keepNumber.removeNumberManagement();
                }
                return false;
            }
        });

        //Set actions to buttons

        transferMoneyButton = binding.transferMoney;
        showCardButton = binding.showCard;
        historyButton = binding.histoyTransactions;

        transferMoneyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SendMoney.class);
                startActivity(intent);
            }
        });

        showCardButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShowCard.class);
                startActivity(intent);
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), History.class);
                startActivity(intent);
            }
        });

    }


    protected void onStart() {
        super.onStart();

        String retrieveNumber = keepNumber.getNumberManagement();

        DB.retrieveDate(retrieveNumber);
        DB.retrieveData(retrieveNumber);

        try {
            setMoneyToUser();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        textViewName.setText(DBConnection.users.getName());
        textViewMoney.setText(DBConnection.users.getMoney());
    }

    //Method to give action to back button

    @Override
    public void  onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.back_button_pressed_message, Snackbar.LENGTH_SHORT);

            snackbar.show();
        }

        mBackPressed = System.currentTimeMillis();
    }

    //Method to update money in home

    @Override
    protected void onPostResume() {

        super.onPostResume();
        DB.retrieveData(DBConnection.users.getNumber());
        textViewMoney.setText(DBConnection.users.getMoney());
    }

    //Method to update money

    public void setMoneyToUser() throws ParseException {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate localDate = LocalDate.now();

            String dateDBString = DBConnection.updateMoney.getDateDB();
            LocalDate dateDB = LocalDate.parse(dateDBString, dateFormat);

            Duration diff = Duration.between(localDate.atStartOfDay(), dateDB.atStartOfDay());

            long diffHours = diff.toHours();

            int diffHoursInt = (int)(diffHours);

            diffHoursInt *= -1;

            if (diffHoursInt >= 24) {
                DB.updateMoney();
            }
        }
    }
}