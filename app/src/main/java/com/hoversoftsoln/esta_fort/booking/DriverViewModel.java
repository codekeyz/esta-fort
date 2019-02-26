package com.hoversoftsoln.esta_fort.booking;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Pair;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.hoversoftsoln.esta_fort.core.data.Driver;
import com.hoversoftsoln.esta_fort.core.data.Request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DriverViewModel extends ViewModel {

    private MutableLiveData<List<Driver>> drivers;
    private MutableLiveData<Boolean> loadingService;
    private MutableLiveData<Pair<Boolean, Boolean>> requestService;
    private AlertDialog dialog;
    private Query driversCollection;
    private CollectionReference requestsCollection;
    private ListenerRegistration driversRegistration;
    private Driver savedDriver;

    public DriverViewModel() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        this.driversCollection = db.collection("Drivers").whereEqualTo("status", 1);
        this.requestsCollection = db.collection("Requests");
    }

    LiveData<List<Driver>> getDrivers() {
        if (drivers == null) {
            drivers = new MutableLiveData<>();
            if (loadingService == null) {
                loadingService = new MutableLiveData<>();
            }
            loadDrivers();
        }
        return drivers;
    }

    LiveData<Boolean> loadingService() {
        if (loadingService == null) {
            loadingService = new MutableLiveData<>();
        }
        return loadingService;
    }

    LiveData<Pair<Boolean, Boolean>> requestService() {
        if (requestService == null) {
            requestService = new MutableLiveData<>();
        }
        return requestService;
    }

    private void loadDrivers() {
        this.loadingService.postValue(true);
        List<Driver> driverList = new ArrayList<>();
        this.driversRegistration = this.driversCollection.addSnapshotListener((qdocs, e) -> {
            this.loadingService.postValue(false);
            if (qdocs != null) {
                driverList.clear();
                for (DocumentSnapshot d :
                        qdocs.getDocuments()) {
                    Driver driver = d.toObject(Driver.class);
                    if (driver != null) {
                        driver.setId(d.getId());
                        driverList.add(driver);
                    }
                }
                this.drivers.postValue(driverList);
            }
        });
    }

    private void sendRequest(Request request) {
        this.requestService.postValue(new Pair<>(true, false));
        request.setDateCreated(new Date().getTime());
        requestsCollection.add(request).addOnCompleteListener(task -> requestService.postValue(new Pair<>(false, task.isSuccessful())));
    }

    @Override
    protected void onCleared() {
        if (this.driversRegistration != null) {
            this.driversRegistration.remove();
        }
    }

    void onDriverClick(Activity context, Driver driver) {
        this.savedDriver = driver;
        if (dialog != null) {
            dialog = null;
        }
            dialog = new AlertDialog.Builder(context)
                    .setTitle("Request Shuttle")
                    .setMessage("You are requesting a shuttle. " + driver.getUsername())
                    .setPositiveButton("Proceed", (dialog, which) -> {
                        Request request = new Request();
                        request.setDriverName(driver.getUsername());
                        request.setUserID(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        request.setDriverID(driver.getId());
                        request.setDateCreated(new Date().getTime());
                        request.setStatus(0);
                        sendRequest(request);
                        savedDriver = null;
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        savedDriver = null;
                        dialog.cancel();
                    })
                    .create();

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    void continueBooking(Activity context){
        onDriverClick(context, savedDriver);
    }
}
