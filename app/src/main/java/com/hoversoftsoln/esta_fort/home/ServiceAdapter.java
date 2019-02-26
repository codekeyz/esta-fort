package com.hoversoftsoln.esta_fort.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hoversoftsoln.esta_fort.R;
import com.hoversoftsoln.esta_fort.core.data.Service;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private List<Service> serviceList;
    private Context context;

    private OnServiceClickListener onServiceClickListener;

    ServiceAdapter(Context context) {
        this.context = context;
        this.serviceList = new ArrayList<>();
        this.setHasStableIds(true);
    }

    public void setOnServiceClickListener(OnServiceClickListener onServiceClickListener) {
        this.onServiceClickListener = onServiceClickListener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_service, viewGroup, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder viewHolder, int position) {
        viewHolder.bind(serviceList.get(position));
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
        notifyDataSetChanged();
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.service_image)
        ImageView image;

        @BindView(R.id.service_item_name)
        TextView title;

        @BindView(R.id.service_item_rating)
        MaterialRatingBar rating;

        @BindView(R.id.service_item_num_rating)
        TextView numrating;

        @BindView(R.id.service_item_desc)
        TextView desc;

        private View view;

        ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, view);
        }

        void bind(Service service) {
            title.setText(service.getTitle());
            desc.setText(service.getDesc());
            Glide.with(context)
                    .load(service.getImage())
                    .into(image);
            numrating.setText(context.getString(R.string.fmt_num_ratings, service.getRating()));
            rating.setRating((float) service.getRating());

            this.view.setOnClickListener(v -> {
                if (onServiceClickListener != null) {
                    onServiceClickListener.onclicK(service);
                }
            });
        }
    }

    public interface OnServiceClickListener {
        void onclicK(Service service);
    }
}
