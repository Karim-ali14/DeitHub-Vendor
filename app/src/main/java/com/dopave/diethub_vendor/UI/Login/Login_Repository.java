package com.dopave.diethub_vendor.UI.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.SignIn.SignIn;
import com.dopave.diethub_vendor.UI.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Repository {
    public static Login_Repository repository;
    public static Login_Repository getInstance(){
        if (repository == null)
            repository =new Login_Repository();
        return repository;
    }

    public MutableLiveData<SignIn> SignIn(String Phone , String Pass ,
                                          final Context context ,
                                          final ProgressDialog dialog){
        final MutableLiveData<SignIn> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().signIn(Phone,Pass).enqueue(new Callback<SignIn>() {
                    @Override
                    public void onResponse(Call<SignIn> call, Response<SignIn> response) {
                        dialog.dismiss();
                        if (response.code() == 200){
                            mutableLiveData.setValue(response.body());
                        }else {
                            try {
                                String message = new JSONObject(response.errorBody()
                                        .string()).getString("message");
                                Log.i("TTTTTTT",message);
                                Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SignIn> call, Throwable t) {
                        dialog.dismiss();
                        Log.i("TTTTT",t.getMessage());
                    }
                });
        return mutableLiveData;
    }
}
