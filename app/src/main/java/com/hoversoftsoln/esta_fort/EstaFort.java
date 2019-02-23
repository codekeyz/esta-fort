package com.hoversoftsoln.esta_fort;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import timber.log.Timber;

import static timber.log.Timber.DebugTree;

public class EstaFort extends Application {

    private static final String TAG = EstaFort.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();

        FirebaseFirestore.getInstance().setFirestoreSettings(settings);

        if (BuildConfig.DEBUG) {

            // Create a Remote Config Setting to enable developer mode, which you can use to increase
            // the number of fetches available per hour during development. See Best Practices in the
            // README for more information.
            // [START enable_dev_mode]
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
                    .build();
            FirebaseRemoteConfig.getInstance().setConfigSettings(configSettings);

            FirebaseRemoteConfig.getInstance().setDefaults(R.xml.remote_config_defaults);

            Timber.plant(new DebugTree());
        }
    }
}
