package com.hoversoftsoln.esta_fort.booking;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hoversoftsoln.esta_fort.R;
import com.hoversoftsoln.esta_fort.core.BaseActivity;
import com.hoversoftsoln.esta_fort.core.data.Driver;
import com.hoversoftsoln.esta_fort.core.data.EstaUser;
import com.hoversoftsoln.esta_fort.profile.ProfileActivity;
import com.hoversoftsoln.esta_fort.profile.ProfileViewModel;
import com.hoversoftsoln.esta_fort.request.RequestsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hoversoftsoln.esta_fort.utils.Constants.PROFILE_CHECK;

public class DriverActivity extends BaseActivity {

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recycler_drivers)
    RecyclerView driverRv;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.view_empty)
    LinearLayout emptyView;

    @BindView(R.id.parent)
    RelativeLayout parentLayout;

    private DriverViewModel driverViewModel;
    private ProfileViewModel profileViewModel;
    private EstaUser estaUser;

    private DriverAdapter driverAdapter;
    private ProgressDialog dialog;
    private AlertDialog successDIalog, profileCheck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Placing Request");
        dialog.setMessage("Please wait while your request is processed");

        driverViewModel = ViewModelProviders.of(this).get(DriverViewModel.class);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        init();

        driverViewModel.loadingService().observe(this, loading -> refreshLayout.setRefreshing(loading));
        profileViewModel.loadingService().observe(this, loading -> refreshLayout.setRefreshing(loading));

        driverViewModel.getDrivers().observe(this, drivers -> {
            if (drivers == null || drivers.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
                driverRv.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                driverRv.setVisibility(View.VISIBLE);
                this.driverAdapter.setDriverList(drivers);
            }
        });

        profileViewModel.getUserAccount().observe(this, user -> estaUser = user);

        driverViewModel.requestService().observe(this, data -> {
            if (data.first) {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            } else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (data.second) {
                    if (successDIalog == null) {
                        successDIalog = new AlertDialog.Builder(this)
                                .setTitle("That was great !")
                                .setMessage("Your request has been placed successfully.\n\nWe will keep you alert on your request status via notifications.\n\nView your request status now")
                                .setPositiveButton("VIEW", (dialog, which) -> {
                                    dialog.dismiss();
                                    startRequestActivity();
                                })
                                .create();
                    }
                    if (!successDIalog.isShowing()) {
                        successDIalog.show();
                    }
                } else {
                    Toast.makeText(this, "An error occurred. Try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startRequestActivity() {
        Intent intent = new Intent(this, RequestsActivity.class);
        startActivity(intent);
        finish();
    }

    private void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refreshLayout.setOnRefreshListener(() -> driverViewModel.getDrivers().observe(this, drivers -> this.driverAdapter.setDriverList(drivers)));

        driverRv.setHasFixedSize(true);
        driverRv.setLayoutManager(new LinearLayoutManager(this));

        driverAdapter = new DriverAdapter(this);
        driverAdapter.setHasStableIds(true);
        driverRv.setAdapter(driverAdapter);


        driverAdapter.setOnDriverClick(driver -> {
            if (isEmpty(estaUser)) {
                showDetailDialog();
                return;
            }

            driverViewModel.onDriverClick(DriverActivity.this, driver);

        });
    }

    private void showDetailDialog() {
        if (profileCheck == null) {
           profileCheck = new AlertDialog.Builder(DriverActivity.this)
                    .setTitle("Request Info")
                    .setMessage("We couldn't validate your profile details. Go Check your Profile Details now")
                    .setPositiveButton("YES", (dialogInterface, i) -> startProfileActivity())
                    .setNegativeButton("CANCEL", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        finish();
                    }).create();
        }

        if (!profileCheck.isShowing()) {
            profileCheck.show();
        }
    }

    private void startProfileActivity() {
        Intent intent = new Intent(DriverActivity.this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("TASK", PROFILE_CHECK);
        startActivityForResult(intent, PROFILE_CHECK);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PROFILE_CHECK) {
            if (resultCode == RESULT_OK){
                Snackbar.make(parentLayout, "Profile has been successfully validated. You can request now", Snackbar.LENGTH_SHORT).show();
            }else {
                Snackbar.make(parentLayout, "Profile validation completed with errors. Check and Try again", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private boolean isEmpty(EstaUser estaUser) {
        if (estaUser == null) {
            return true;
        } else return estaUser.getUsername().trim().isEmpty() ||
                estaUser.getEmail().trim().isEmpty() ||
                estaUser.getTelephone().trim().isEmpty() ||
                estaUser.getLocation().trim().isEmpty();

    }
}
