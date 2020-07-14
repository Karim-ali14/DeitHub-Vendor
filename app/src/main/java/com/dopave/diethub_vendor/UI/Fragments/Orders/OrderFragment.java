package com.dopave.diethub_vendor.UI.Fragments.Orders;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dopave.diethub_vendor.Adapter.AdapterForOrder;
import com.dopave.diethub_vendor.Models.Orders.OrderRaw;
import com.dopave.diethub_vendor.Models.Orders.Orders;
import com.dopave.diethub_vendor.R;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    ProgressBar progressBar;
    AdapterForOrder adapter;

    public static int PENDING_ID = 0;
    public static int PREPARING_ID = 1;
    public static int PREPARING_ID_2 = 3;
    public static int FINISHED_ID = 2;
    static final int limit = 5;
    int skip = 0;
    String status[];
    public static boolean PENDING = false;
    public static boolean PREPARING = false;
    public static boolean FINISHED = false;
    boolean isScrolling = false;
    LinearLayout PendingLayout , PreparingLayout , FinishedLayout;
    TextView PendingText , PreparingText , FinishedText;
    OrdersViewModel viewModel;
    ProgressDialog dialog;
    String activeButton = "Pending";
    int count;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View inflate = inflater.inflate(R.layout.fragment_my_order, container, false);
        init(inflate);
        return inflate;
    }

    private void getOrders(String [] Status, final int type,int limit, int skip){
        progressBar.setVisibility(View.GONE);
        dialog.show();
        viewModel.getAllOrders(Status,viewModel,dialog,getActivity(),limit,skip).observe(getViewLifecycleOwner(),
                new Observer<Orders>() {
                    @Override
                    public void onChanged(Orders orders) {
                        adapter = new AdapterForOrder
                                (orders.getData().getOrderRaw(),getActivity(),type);
                        count = orders.getData().getCount();
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }
    private void fetchData(String [] Status, final int type,int limit, int skip){
        progressBar.setVisibility(View.VISIBLE);
        viewModel.getAllOrders(Status,viewModel,dialog,getActivity(),limit,skip).observe(getViewLifecycleOwner(),
                new Observer<Orders>() {
                    @Override
                    public void onChanged(Orders orders) {
                        adapter.allList(orders.getData().getOrderRaw(),type);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void init (View view){
        viewModel = ViewModelProviders.of(getActivity()).get(OrdersViewModel.class);
        dialog = new ProgressDialog(getActivity());
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        status = new String[1];
        status[0] = "pending";
        getOrders(status,PENDING_ID,limit,skip);
        PendingLayout = view.findViewById(R.id.PendingLayout);
        PreparingLayout = view.findViewById(R.id.PreparingLayout);
        FinishedLayout = view.findViewById(R.id.FinishedLayout);

        PendingText = view.findViewById(R.id.PendingText);
        PreparingText = view.findViewById(R.id.PreparingText);
        FinishedText = view.findViewById(R.id.FinishedText);

        PendingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activeButton.equals("Pending")) {
                    activeButton = "Pending";

                    status = new String[1];
                    status[0] = "pending";
                    skip = 0;
                    getOrders(status, PENDING_ID, limit, skip);

                    PendingLayout.setBackground(getResources().getDrawable(R.drawable.style_button_active));
                    PendingText.setTextColor(getResources().getColor(R.color.white));

                    PreparingLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
                    PreparingText.setTextColor(getResources().getColor(R.color.colorPrimary));

                    FinishedLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
                    FinishedText.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });

        PreparingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activeButton.equals("Preparing")) {
                    activeButton = "Preparing";
                    status = new String[3];
                    status[0] = "accepted";
                    status[1] = "prepared";
                    status[2] = "delivering";
                    skip = 0;
                    PendingLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
                    PendingText.setTextColor(getResources().getColor(R.color.colorPrimary));

                    PreparingLayout.setBackground(getResources().getDrawable(R.drawable.style_button_active));
                    PreparingText.setTextColor(getResources().getColor(R.color.white));

                    FinishedLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
                    FinishedText.setTextColor(getResources().getColor(R.color.colorPrimary));


                    if (FINISHED)
//                    recyclerView.setAdapter(new AdapterForOrder(getData(), getContext(), PREPARING_ID_2,"جاري التجهيز"));
                        getOrders(status, PREPARING_ID_2,limit,skip);
                    else if (!PREPARING)
//                    recyclerView.setAdapter(new AdapterForOrder(getData(), getContext(), PREPARING_ID,"جاري التجهيز"));
                        getOrders(status, PREPARING_ID,limit,skip);
                }
            }
        });

        FinishedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activeButton.equals("Finished")) {
                    activeButton = "Finished";
                    status = new String[1];
                    status[0] = "delivered";
                    skip = 0;

                    PendingLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
                    PendingText.setTextColor(getResources().getColor(R.color.colorPrimary));

                    PreparingLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
                    PreparingText.setTextColor(getResources().getColor(R.color.colorPrimary));

                    FinishedLayout.setBackground(getResources().getDrawable(R.drawable.style_button_active));
                    FinishedText.setTextColor(getResources().getColor(R.color.white));

                    if (!FINISHED)
                        getOrders(status, FINISHED_ID,limit,skip);
                }
            }
        });
        
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
                    isScrolling = false;
                    progressBar.setVisibility(View.VISIBLE);
                    if (activeButton.equals("Pending"))
                        fetchData(status,PENDING_ID,limit,++skip);
                    else if (activeButton.equals("Preparing"))
                        fetchData(status,PREPARING_ID,limit,++skip);
                    else if (activeButton.equals("Finished"))
                        fetchData(status,FINISHED_ID,limit,++skip);
                }
            }
        });

    }
}
