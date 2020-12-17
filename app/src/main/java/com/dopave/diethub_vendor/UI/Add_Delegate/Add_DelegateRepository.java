package com.dopave.diethub_vendor.UI.Add_Delegate;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Cities.Cities;
import com.dopave.diethub_vendor.Models.Cities.CityRow;
import com.dopave.diethub_vendor.R;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_DelegateRepository {
    public static Add_DelegateRepository repository;
    public static Add_DelegateRepository getInstance(){
        if (repository == null)
            repository = new Add_DelegateRepository();
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
                    if (response.code() >= 500){
                        Toast.makeText(context, R.string.Server_problem, Toast.LENGTH_SHORT).show();
                    }else {
                        try {
                            String message = new JSONObject(response.errorBody()
                                    .string()).getString("message");
                            Log.i("TTTTTTT",message);
                            Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Cities> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

}
