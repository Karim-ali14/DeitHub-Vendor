package com.dopave.diethub_vendor.UI.Setting.TimeWork;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.GetTimeWork.Data;
import com.dopave.diethub_vendor.Models.GetTimeWork.TimeWorks;
import com.dopave.diethub_vendor.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeWorkRepository {
    public static TimeWorkRepository timeWorkRepository;
    public static TimeWorkRepository getInstance(){
        if (timeWorkRepository == null)
            timeWorkRepository = new TimeWorkRepository();
        return timeWorkRepository;
    }

    public MutableLiveData<TimeWorks> getTimeWork(final Context context, final ProgressDialog dialog){
        final MutableLiveData<TimeWorks> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().getTimeWork(Common.currentPosition.getData().getProvider()
                .getId()+"").enqueue(new Callback<TimeWorks>() {
            @Override
            public void onResponse(Call<TimeWorks> call, Response<TimeWorks> response) {
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
            public void onFailure(Call<TimeWorks> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<Defualt> updateWorkTime(Data data,final Context context, final ProgressDialog dialog){
        final MutableLiveData<Defualt> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().getCreateTimeWork("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",data).enqueue(new Callback<Defualt>() {
            @Override
            public void onResponse(Call<Defualt> call, Response<Defualt> response) {
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
