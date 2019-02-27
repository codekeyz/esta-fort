package com.hoversoftsoln.esta_fort.request;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.hoversoftsoln.esta_fort.R;
import com.hoversoftsoln.esta_fort.core.data.Request;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private List<Request> requestList;
    private Activity activity;

    public RequestAdapter(Activity activity) {
        this.activity = activity;
        this.requestList = new ArrayList<>();
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_request, viewGroup, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder requestViewHolder, int i) {
        requestViewHolder.bind(requestList.get(i));
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.request_time)
        RelativeTimeTextView timePlaced;

        @BindView(R.id.status)
        TextView status;

        @BindView(R.id.driverName)
        TextView driverName;

        @BindView(R.id.emailthread_item_avatar)
        ImageView callbtn;

        RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Request request) {
            timePlaced.setReferenceTime(request.getDateCreated());
            status.setText(getStatus(request.getStatus()));
            driverName.setText(request.getDriverName());

            callbtn.setOnClickListener(v -> {
                if (!request.getIscancelled() || !request.getIscompleted()) {
                    launchDialer(request.getDriverTelephone());
                }else {
                    Toast.makeText(activity, "You can't place a call to this Driver right now.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        void launchDialer(String telephone) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telephone));
            activity.startActivity(intent);
        }


        String getStatus(int status) {
            String result = "";
            switch (status){
                case -1:
                    result = "Cancelled";
                    break;
                case 0:
                    result = "Placed";
                    break;
                case 1:
                    result = "Confirmed";
                    break;
                case 2:
                    result = "Driver has arrived";
                    break;
                case 3:
                    result = "In Progress";
                    break;
                case 4:
                    result = "Completed";
                    break;
            }
            return result;
        }
    }
}
