package com.hoversoftsoln.esta_fort.request;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hoversoftsoln.esta_fort.R;
import com.hoversoftsoln.esta_fort.core.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RequestsActivity extends BaseActivity {

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recycler_requests)
    RecyclerView requestsRV;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.view_empty)
    LinearLayout emptyView;

    private RequestAdapter requestAdapter;
    private RequestsViewModel requestsViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        requestsViewModel = ViewModelProviders.of(this).get(RequestsViewModel.class);

        init();

        requestsViewModel.loadingService().observe(this, loading -> refreshLayout.setRefreshing(loading));

        requestsViewModel.getRequests().observe(this, requests ->  {
            if (requests == null || requests.isEmpty()){
                emptyView.setVisibility(View.VISIBLE);
                requestsRV.setVisibility(View.GONE);
            }else {
                Toast.makeText(this, requests.toArray().toString(), Toast.LENGTH_SHORT).show();
                emptyView.setVisibility(View.GONE);
                requestsRV.setVisibility(View.VISIBLE);
                requestAdapter.setRequestList(requests);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refreshLayout.setOnRefreshListener(() -> requestsViewModel.getRequests().observe(this, requests -> {
            if (requests == null || requests.isEmpty()){
                emptyView.setVisibility(View.VISIBLE);
                requestsRV.setVisibility(View.GONE);
            }else {
                Toast.makeText(this, requests.toArray().toString(), Toast.LENGTH_SHORT).show();
                emptyView.setVisibility(View.GONE);
                requestsRV.setVisibility(View.VISIBLE);
                requestAdapter.setRequestList(requests);
            }
        }));

        requestsRV.setHasFixedSize(true);
        requestsRV.setLayoutManager(new LinearLayoutManager(this));

        requestAdapter = new RequestAdapter();
        requestsRV.setAdapter(requestAdapter);
    }


}
