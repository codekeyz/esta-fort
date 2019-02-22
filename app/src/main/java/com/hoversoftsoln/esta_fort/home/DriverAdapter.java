package com.hoversoftsoln.esta_fort.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hoversoftsoln.esta_fort.R;
import com.hoversoftsoln.esta_fort.core.data.Driver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverViewHolder> {

    private List<Driver> driverList;
    private Context context;
    private OnDriverClick onDriverClick;

    public DriverAdapter(Context context) {
        this.context = context;
        driverList = new ArrayList<>();
    }

    public void setDriverList(List<Driver> driverList) {
        this.driverList = driverList;
        notifyDataSetChanged();
    }

    public void setOnDriverClick(OnDriverClick onDriverClick) {
        this.onDriverClick = onDriverClick;
    }

    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.driver_list, viewGroup, false);
        return new DriverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder driverViewHolder, int i) {
        driverViewHolder.bind(driverList.get(i));
    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    class DriverViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.driver_image)
        CircleImageView driverImage;

        @BindView(R.id.driverName)
        TextView driverName;

        @BindView(R.id.driverRating)
        MaterialRatingBar driverRating;

        @BindView(R.id.driverRatingText)
        TextView driverRatingText;

        @BindView(R.id.callDriver)
        ImageView callDriver;

        @BindView(R.id.status)
        TextView driverStatus;

        DriverViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Driver driver) {
            Glide.with(context)
                    .load(driver.getImage())
                    .into(driverImage);
            driverName.setText(driver.getUsername());
            driverRating.setRating(driver.getRating());
            driverRatingText.setText(context.getString(R.string.fmt_num_ratings, driver.getRating()));
            driverStatus.setText(status(driver.getStatus()));
            callDriver.setOnClickListener(v -> launchDialer(driver.getTelephone()));


            if (onDriverClick != null) {
                itemView.setOnClickListener(v -> onDriverClick.onclick(driver));
            }
        }

        void launchDialer(String telephone) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + telephone));
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Please set call permissions to allow for this app to make calls", Toast.LENGTH_LONG).show();
                return;
            }
            context.startActivity(intent);
        }

        public String status(int status) {
            String result = "";
            switch (status) {
                case 0 :
                    result = "Not Available";
                    break;
                case 1:
                    result = "Available";
                    break;
                case 2:
                    result = "Ride In Rrogress";
                    break;
            }
            return result;
        }

    }

    public interface OnDriverClick {
        void onclick(Driver driver);
    }
}
