package com.dopave.diethub_vendor.UI.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.SignIn.SignIn;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

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

    public MutableLiveData<SignIn> SignIn(final String Phone , final String Pass ,
                                          final Context context ,
                                          final ProgressDialog dialog,
                                          final Login_ViewModel viewModel){
        final MutableLiveData<SignIn> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().signIn(Phone,Pass).enqueue(new Callback<SignIn>() {
                    @Override
                    public void onResponse(Call<SignIn> call, Response<SignIn> response) {
                        dialog.dismiss();
                        if (response.code() == 200){
                            mutableLiveData.setValue(response.body());
                        }else if (response.code() == 401)
                            Toast.makeText(context, R.string.Incorrect_credential, Toast.LENGTH_SHORT).show();
                        else {
                            try {
                                String message = new JSONObject(response.errorBody()
                                        .string()).getString("message");
                                Log.i("TTTTTTT",message + response.code());
                                Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SignIn> call, Throwable t) {
                        dialog.dismiss();

                        if(t instanceof SocketTimeoutException) {
                            Toast.makeText(context,R.string.Unable_contact_server, Toast.LENGTH_SHORT).show();
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
