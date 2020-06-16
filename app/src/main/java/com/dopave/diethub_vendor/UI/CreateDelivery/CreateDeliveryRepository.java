package com.dopave.diethub_vendor.UI.CreateDelivery;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Cities.Cities;
import com.dopave.diethub_vendor.Models.CreateDeliveryRequest.Request.DeliveryByProvider;
import com.dopave.diethub_vendor.Models.CreateDeliveryRequest.Response.DeliveryByProviderResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateDeliveryRepository {
    public static CreateDeliveryRepository repository;
    public static CreateDeliveryRepository getInstance(){
        if (repository == null)
            repository = new CreateDeliveryRepository();
        return repository;
    }

    public MutableLiveData<Cities> getCities(final Context context){
        final MutableLiveData<Cities> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().getAllCities().enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Call<Cities> call, Response<Cities> response) {
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
            public void onFailure(Call<Cities> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<DeliveryByProviderResponse> createDelivery (String Auth,
                                                                       final DeliveryByProvider requestBody,
                                                                       String id,
                                                                       final Context context,
                                                                       final ProgressDialog dialog){
        final MutableLiveData<DeliveryByProviderResponse> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().createDeliveryByProvider(Auth, requestBody, id)
                .enqueue(new Callback<DeliveryByProviderResponse>() {
            @Override
            public void onResponse(Call<DeliveryByProviderResponse> call,
                                   Response<DeliveryByProviderResponse> response) {
                dialog.dismiss();
                if (response.code() == 201)
                    mutableLiveData.setValue(response.body());
                else {
                    try {
                        String message = new JSONObject(response.errorBody().string())
                                .getString("message");

                        Toast.makeText(context,message + response.code(), Toast.LENGTH_SHORT).show();

                        Log.d("FFFFFF",requestBody.toString() + response.code());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DeliveryByProviderResponse> call, Throwable t) {
                dialog.dismiss();
            }
        });
        return mutableLiveData;
    }
}
