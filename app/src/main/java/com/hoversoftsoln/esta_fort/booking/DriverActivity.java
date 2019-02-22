package com.hoversoftsoln.esta_fort.booking;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.hoversoftsoln.esta_fort.R;
import com.hoversoftsoln.esta_fort.home.DriverAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DriverActivity extends AppCompatActivity {

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recycler_drivers)
    RecyclerView driverRv;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private DriverActivityViewModel driverActivityViewModel;
    private DriverAdapter driverAdapter;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Placing Request");
        dialog.setMessage("Please wait while your request is placed");

        driverActivityViewModel = ViewModelProviders.of(this).get(DriverActivityViewModel.class);
        init();

        driverActivityViewModel.loadingService().observe(this, loading -> refreshLayout.setRefreshing(loading));

        driverActivityViewModel.getDrivers().observe(this, drivers -> this.driverAdapter.setDriverList(drivers));

        driverActivityViewModel.requestService().observe(this, data -> {
            if (data.first) {
                if (!dialog.isShowing()){
                    dialog.show();
                }

                if (data.second != null) {
                    if (data.second.isSuccessful()) {
                        Toast.makeText(this, "Your Request has been placed.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "An error occurred. Try again later", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refreshLayout.setOnRefreshListener(() -> driverActivityViewModel.getDrivers().observe(this, drivers -> this.driverAdapter.setDriverList(drivers)));

        driverRv.setHasFixedSize(true);
        driverRv.setLayoutManager(new LinearLayoutManager(this));

        driverAdapter = new DriverAdapter(this);
        driverAdapter.setHasStableIds(true);
        driverRv.setAdapter(driverAdapter);

        driverAdapter.setOnDriverClick(driver -> driverActivityViewModel.onDriverClick(DriverActivity.this, driver));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
