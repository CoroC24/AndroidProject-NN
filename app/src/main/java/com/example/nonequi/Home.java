package com.example.nonequi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.nonequi.databinding.ActivityHomeBinding;
import com.google.android.material.appbar.MaterialToolbar;


public class Home extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SwipeRefreshLayout swipeRefresh = binding.swipeRefresh;

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                binding.swipeRefresh.isRefreshing();
            }
        });

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
                if(item.getItemId() == R.id.more) {
                    Intent intent = new Intent(getApplicationContext(), Welcome.class);
                    startActivity(intent);
                }
                return false;
            }
        });

    }



    @Override
    public void  onBackPressed() {
        /*DialogFragment exitDialog = new ExitAlertDialog();
        exitDialog.show(getSupportFragmentManager(), "Exit");*/

        finishAffinity();
    }
}