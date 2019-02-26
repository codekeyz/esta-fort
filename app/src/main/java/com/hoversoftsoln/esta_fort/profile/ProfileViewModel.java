package com.hoversoftsoln.esta_fort.profile;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;
import com.hoversoftsoln.esta_fort.core.data.EstaUser;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<EstaUser> estaUser;
    private MutableLiveData<Boolean> loadingService;

    private DocumentReference userRefence;
    private ListenerRegistration userDocListenerReg;

    public ProfileViewModel() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        this.userRefence = db.collection("Users").document(uid);
    }

    public LiveData<EstaUser> getUserAccount() {
        if (estaUser == null) {
            estaUser = new MutableLiveData<>();
            if (loadingService == null) {
                loadingService = new MutableLiveData<>();
            }
            loadEstaUser();
        }
        return estaUser;
    }

    public LiveData<Boolean> loadingService() {
        if (loadingService == null) {
            loadingService = new MutableLiveData<>();
        }
        return loadingService;
    }

    private void loadEstaUser() {
        this.loadingService.postValue(true);
        this.userDocListenerReg = this.userRefence.addSnapshotListener((documentSnapshot, e) -> {
            this.loadingService.postValue(false);
            if (documentSnapshot != null) {
                EstaUser user= documentSnapshot.toObject(EstaUser.class);
                if (user != null) {
                    this.estaUser.postValue(user);
                }
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.userDocListenerReg.remove();
    }

    void updateUserAccount(EstaUser estaUser) {
        this.loadingService.postValue(true);
        Map<String, Object> datamap = new HashMap<>();
        datamap.put("username", estaUser.getUsername());
        datamap.put("email", estaUser.getEmail());
        datamap.put("telephone", estaUser.getTelephone());
        datamap.put("location", estaUser.getLocation());
        userRefence.set(datamap, SetOptions.merge()).addOnCompleteListener(task -> {
            this.loadingService.postValue(false);
            if (task.isSuccessful()){
                this.estaUser.postValue(estaUser);
            }
        });
    }

    void updateUserAccountWithResult(Activity activity, EstaUser estaUser) {
        this.loadingService.postValue(true);
        Map<String, Object> datamap = new HashMap<>();
        datamap.put("username", estaUser.getUsername());
        datamap.put("email", estaUser.getEmail());
        datamap.put("telephone", estaUser.getTelephone());
        datamap.put("location", estaUser.getLocation());
        userRefence.set(datamap, SetOptions.merge()).addOnCompleteListener(task -> {
            this.loadingService.postValue(false);
            if (task.isSuccessful()){
                this.estaUser.postValue(estaUser);
                if (!isEmpty(estaUser)) {
                    activity.setResult(RESULT_OK, new Intent());
                    activity.finish();
                } else {
                    Toast.makeText(activity, "Make sure all data is provided & try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
