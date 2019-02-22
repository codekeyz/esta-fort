package com.hoversoftsoln.esta_fort.home;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hoversoftsoln.esta_fort.R;
import com.hoversoftsoln.esta_fort.utils.FabMenuAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import uk.co.markormesher.android_fab.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab_menu)
    FloatingActionButton fabLayout;

    @BindView(R.id.loading)
    MaterialProgressBar loader;

    @BindView(R.id.recycler_services)
    RecyclerView servicesRV;

    private HomeViewModel homeViewModel;
    private ServiceAdapter serviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        init();

        homeViewModel.loadingService().observe(this, isloading -> {
            if (isloading.booleanValue()) {
                this.loader.setVisibility(View.VISIBLE);
            }else {
                this.loader.setVisibility(View.GONE);
            }
        });

        homeViewModel.getServices().observe(this, data -> {
            this.serviceAdapter.setServiceList(data);
            this.serviceAdapter.notifyDataSetChanged();
        });
    }

    private void init() {
        // RecyclerView
        servicesRV.setHasFixedSize(true);
        servicesRV.setLayoutManager(new LinearLayoutManager(this));
        serviceAdapter = new ServiceAdapter(this);
        servicesRV.setAdapter(serviceAdapter);

        // Fab Menu
        fabLayout.setContentCoverEnabled(true);
        fabLayout.setContentCoverColour(Color.argb(80, 0, 0, 0));
        fabLayout.setSpeedDialMenuAdapter(new FabMenuAdapter());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
