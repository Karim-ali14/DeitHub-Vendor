package com.dopave.diethub_vendor.UI.CreateDelivery;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dopave.diethub_vendor.Models.Cities.Cities;
import com.dopave.diethub_vendor.Models.CreateDeliveryRequest.Request.DeliveryByProvider;
import com.dopave.diethub_vendor.Models.CreateDeliveryRequest.Response.DeliveryByProviderResponse;

public class CreateDeliveryViewModel extends ViewModel {

    public CreateDeliveryRepository repository;

    public CreateDeliveryViewModel() {
        this.repository = CreateDeliveryRepository.getInstance();
    }

    public LiveData<Cities> getCities(final Context context){
        return repository.getCities(context);
    }

    public LiveData<DeliveryByProviderResponse> createDelivery (String Auth,
                                                         final DeliveryByProvider requestBody,
                                                         String id,
                                                         final Context context,
                                                         final ProgressDialog dialog){
        return repository.createDelivery(Auth, requestBody, id, context, dialog);
    }
}
