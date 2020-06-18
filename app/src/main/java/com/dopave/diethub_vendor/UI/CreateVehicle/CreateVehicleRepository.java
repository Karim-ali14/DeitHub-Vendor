package com.dopave.diethub_vendor.UI.CreateVehicle;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.CreateVehicle.Request.CreateVehicleRequest;
import com.dopave.diethub_vendor.Models.CreateVehicle.Response.CreateVehicleRespons;
import com.dopave.diethub_vendor.Models.VehicleTypes.VehicleTypes;
import com.dopave.diethub_vendor.Models.Years.Years;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
}
