package com.hoversoftsoln.esta_fort.profile;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hoversoftsoln.esta_fort.R;
import com.hoversoftsoln.esta_fort.core.BaseActivity;
import com.hoversoftsoln.esta_fort.data.EstaUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.userEmail)
    TextView userEmail;
    @BindView(R.id.userTelephone)
    TextView userTelephone;
    @BindView(R.id.userLocation)
    TextView userLocation;
    @BindView(R.id.etTelephone)
    EditText etTelephone;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etLocation)
    EditText etLocation;
    @BindView(R.id.updateProfile)
    Button updateProfile;
    @BindView(R.id.saveProfile)
    Button saveProfile;
    private ProfileViewModel profileViewModel;

    @BindView(R.id.editLayout)
    LinearLayout editLayout;

    @BindView(R.id.dataLayout)
    LinearLayout dataLayout;

    @BindView(R.id.loading)
    MaterialProgressBar loader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        setDefaults();

        profileViewModel.loadingService().observe(this, loading -> {
            if (loading) {
                updateProfile.setVisibility(View.GONE);
                loader.setVisibility(View.VISIBLE);
                editLayout.setVisibility(View.GONE);
                dataLayout.setVisibility(View.GONE);
            }else {
                updateProfile.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
                dataLayout.setVisibility(View.VISIBLE);
            }
        });

        profileViewModel.getUserAccount().observe(this, user -> {
            if (user != null) {
                editLayout.setVisibility(View.GONE);
                dataLayout.setVisibility(View.VISIBLE);
                this.username.setText(user.getUsername());
                this.userEmail.setText(user.getEmail());
                this.userTelephone.setText(user.getTelephone());
                this.userLocation.setText(user.getLocation());

                this.etUsername.setText(user.getUsername());
                this.etTelephone.setText(user.getTelephone());
                this.etEmail.setText(user.getEmail());
                this.etLocation.setText(user.getLocation());
            }else {
                editLayout.setVisibility(View.VISIBLE);
            }
        });

        profileViewModel.viewState().observe(this, this::switchState);

        this.updateProfile.setOnClickListener(v -> this.switchState(0));

        this.saveProfile.setOnClickListener(v -> {
            EstaUser estaUser = new EstaUser();
            estaUser.setEmail(etEmail.getText().toString());
            estaUser.setUsername(etUsername.getText().toString());
            estaUser.setLocation(etLocation.getText().toString());
            estaUser.setTelephone(etTelephone.getText().toString());
            this.profileViewModel.updateUserAccount(estaUser);
        });
    }

    private void setDefaults() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {

            if (user.getDisplayName() != null) {
                this.username.setText(user.getDisplayName());
                this.etUsername.setText(user.getDisplayName());
            }

            if (user.getEmail() != null) {
                this.userEmail.setText(user.getEmail());
                this.etEmail.setText(user.getEmail());
            }

            if (user.getPhoneNumber() != null) {
                this.userTelephone.setText(user.getPhoneNumber());
                this.etTelephone.setText(user.getPhoneNumber());
            }
        }
    }

    public void switchState(int state) {
        switch (state) {
            case 0:
                dataLayout.setVisibility(View.GONE);
                editLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                dataLayout.setVisibility(View.VISIBLE);
                editLayout.setVisibility(View.GONE);
                break;
        }
    }

}
