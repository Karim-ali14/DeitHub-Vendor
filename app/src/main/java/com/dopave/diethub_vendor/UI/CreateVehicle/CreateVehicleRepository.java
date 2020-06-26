package com.dopave.diethub_vendor.UI.CreateVehicle;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.CreateVehicle.Request.CreateVehicleRequest;
import com.dopave.diethub_vendor.Models.CreateVehicle.Request.Image;
import com.dopave.diethub_vendor.Models.CreateVehicle.Response.CreateVehicleRespons;
import com.dopave.diethub_vendor.Models.GetVehicles.Data;
import com.dopave.diethub_vendor.Models.GetVehicles.GetVehicleData;
import com.dopave.diethub_vendor.Models.UpdateVehicle.DrivingLicenceImage;
import com.dopave.diethub_vendor.Models.UpdateVehicle.UpdateVehicle;
import com.dopave.diethub_vendor.Models.UpdateVehicle.VehicleLicenceImage;
import com.dopave.diethub_vendor.Models.VehicleTypes.VehicleTypes;
import com.dopave.diethub_vendor.Models.Years.Years;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateVehicleRepository {
    public static CreateVehicleRepository repository;
    public static CreateVehicleRepository getInstance(){
        if (repository == null)
            repository = new CreateVehicleRepository();
        return repository;
    }

    public MutableLiveData<VehicleTypes> getAllVehicleTypes(final Context context){
        final MutableLiveData<VehicleTypes> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().getAllVehicleTypes().enqueue(new Callback<VehicleTypes>() {
            @Override
            public void onResponse(Call<VehicleTypes> call, Response<VehicleTypes> response) {
                if (response.code()==200){
                    mutableLiveData.setValue(response.body());
                }else {
                    try {
                        String message = new JSONObject(response.errorBody().string())
                                .getString("message");
                        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();

                        Log.i("TTTTTT",new JSONObject(response.errorBody().string())
                                .getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<VehicleTypes> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<Years> getAllYears(final Context context){
        final MutableLiveData<Years> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().getAllYears().enqueue(new Callback<Years>() {
            @Override
            public void onResponse(Call<Years> call, Response<Years> response) {
                if (response.code() == 200){
                    mutableLiveData.setValue(response.body());
                }else {
                    try {
                        String message = new JSONObject(response.errorBody().string())
                                .getString("message");
                        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();

                        Log.i("TTTTTT",new JSONObject(response.errorBody().string())
                                .getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Years> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<CreateVehicleRespons> createVehicle(String Auth,String id,
                                                               String deliveryId,
                                                               CreateVehicleRequest createVehicleRequest,
                                                               final Context context){
        final MutableLiveData<CreateVehicleRespons> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().createVehicle(Auth, id, deliveryId, createVehicleRequest)
                .enqueue(new Callback<CreateVehicleRespons>() {
            @Override
            public void onResponse(Call<CreateVehicleRespons> call, Response<CreateVehicleRespons> response) {
                if (response.code() == 201)
                    mutableLiveData.setValue(response.body());
                else {
                    try {
                        String message = new JSONObject(response.errorBody().string())
                                .getString("message");
                        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();

                        Log.i("TTTTTT",new JSONObject(response.errorBody().string())
                                .getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateVehicleRespons> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<GetVehicleData> getVehicleData(String deliveryId, final Context context,
                                                          final ProgressDialog dialog){
        final MutableLiveData<GetVehicleData> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().getVehicleByDeliveryId("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId().toString(),deliveryId,
                true,true,true)
                .enqueue(new Callback<GetVehicleData>() {
                    @Override
                    public void onResponse(Call<GetVehicleData> call, Response<GetVehicleData> response) {
                        dialog.dismiss();
                        if (response.code() == 200)
                            mutableLiveData.setValue(response.body());
                        else {
                            try {
                                String message = new JSONObject(response.errorBody().string())
                                        .getString("message");
                                Toast.makeText(context,message, Toast.LENGTH_SHORT).show();

                                Log.i("TTTTTT",new JSONObject(response.errorBody().string())
                                        .getString("message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetVehicleData> call, Throwable t) {
                        Log.i("TTTTTTF",t.getMessage());
                    }
                });
        return mutableLiveData;
    }

    public MutableLiveData<Data> updateVehicle(String vehicleId, String Number, String vehicleModel,
                                               int selectedYear, int vehicleTypes,
                                               String drivingLicenceImage,
                                               String vehicleLicenceImage, List<Image> list,
                                               final Context context, final ProgressDialog dialog){
        final MutableLiveData<Data> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().updateVehicle("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",
                vehicleId,new UpdateVehicle(Number,vehicleModel,
                        selectedYear,vehicleTypes,new DrivingLicenceImage(drivingLicenceImage),
                        new VehicleLicenceImage(vehicleLicenceImage),list)).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                dialog.dismiss();
                if (response.code() == 200)
                    mutableLiveData.setValue(response.body());
                else {
                    try {
                        String message = new JSONObject(response.errorBody().string())
                                .getString("message");
                        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();

                        Log.i("TTTTTT",new JSONObject(response.errorBody().string())
                                .getString("error").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }
}
