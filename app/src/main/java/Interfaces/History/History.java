package Interfaces.History;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.nonequi.DBConnection;
import com.example.nonequi.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    List<HistoryTransactionList> elements;

    DBConnection DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        DB = new DBConnection(this);

        init();

        MaterialToolbar toolBar = findViewById(R.id.topAppBarH);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void init() {
        elements = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewHistory);
        HistoryTransactionListAdapter listAdapter = new HistoryTransactionListAdapter(elements, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Consult to DB

        Cursor cursor = DB.retrieveDataToHistory();
        if(cursor.getCount() == 0) {
            Toast.makeText(this, "Data not found", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                String numberSender = cursor.getString(0);
                String nameSender = cursor.getString(1);
                String numberReceiver = cursor.getString(2);
                String nameReceiver = cursor.getString(3);
                String money = cursor.getString(4);
                String date = cursor.getString(5);

                elements.add(new HistoryTransactionList(numberSender, nameSender, numberReceiver, nameReceiver, money, date));
            }
        }
    }


}