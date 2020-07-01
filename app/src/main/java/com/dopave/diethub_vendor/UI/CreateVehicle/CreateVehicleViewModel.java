package com.dopave.diethub_vendor.UI.CreateVehicle;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.dopave.diethub_vendor.Models.CreateVehicle.Request.CreateVehicleRequest;
import com.dopave.diethub_vendor.Models.CreateVehicle.Request.Image;
import com.dopave.diethub_vendor.Models.CreateVehicle.Response.CreateVehicleRespons;
import com.dopave.diethub_vendor.Models.GetVehicles.Data;
import com.dopave.diethub_vendor.Models.GetVehicles.GetVehicleData;
import com.dopave.diethub_vendor.Models.VehicleTypes.VehicleTypes;
import com.dopave.diethub_vendor.Models.Years.Years;

import java.util.List;

public class CreateVehicleViewModel extends ViewModel {
    public CreateVehicleRepository repository;

    public CreateVehicleViewModel() {
        this.repository = CreateVehicleRepository.getInstance();
    }

    public LiveData<VehicleTypes> getAllVehicleTypes(final Context context, ProgressDialog dialog,
                                                     CreateVehicleViewModel viewModel, String type,
                                                     RecyclerView recyclerView){
        return repository.getAllVehicleTypes(context,dialog,viewModel,type,recyclerView);
    }

    public LiveData<Years> getAllYears(final Context context,ProgressDialog dialog,
                                       CreateVehicleViewModel viewModel){
        return repository.getAllYears(context,dialog,viewModel);
    }

    public LiveData<CreateVehicleRespons> createVehicle(String Auth, String id,
                                                        String deliveryId,
                                                        CreateVehicleRequest createVehicleRequest,
                                                        final Context context,ProgressDialog dialog){
        return repository.createVehicle(Auth, id, deliveryId, createVehicleRequest, context,dialog);
    }

    public LiveData<GetVehicleData> getVehicleData(String deliveryId, final Context context,
                                                   final ProgressDialog dialog,
                                                   CreateVehicleViewModel viewModel){
        return repository.getVehicleData(deliveryId, context, dialog,viewModel);
    }

    LiveData<Data> updateVehicle(String vehicleId, String Number, String vehicleModel,
                                 int selectedYear, int vehicleTypes,
                                 String drivingLicenceImage,
                                 String vehicleLicenceImage, List<Image> list,
                                 final Context context,ProgressDialog dialog){
        return repository.updateVehicle(vehicleId, Number, vehicleModel, selectedYear,
                vehicleTypes, drivingLicenceImage, vehicleLicenceImage, list, context,dialog);
    }
}
