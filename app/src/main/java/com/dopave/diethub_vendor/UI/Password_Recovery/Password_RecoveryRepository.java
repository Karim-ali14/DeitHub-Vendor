package com.dopave.diethub_vendor.UI.Password_Recovery;

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
import com.dopave.diethub_vendor.Models.ResetPassword.ResetPassword;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.Enter_CodeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Password_RecoveryRepository {
    public static Password_RecoveryRepository repository;
    public static Password_RecoveryRepository getInstance(){
        if (repository == null)
            repository = new Password_RecoveryRepository();
        return repository;
    }

    public MutableLiveData<ResetPassword> sendCodeToEmail(final String Email ,
                                                          final ProgressDialog dialog,
                                                          final Context context,
                                                          final Password_RecoveryViewModel viewModel) {
        final MutableLiveData<ResetPassword> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().sendCode(Email).enqueue(new Callback<ResetPassword>() {
            @Override
            public void onResponse(Call<ResetPassword> call, Response<ResetPassword> response) {
                dialog.dismiss();
                if (response.code() == 200){
                    mutableLiveData.setValue(response.body());
                }else {
                    if (response.code() == 422)
                        Toast.makeText(context, R.string.email_not_found, Toast.LENGTH_SHORT).show();
                    else if (response.code() == 500){
                        Toast.makeText(context, R.string.Server_problem, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        try {
                            String message = new JSONObject(response.errorBody()
                                    .string()).getString("message");
                            Log.i("TTTTTTT", message);
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResetPassword> call, Throwable t) {
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
