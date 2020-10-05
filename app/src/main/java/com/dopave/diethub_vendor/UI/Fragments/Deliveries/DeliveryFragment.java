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

import com.dopave.diethub_vendor.Models.GetDeliveries.DeliveryRow;
import com.dopave.diethub_vendor.Models.GetDeliveries.GetDeliveriesData;
import com.dopave.diethub_vendor.Models.VehicleTypes.VehicleTypes;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.CreateDelivery.CreateDeliveryActivity;
import com.dopave.diethub_vendor.UI.CreateVehicle.CreateVehicleViewModel;

import java.util.ArrayList;
import java.util.List;


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
    int count = 5;
    boolean hasMoreData;
    List<DeliveryRow> list;
    boolean isScrolling = false;
    ProgressBar progressBar;
    VehicleTypes vTypes;
    public static final int limit = 5 ;
    int skip = 0;
    public DeliveryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delivery, container, false);
        list = new ArrayList<>();
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
        vehicleViewModel.getAllVehicleTypes(getActivity(), // get all types to know what is vehicle type
                dialog,vehicleViewModel,"DeliveryFragment",recyclerView)
                .observe(getActivity(), new Observer<VehicleTypes>() {
            @Override
            public void onChanged(VehicleTypes vehicleTypes) {
                vTypes = vehicleTypes;
                getAllDelivery(dialog,vehicleTypes);
            }
        });
//        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                isScrolling = true;
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int childCount = manager.getChildCount();
//                int itemCount = manager.getItemCount();
//                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
//
//                if (isScrolling && (childCount+firstVisibleItemPosition == itemCount) &&
//                        list.size() == count && hasMoreData){
//                    count += 5;
//                    progressBar.setVisibility(View.VISIBLE);
//                    isScrolling = false;
//                    progressBar.setVisibility(View.VISIBLE);
//                    fetchData(dialog,vTypes,++skip);
//                }
//            }
//        });
        return view;
    }

    public void getAllDelivery(ProgressDialog dialog, final VehicleTypes vehicleTypes){
        viewModel.getAllDeliveries(dialog,getActivity(),viewModel,recyclerView,vehicleTypes).observe(getActivity(),
                new Observer<GetDeliveriesData>() {
            @Override
            public void onChanged(GetDeliveriesData getDeliveriesData) {
                if (getDeliveriesData.getData().getDeliveryRows().size() != 0) {
                    if (getDeliveriesData.getData().getDeliveryRows().size() == 15){
                        hasMoreData = true;
                    }else {
                        hasMoreData = false;
                    }
                    list.addAll(getDeliveriesData.getData().getDeliveryRows());
                    adapter = new AdapterForDelegate(list,getContext(),recyclerView,
                            viewModel,vehicleTypes);
                    recyclerView.setAdapter(adapter);
                }
                else
                    Toast.makeText(getActivity(), R.string.there_arent_any_deliveries_yet, Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void fetchData(ProgressDialog dialog, final VehicleTypes vehicleTypes,int skip){
//        viewModel.getAllDeliveries(dialog,getActivity(),viewModel,recyclerView,
//                vehicleTypes,limit,skip).observe(getActivity(),
//                new Observer<GetDeliveriesData>() {
//                    @Override
//                    public void onChanged(GetDeliveriesData getDeliveriesData) {
////                        adapter.allList(getDeliveriesData.getData().getDeliveryRows());
//                        if (getDeliveriesData.getData().getDeliveryRows().size() == 15){
//                            hasMoreData = true;
//                        }else {
//                            hasMoreData = false;
//                        }
//                        list.addAll(getDeliveriesData.getData().getDeliveryRows());
//                        adapter.notifyDataSetChanged();
//                    }
//                });
//    }
}
