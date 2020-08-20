package com.dopave.diethub_vendor.UI.NewPassword;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.ResetPassword.ResetPassword;
import com.dopave.diethub_vendor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPassword_Repository {
    public static NewPassword_Repository repository;

    public static NewPassword_Repository getInstance(){
        if (repository == null)
            repository = new NewPassword_Repository();
        return repository;
    }

    public MutableLiveData<ResetPassword> reset_password(final Context context,final ProgressDialog dialog,
                                                   String Email, String Code, String Password) {
        dialog.show();
        final MutableLiveData<ResetPassword> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().reset_password(Email,Code,Password)
                .enqueue(new Callback<ResetPassword>() {
                    @Override
                    public void onResponse(Call<ResetPassword> call, Response<ResetPassword> response) {
                        dialog.dismiss();
                        Toast.makeText(context,response.message(), Toast.LENGTH_SHORT).show();
                        if (response.code() == 200)
                            mutableLiveData.setValue(response.body());
                        else {
                            try {
                                Toast.makeText(context,new JSONObject(response.errorBody().string())
                                                .getString("message"), Toast.LENGTH_SHORT).show();
                                JSONArray errors = new JSONObject(response.errorBody().string())
                                        .getJSONArray("errors");
                                Log.i("TTTTTVV",errors+"");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResetPassword> call, Throwable t) {
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
