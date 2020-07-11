package com.dopave.diethub_vendor.UI.Subscriptions;

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
    SubscriptionsViewModel viewModel;
    ProgressDialog dialog;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);
        viewModel = ViewModelProviders.of(this).get(SubscriptionsViewModel.class);
        dialog = new ProgressDialog(this);
        dialog.show();
        recyclerView = findViewById(R.id.recyclerViewSub);
        title = findViewById(R.id.title);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

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
    }

    public void BackButton(View view) {
        finish();
    }

    private void getAllSub(final int type, final String status){
        viewModel.getAllSubscriptions(this,dialog,viewModel,type,status).observe(this,
                new Observer<Subscriptions>() {
            @Override
            public void onChanged(Subscriptions subscriptions) {
                setListOfSubscription(type,subscriptions.getData().getRows(),viewModel,status);
            }
        });
    }

    public void setListOfSubscription(int type, List<Row> list,
                                      SubscriptionsViewModel viewModel,String status){
        if (list.size() != 0) {
            recyclerView.setAdapter(new AdapterForSubscription(list,
                    SubscriptionsActivity.this, type, viewModel,dialog,status,recyclerView));
        }else {
            Toast.makeText(this, R.string.no_Subscription, Toast.LENGTH_SHORT).show();
        }
    }
}
