package com.hoversoftsoln.esta_fort.splash;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hoversoftsoln.esta_fort.R;
import com.hoversoftsoln.esta_fort.core.BaseActivity;
import com.hoversoftsoln.esta_fort.home.HomeActivity;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static com.hoversoftsoln.esta_fort.utils.Constants.RC_SIGN_IN;
import static com.hoversoftsoln.esta_fort.utils.Constants.SPLASH_TIME_OUT;


public class SplashActivity extends BaseActivity {

    @BindView(R.id.progress)
    MaterialProgressBar materialProgressBar;
    @BindView(R.id.parent)
    ConstraintLayout parentLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

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
        intent.setFlags(FLAG_ACTIVITY_SINGLE_TOP);

        startActivityForResult(intent, RC_SIGN_IN);
    }

    private boolean shouldStartSignIn() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return (currentUser == null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.materialProgressBar.setVisibility(View.VISIBLE);

        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK && !shouldStartSignIn()) {
                    startHome();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Snackbar.make(parentLayout,  R.string.sign_in_cancelled, Snackbar.LENGTH_LONG).show();
                    startSignIn();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Snackbar.make(parentLayout, getString(R.string.no_network), Snackbar.LENGTH_LONG).show();
                    startSignIn();
                    return;
                }


                startSignIn();
            }
        }
    }

    private void startHome() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
