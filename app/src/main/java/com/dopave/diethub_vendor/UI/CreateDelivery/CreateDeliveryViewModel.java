package com.dopave.diethub_vendor.UI.CreateDelivery;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dopave.diethub_vendor.Models.Cities.Cities;
import com.dopave.diethub_vendor.Models.CreateDelivery.Request.CreateDeliveryRequest;
import com.dopave.diethub_vendor.Models.CreateDelivery.Response.CreateDeliveryResponse;

public class CreateDeliveryViewModel extends ViewModel {

    public CreateDeliveryRepository repository;

    public CreateDeliveryViewModel() {
        this.repository = CreateDeliveryRepository.getInstance();
    }

    public LiveData<Cities> getCities(final Context context){
        return repository.getCities(context);
    }

    public LiveData<CreateDeliveryResponse> createDelivery (String Auth,
                                                            final CreateDeliveryRequest requestBody,
                                                            String id,
                                                            final Context context,
                                                            final ProgressDialog dialog){
        return repository.createDelivery(Auth, requestBody, id, context, dialog);
    }
}
