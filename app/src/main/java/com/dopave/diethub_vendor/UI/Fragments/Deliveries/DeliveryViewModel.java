package com.dopave.diethub_vendor.UI.Fragments.Deliveries;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dopave.diethub_vendor.Models.GetDeliveries.GetDeliveriesData;

public class DeliveryViewModel extends ViewModel {
    private DeliveryRepository repository;
    public DeliveryViewModel() {
        this.repository = DeliveryRepository.getInstance();
    }

    public LiveData<GetDeliveriesData> getAllDeliveries(final ProgressDialog dialog,
                                                        final Context context){
        return repository.getAllDeliveries(dialog,context);
    }
}
