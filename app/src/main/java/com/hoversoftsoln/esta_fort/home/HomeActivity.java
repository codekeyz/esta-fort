package com.hoversoftsoln.esta_fort.home;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hoversoftsoln.esta_fort.R;
import com.hoversoftsoln.esta_fort.core.data.EstaUser;
import com.hoversoftsoln.esta_fort.splash.SplashViewModel;
import com.hoversoftsoln.esta_fort.utils.FabMenuAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import uk.co.markormesher.android_fab.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab_menu)
    FloatingActionButton fabLayout;

    private HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        EstaUser estaUser = new EstaUser();
        estaUser.setUsername("Lomotey Edwin");
        estaUser.setEmail("edwin@gmail.com");
        estaUser.setTelephone("0244961291");

        fabLayout.setContentCoverEnabled(true);
        fabLayout.setContentCoverColour(Color.argb(80, 0, 0, 0));
        fabLayout.setSpeedDialMenuAdapter(homeViewModel.getFabMenuAdapter());

        homeViewModel.getServices().observe(this, data ->
                Toast.makeText(this, data.get(0).getTitle(), Toast.LENGTH_SHORT).show());

        homeViewModel.loadingService().observe(this, res -> Timber.d(res  ? "Yes Loading": "Not Loading"));

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
