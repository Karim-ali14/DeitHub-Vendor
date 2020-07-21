package com.dopave.diethub_vendor.UI.Fragments.Deliveries;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dopave.diethub_vendor.Adapter.AdapterForDelegate;

import com.dopave.diethub_vendor.Models.GetDeliveries.GetDeliveriesData;
import com.dopave.diethub_vendor.Models.VehicleTypes.VehicleTypes;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.CreateDelivery.CreateDeliveryActivity;
import com.dopave.diethub_vendor.UI.CreateVehicle.CreateVehicleViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryFragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    AdapterForDelegate adapter;
    LinearLayout AddDelegateLayout;
    DeliveryViewModel viewModel;
    CreateVehicleViewModel vehicleViewModel;
    int count;
    boolean isScrolling = false;
    ProgressBar progressBar;
    VehicleTypes vTypes;
    public DeliveryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delivery, container, false);
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.show();
        viewModel = ViewModelProviders.of(this).get(DeliveryViewModel.class);
        recyclerView = view.findViewById(R.id.recyclerForDelegate);
        recyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        AddDelegateLayout = view.findViewById(R.id.AddDelegateLayout);
        vehicleViewModel = ViewModelProviders.of(this).get(CreateVehicleViewModel.class);
        AddDelegateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateDeliveryActivity.class).putExtra("type","normal"));
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
                    fetchData(dialog,vTypes);
                }
            }
        });
        return view;
    }

    public void getAllDelivery(ProgressDialog dialog, final VehicleTypes vehicleTypes){
        viewModel.getAllDeliveries(dialog,getActivity(),viewModel,recyclerView,vehicleTypes).observe(getActivity(),
                new Observer<GetDeliveriesData>() {
            @Override
            public void onChanged(GetDeliveriesData getDeliveriesData) {
                if (getDeliveriesData.getData().getDeliveryRows().size() != 0)
                {
                    adapter = new AdapterForDelegate(
                            getDeliveriesData.getData().getDeliveryRows(),getContext(),recyclerView,
                            viewModel,vehicleTypes);
                    recyclerView.setAdapter(adapter);
                }
                else
                    Toast.makeText(getActivity(), "there are't any deliveries yet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchData(ProgressDialog dialog, final VehicleTypes vehicleTypes){
        viewModel.getAllDeliveries(dialog,getActivity(),viewModel,recyclerView,vehicleTypes).observe(getActivity(),
                new Observer<GetDeliveriesData>() {
                    @Override
                    public void onChanged(GetDeliveriesData getDeliveriesData) {
                        adapter.allList(getDeliveriesData.getData().getDeliveryRows());
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
