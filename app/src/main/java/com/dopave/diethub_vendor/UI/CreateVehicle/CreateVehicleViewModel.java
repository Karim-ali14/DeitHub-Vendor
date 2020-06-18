package com.dopave.diethub_vendor.UI.CreateVehicle;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dopave.diethub_vendor.Models.CreateVehicle.Request.CreateVehicleRequest;
import com.dopave.diethub_vendor.Models.CreateVehicle.Response.CreateVehicleRespons;
import com.dopave.diethub_vendor.Models.VehicleTypes.VehicleTypes;
import com.dopave.diethub_vendor.Models.Years.Years;

public class CreateVehicleViewModel extends ViewModel {
    public CreateVehicleRepository repository;

    public CreateVehicleViewModel() {
        this.repository = CreateVehicleRepository.getInstance();
    }

    public LiveData<VehicleTypes> getAllVehicleTypes(final Context context){
        return repository.getAllVehicleTypes(context);
    }

    public LiveData<Years> getAllYears(final Context context){
        return repository.getAllYears(context);
    }

    public LiveData<CreateVehicleRespons> createVehicle(String Auth, String id,
                                                        String deliveryId,
                                                        CreateVehicleRequest createVehicleRequest,
                                                        final Context context){
        return repository.createVehicle(Auth, id, deliveryId, createVehicleRequest, context);
    }
}