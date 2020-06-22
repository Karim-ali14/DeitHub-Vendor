package com.dopave.diethub_vendor.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dopave.diethub_vendor.Adapter.AdapterForSubscription;
import com.dopave.diethub_vendor.R;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);

        recyclerView = findViewById(R.id.recyclerViewSub);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        if (getIntent().getExtras().getString("type").equals("nav_mySubscriptionOrders"))
            recyclerView.setAdapter(new AdapterForSubscription(list,this,0));
        else
            recyclerView.setAdapter(new AdapterForSubscription(list,this,1));
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public void BackButton(View view) {
        finish();
    }

}
