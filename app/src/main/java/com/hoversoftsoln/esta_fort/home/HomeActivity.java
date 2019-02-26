package com.hoversoftsoln.esta_fort.home;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.hoversoftsoln.esta_fort.R;
import com.hoversoftsoln.esta_fort.booking.DriverActivity;
import com.hoversoftsoln.esta_fort.core.BaseActivity;
import com.hoversoftsoln.esta_fort.profile.ProfileActivity;
import com.hoversoftsoln.esta_fort.request.RequestsActivity;
import com.hoversoftsoln.esta_fort.splash.SplashActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.loading)
    MaterialProgressBar loader;

    @BindView(R.id.recycler_services)
    RecyclerView servicesRV;

    private HomeViewModel homeViewModel;
    private ServiceAdapter serviceAdapter;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        init();

        homeViewModel.loadingService().observe(this, isloading -> {
            if (isloading) {
                this.loader.setVisibility(View.VISIBLE);
            }else {
                this.loader.setVisibility(View.GONE);
            }
        });

        homeViewModel.getServices().observe(this, data -> {
            this.serviceAdapter.setServiceList(data);
        });
    }

    private void init() {
        // RecyclerView
        servicesRV.setHasFixedSize(true);
        servicesRV.setLayoutManager(new LinearLayoutManager(this));
        serviceAdapter = new ServiceAdapter(this);
        servicesRV.setAdapter(serviceAdapter);

        serviceAdapter.setOnServiceClickListener(service -> {
            if (service.getType() == 100){
                Intent intent = new Intent(this, DriverActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(this, "Service not yet Supported", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile:
                openProfile();
                break;
            case R.id.menu_rides:
                openRides();
                break;
            case R.id.menu_sign_out:
                signout();
                break ;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signout() {
        if (dialog == null) {
         dialog =new AlertDialog.Builder(this)
                    .setTitle("Sign Out")
                    .setMessage("Are you sure you want to sign out ?")
                    .setPositiveButton("YES", (dialog, which) -> {
                        AuthUI.getInstance().signOut(HomeActivity.this);
                        dialog.dismiss();
                        Intent iii = new Intent(HomeActivity.this, SplashActivity.class);
                        iii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(iii);
                    }).setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss())
                    .create();
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void openRides() {
        Intent intent = new Intent(this, RequestsActivity.class);
        startActivity(intent);
    }

    private void openProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
