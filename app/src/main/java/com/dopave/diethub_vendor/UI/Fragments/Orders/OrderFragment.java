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

import com.dopave.diethub_vendor.Adapter.AdapterForDelegate;
import com.dopave.diethub_vendor.Adapter.AdapterForOrder;
import com.dopave.diethub_vendor.Models.GetDeliveries.DeliveryRow;
import com.dopave.diethub_vendor.Models.GetDeliveries.GetDeliveriesData;
import com.dopave.diethub_vendor.Models.Orders.OrderRaw;
import com.dopave.diethub_vendor.Models.Orders.Orders;
import com.dopave.diethub_vendor.Models.VehicleTypes.VehicleTypes;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.CreateVehicle.CreateVehicleViewModel;
import com.dopave.diethub_vendor.UI.Fragments.Deliveries.DeliveryViewModel;

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
    public static final int limit = 5;
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

    DeliveryViewModel DViewModel;
    CreateVehicleViewModel vehicleViewModel;
    VehicleTypes vTypes;

    int typeID;

    public OrderFragment(int typeId) {
        this.typeID = typeID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View inflate = inflater.inflate(R.layout.fragment_my_order, container, false);
        init(inflate);
        return inflate;
    }

    public void getOrders(String [] Status, final int type,int limit, int skip,int typeRequest){
        progressBar.setVisibility(View.GONE);
        dialog.show();
        viewModel.getAllOrders(Status,viewModel,dialog,getActivity(),limit,skip,typeRequest)
                .observe(getViewLifecycleOwner(),
                new Observer<Orders>() {
                    @Override
                    public void onChanged(Orders orders) {
                        adapter = new AdapterForOrder
                                (orders.getData().getOrderRaw(),getActivity(),type,DViewModel,viewModel,recyclerView);
                        count = orders.getData().getCount();
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setAdapter(adapter);
                        if (orders.getData().getOrderRaw() == null || orders.getData().getOrderRaw().size() == 0)
                            Toast.makeText(getActivity(), R.string.there_no_order, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void fetchData(String [] Status, final int type,int limit, int skip,int typeRequest){
        progressBar.setVisibility(View.VISIBLE);
        viewModel.getAllOrders(Status,viewModel,dialog,getActivity(),limit,skip,typeRequest).observe(getViewLifecycleOwner(),
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
        DViewModel = ViewModelProviders.of(getActivity()).get(DeliveryViewModel.class);
        vehicleViewModel = ViewModelProviders.of(this).get(CreateVehicleViewModel.class);
        dialog = new ProgressDialog(getActivity());
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        getOrdersByButtonId(typeID);

        PendingLayout = view.findViewById(R.id.PendingLayout);
        PreparingLayout = view.findViewById(R.id.PreparingLayout);
        FinishedLayout = view.findViewById(R.id.FinishedLayout);

        PendingText = view.findViewById(R.id.PendingText);
        PreparingText = view.findViewById(R.id.PreparingText);
        FinishedText = view.findViewById(R.id.FinishedText);

        PendingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPendingButtonClick();
                typeID = PENDING_ID;
            }
        });

        PreparingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPreparingButtonClick();
                typeID = PREPARING_ID;
            }
        });

        FinishedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinishButtonClick();
                typeID = FINISHED_ID;
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

                if (isScrolling && (childCount+firstVisibleItemPosition == itemCount)
                        && itemCount < count){
                    isScrolling = false;
                    progressBar.setVisibility(View.VISIBLE);
                    if (activeButton.equals("Pending"))
                        fetchData(status,PENDING_ID,limit,++skip,1);
                    else if (activeButton.equals("Preparing"))
                        fetchData(status,PREPARING_ID,limit,++skip,1);
                    else if (activeButton.equals("Finished"))
                        fetchData(status,FINISHED_ID,limit,++skip,1);
                }
            }
        });

        vehicleViewModel.getAllVehicleTypes(getActivity(),
                dialog,vehicleViewModel,"DeliveryFragment",recyclerView)
                .observe(getActivity(), new Observer<VehicleTypes>() {
                    @Override
                    public void onChanged(VehicleTypes vehicleTypes) {
                        vTypes = vehicleTypes;
                        getAllDelivery(dialog,vehicleTypes);
                    }
                });
    }



    private void getOrdersByButtonId(int type) {
        if (type == 0) {
            activeButton = "Pending";

            status = new String[1];
            status[0] = "pending";
            skip = 0;
            getOrders(status, PENDING_ID, limit, skip,0);
        }else if (type == 1){
            onPreparingButtonClick();
        }else if (type == 2){
            onFinishButtonClick();
        }
    }

    public void getAllDelivery(ProgressDialog dialog, final VehicleTypes vehicleTypes){
        DViewModel.getAllDeliveries(dialog,getActivity(),DViewModel,recyclerView,vehicleTypes).observe(getActivity(),
                new Observer<GetDeliveriesData>() {
                    @Override
                    public void onChanged(GetDeliveriesData getDeliveriesData) {
                        if (getDeliveriesData.getData().getDeliveryRows().size() != 0)
                        {
                            List<DeliveryRow> deliveryRows = getDeliveriesData.getData().getDeliveryRows();
                            recyclerView.setAdapter(adapter);
                        }
                        else
                            Toast.makeText(getActivity(), "there are't any deliveries yet", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onPendingButtonClick() {
        if (!activeButton.equals("Pending")) {

            if (activeButton.equals("Finished")){
                OrderFragment.PREPARING = true;
                OrderFragment.FINISHED = true;
            }else if (activeButton.equals("Preparing")){
                OrderFragment.PREPARING = true;
            }

            activeButton = "Pending";

            status = new String[1];
            status[0] = "pending";
            skip = 0;
            getOrders(status, PENDING_ID, limit, skip,0);

            PendingLayout.setBackground(getResources().getDrawable(R.drawable.style_button_active));
            PendingText.setTextColor(getResources().getColor(R.color.white));

            PreparingLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
            PreparingText.setTextColor(getResources().getColor(R.color.colorPrimary));

            FinishedLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
            FinishedText.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }
    private void onPreparingButtonClick() {
        if (!activeButton.equals("Preparing")) {

            if (activeButton.equals("Finished")){
                OrderFragment.FINISHED = true;
                OrderFragment.PREPARING = true;
            }else if (activeButton.equals("Pending")){
                OrderFragment.FINISHED = false;
                OrderFragment.PREPARING = false;
            }

            activeButton = "Preparing";

            status = new String[2];
            status[0] = "accepted";
            status[1] = "preparing";
            skip = 0;
            PendingLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
            PendingText.setTextColor(getResources().getColor(R.color.colorPrimary));

            PreparingLayout.setBackground(getResources().getDrawable(R.drawable.style_button_active));
            PreparingText.setTextColor(getResources().getColor(R.color.white));

            FinishedLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
            FinishedText.setTextColor(getResources().getColor(R.color.colorPrimary));


            if (FINISHED) {
//                    recyclerView.setAdapter(new AdapterForOrder(getData(), getContext(), PREPARING_ID_2,"جاري التجهيز"));
                Log.i("JJJJJJJ", FINISHED + "FFFFFF");
                getOrders(status, PREPARING_ID_2, limit, skip,0);
            }
            else if (!PREPARING) {
//                    recyclerView.setAdapter(new AdapterForOrder(getData(), getContext(), PREPARING_ID,"جاري التجهيز"));
                Log.i("JJJJJJJ", FINISHED + "PPPPPPP");
                getOrders(status, PREPARING_ID, limit, skip,0);
            }
        }
    }
    private void onFinishButtonClick() {
        if (!activeButton.equals("Finished")) {

            if (activeButton.equals("Preparing")){
                OrderFragment.FINISHED = false;
                OrderFragment.PREPARING = true;
            }else if (activeButton.equals("Pending")){
                OrderFragment.FINISHED = false;
                OrderFragment.PREPARING = false;
            }

            activeButton = "Finished";
            status = new String[2];
            status[0] = "prepared";
            status[1] = "readyForDelivery";
            skip = 0;

            PendingLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
            PendingText.setTextColor(getResources().getColor(R.color.colorPrimary));

            PreparingLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
            PreparingText.setTextColor(getResources().getColor(R.color.colorPrimary));

            FinishedLayout.setBackground(getResources().getDrawable(R.drawable.style_button_active));
            FinishedText.setTextColor(getResources().getColor(R.color.white));

            if (!FINISHED)
                getOrders(status, FINISHED_ID,limit,skip,0);
        }
    }

}
