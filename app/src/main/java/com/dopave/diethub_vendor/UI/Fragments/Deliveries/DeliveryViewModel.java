package com.dopave.diethub_vendor.UI.Fragments.Deliveries;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.dopave.diethub_vendor.Models.GetDeliveries.GetDeliveriesData;
import com.dopave.diethub_vendor.Models.VehicleTypes.VehicleTypes;

public class DeliveryViewModel extends ViewModel {
    private DeliveryRepository repository;
    public DeliveryViewModel() {
        this.repository = DeliveryRepository.getInstance();
    }

    public LiveData<GetDeliveriesData> getAllDeliveries(final ProgressDialog dialog,
                                                        final Context context,
                                                        DeliveryViewModel viewModel,
                                                        RecyclerView recyclerView,
                                                        VehicleTypes vehicleTypes,
                                                        boolean hasTrip){
        return repository.getAllDeliveries(dialog,context,viewModel,recyclerView,vehicleTypes,hasTrip);
    }
    public LiveData<GetDeliveriesData> getAllDeliveries(final ProgressDialog dialog,
                                                        final Context context,
                                                        DeliveryViewModel viewModel,
                                                        RecyclerView recyclerView,
                                                        VehicleTypes vehicleTypes){
        return repository.getAllDeliveries(dialog,context,viewModel,recyclerView,vehicleTypes);
    }

    public LiveData<GetDeliveriesData> deleteDelivery(final String deliveryId,
                                                      final ProgressDialog dialog, final Context context){
        return repository.deleteDelivery(deliveryId, dialog, context);
    }
}
