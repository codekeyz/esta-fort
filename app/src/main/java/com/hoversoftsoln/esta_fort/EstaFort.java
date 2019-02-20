package com.hoversoftsoln.esta_fort;

import android.app.Application;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import timber.log.Timber;

import static timber.log.Timber.DebugTree;

public class EstaFort extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

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
