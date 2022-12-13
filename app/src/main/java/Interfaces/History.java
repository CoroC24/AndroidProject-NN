package Interfaces;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.nonequi.DBConnection;
import com.example.nonequi.HistoryTransactionList;
import com.example.nonequi.HistoryTransactionListAdapter;
import com.example.nonequi.R;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    List<HistoryTransactionList> elements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        init();
    }

    public void init() {
        elements = new ArrayList<>();
        elements.add(new HistoryTransactionList());

        HistoryTransactionListAdapter listAdapter = new HistoryTransactionListAdapter(elements, this);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }
}