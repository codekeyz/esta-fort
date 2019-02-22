package com.hoversoftsoln.esta_fort.splash;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.hoversoftsoln.esta_fort.R;
import com.hoversoftsoln.esta_fort.core.BaseActivity;
import com.hoversoftsoln.esta_fort.home.HomeActivity;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static com.hoversoftsoln.esta_fort.utils.Constants.RC_SIGN_IN;
import static com.hoversoftsoln.esta_fort.utils.Constants.SPLASH_TIME_OUT;


public class SplashActivity extends BaseActivity {

    @BindView(R.id.progress)
    MaterialProgressBar materialProgressBar;
    private SplashViewModel splashViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        // View model
        splashViewModel = ViewModelProviders.of(this).get(SplashViewModel.class);

        new Handler().postDelayed(() -> {

            materialProgressBar.setVisibility(View.GONE);

            if (shouldStartSignIn()) {
                startSignIn();
            } else {
                startHome();
            }
        }, SPLASH_TIME_OUT);

    }

    private void startSignIn() {
        // Sign in with FirebaseUI
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.PhoneBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()
                ))
                .setLogo(R.mipmap.ic_launcher)
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);
        splashViewModel.setIsSigningIn(true);
    }

    private boolean shouldStartSignIn() {
        return (!splashViewModel.IsSigningIn() && FirebaseAuth.getInstance().getCurrentUser() == null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            splashViewModel.setIsSigningIn(false);

            if (resultCode != RESULT_OK && shouldStartSignIn()) {
                startSignIn();
            } else {
                startHome();
            }
        }
    }

    private void startHome() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
