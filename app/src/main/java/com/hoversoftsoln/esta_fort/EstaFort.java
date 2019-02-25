package com.hoversoftsoln.esta_fort;

import android.app.Application;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import timber.log.Timber;

import static timber.log.Timber.DebugTree;

public class EstaFort extends Application {

    private static final String TAG = EstaFort.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {

            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build();

            FirebaseFirestore.getInstance().setFirestoreSettings(settings);

            Timber.plant(new DebugTree());
        }
    }
}
