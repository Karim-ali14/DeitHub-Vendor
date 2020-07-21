package com.dopave.diethub_vendor.UI.Subscriptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dopave.diethub_vendor.Adapter.AdapterForSubscription;
import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Subscriptions.Row;
import com.dopave.diethub_vendor.Models.Subscriptions.Subscriptions;
import com.dopave.diethub_vendor.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriptionsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    AdapterForSubscription adapter;
    SubscriptionsViewModel viewModel;
    ProgressDialog dialog;
    TextView title;
    public static final int limit = 5 ;
    int skip = 0;
    int count;
    boolean isScrolling = false;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);
        viewModel = ViewModelProviders.of(this).get(SubscriptionsViewModel.class);
        dialog = new ProgressDialog(this);
        dialog.show();
        recyclerView = findViewById(R.id.recyclerViewSub);
        manager = new LinearLayoutManager(this);
        title = findViewById(R.id.title);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        progressBar = findViewById(R.id.progressBarSub);
        if (getIntent().getExtras().getString("type").equals("nav_mySubscriptionOrders")) {
            getAllSub(0, "pending");
            title.setText(getResources().getString(R.string.subscriptionOrders));
        }
        else {
            getAllSub(1, "approved");
            title.setText(getResources().getString(R.string.subscription));
        }
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isScrolling = true;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int childCount = manager.getChildCount();
                int itemCount = manager.getItemCount();
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();

                if (isScrolling && (childCount+firstVisibleItemPosition == itemCount) && itemCount < count){
                    progressBar.setVisibility(View.VISIBLE);
                    isScrolling = false;
                    progressBar.setVisibility(View.VISIBLE);
                    if (getIntent().getExtras().getString("type").equals("nav_mySubscriptionOrders")) {
                        fetchData(0, "pending");
                    }
                    else {
                        fetchData(1, "approved");
                    }
                }
            }
        });
    }

    public void BackButton(View view) {
        finish();
    }

    private void getAllSub(final int type, final String status){
        viewModel.getAllSubscriptions(this,dialog,viewModel,type,status,limit,skip).observe(this,
                new Observer<Subscriptions>() {
            @Override
            public void onChanged(Subscriptions subscriptions) {
                count = subscriptions.getData().getCount();
                setListOfSubscription(type,subscriptions.getData().getRows(),viewModel,status);
            }
        });
    }

    public void setListOfSubscription(int type, List<Row> list,
                                      SubscriptionsViewModel viewModel,String status){
        if (list.size() != 0) {
            adapter = new AdapterForSubscription(list,
                    SubscriptionsActivity.this, type, viewModel,dialog,status,recyclerView);
            recyclerView.setAdapter(adapter);
        }else {
            Toast.makeText(this, R.string.no_Subscription, Toast.LENGTH_SHORT).show();
        }
    }

    public void fetchData(final int type, final String status){
        viewModel.getAllSubscriptions(this,dialog,viewModel,type,status,limit,skip).observe(this,
                new Observer<Subscriptions>() {
                    @Override
                    public void onChanged(Subscriptions subscriptions) {
                        progressBar.setVisibility(View.GONE);
                        adapter.allList(subscriptions.getData().getRows());
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
