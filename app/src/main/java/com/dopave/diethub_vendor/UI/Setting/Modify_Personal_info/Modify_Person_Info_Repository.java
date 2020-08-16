package com.dopave.diethub_vendor.UI.Setting.Modify_Personal_info;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.ProviderInfo.ProviderInfo;
import com.dopave.diethub_vendor.Models.ProviderInfo.Request.ProviderInfoRequest;
import com.dopave.diethub_vendor.R;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Modify_Person_Info_Repository {
    public static Modify_Person_Info_Repository repository;
    public static Modify_Person_Info_Repository getInstance(){
        if (repository  == null)
            repository = new Modify_Person_Info_Repository();
        return repository;
    }

    public MutableLiveData<ProviderInfo> getProviderInfo(final Context context, final ProgressDialog dialog){
        final MutableLiveData<ProviderInfo> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().getProviderInfo("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"")
                .enqueue(new Callback<ProviderInfo>() {
                    @Override
                    public void onResponse(Call<ProviderInfo> call, Response<ProviderInfo> response) {
                        dialog.dismiss();
                        if (response.code() == 200){
                            mutableLiveData.setValue(response.body());
                        }else {
                            try {
                                String message = new JSONObject(response.errorBody().string())
                                        .getString("message");
                                Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProviderInfo> call, Throwable t) {

                    }
                });

        return mutableLiveData;
    }

    public MutableLiveData<Defualt> updateProvideInfo(final Context context,
                                                      final ProgressDialog dialog,
                                                      final ProviderInfoRequest request){
        dialog.show();
        final MutableLiveData<Defualt> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().updateProviderInfo("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",request).enqueue(new Callback<Defualt>() {
            @Override
            public void onResponse(Call<Defualt> call, Response<Defualt> response) {
                dialog.dismiss();
                if (response.code() == 200)
                    mutableLiveData.setValue(response.body());
                else {
                    try {
                        String message = new JSONObject(response.errorBody().string())
                                .getString("message");
                        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
                        Log.i("GGGGGGG",new JSONObject(response.errorBody().string())
                                .getJSONArray("errors").getJSONObject(0).getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Defualt> call, Throwable t) {
                dialog.dismiss();

                if(t instanceof SocketTimeoutException) {
                    Toast.makeText(context, R.string.Unable_contact_server, Toast.LENGTH_SHORT).show();
                }

                else if (t instanceof UnknownHostException) {
                    Toast.makeText(context,R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }

                else {
                    Toast.makeText(context,R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return mutableLiveData;
    }
}
