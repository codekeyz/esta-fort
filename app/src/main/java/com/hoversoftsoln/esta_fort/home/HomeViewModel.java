package com.hoversoftsoln.esta_fort.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.hoversoftsoln.esta_fort.core.data.Service;
import com.hoversoftsoln.esta_fort.utils.FabMenuAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Service>> services;
    private MutableLiveData<Boolean> loadingService;

    private FabMenuAdapter fabMenuAdapter;
    private CollectionReference servicesCollection;
    private ListenerRegistration servicesRegistration;

    public HomeViewModel() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        this.servicesCollection = db.collection("Services");
        fabMenuAdapter = new FabMenuAdapter();
    }

    public LiveData<List<Service>> getServices() {
        if (services == null) {
            services = new MutableLiveData<>();
            if (loadingService == null) {
                loadingService = new MutableLiveData<>();
            }
            loadServices();
        }
        return services;
    }

    public LiveData<Boolean> loadingService() {
        if (loadingService == null) {
            loadingService = new MutableLiveData<>();
        }
        return loadingService;
    }

    private void loadServices() {
        this.loadingService.postValue(true);
        List<Service> serviceList = new ArrayList<>();
        this.servicesRegistration = this.servicesCollection.addSnapshotListener((queryDocumentSnapshots, e) -> {
            this.loadingService.postValue(false);
            if (queryDocumentSnapshots != null) {
                for (DocumentSnapshot d :
                        queryDocumentSnapshots.getDocuments()) {
                    Service s = d.toObject(Service.class);
                    serviceList.add(s);
                }
                this.services.postValue(serviceList);
            }
        });
    }

    public FabMenuAdapter getFabMenuAdapter() {
        return fabMenuAdapter;
    }

    @Override
    protected void onCleared() {
        if (this.servicesRegistration != null) {
            this.servicesRegistration.remove();
        }
    }
}
