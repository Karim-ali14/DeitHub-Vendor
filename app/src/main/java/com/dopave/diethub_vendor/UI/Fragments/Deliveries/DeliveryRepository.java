package com.dopave.diethub_vendor.UI.Fragments.Deliveries;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.GetDeliveries.GetDeliveriesData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryRepository {
    public static DeliveryRepository repository;
    public static DeliveryRepository getInstance(){
        if (repository == null)
            repository= new DeliveryRepository();
        return repository;
    }

    public MutableLiveData<GetDeliveriesData> getAllDeliveries(final ProgressDialog dialog,
                                                               final Context context){
        final MutableLiveData<GetDeliveriesData> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().getAllDeliveries("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",
                true,true)
                .enqueue(new Callback<GetDeliveriesData>() {
                    @Override
                    public void onResponse(Call<GetDeliveriesData> call, Response<GetDeliveriesData> response) {
                        dialog.dismiss();
                        if (response.code() == 200){
                            mutableLiveData.setValue(response.body());
                        } else {
                            try {
                                String message = new JSONObject(response.errorBody()
                                        .string()).getString("message");
                                Log.i("TTTTTTT",message+response.code());
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetDeliveriesData> call, Throwable t) {

                    }
                });
        return mutableLiveData;
    }
}
