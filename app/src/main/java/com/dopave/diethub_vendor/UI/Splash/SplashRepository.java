package com.dopave.diethub_vendor.UI.Splash;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.ProviderInfo.ProviderInformation;
import com.dopave.diethub_vendor.Models.SignIn.SignIn;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.Login.Login_inActivity;
import com.dopave.diethub_vendor.UI.Setting.Modify_Personal_info.Modify_Person_info_viewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashRepository {
    public static SplashRepository repository;
    public static SplashRepository getInstance(){
        if (repository == null)
            repository = new SplashRepository();
        return repository;
    }

    public MutableLiveData<ProviderInformation> getProviderInfo(final Context context,
                                                                String token, String id) {
        final MutableLiveData<ProviderInformation> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().getProviderInfo("Bearer " + token, id)
                .enqueue(new Callback<ProviderInformation>() {
            @Override
            public void onResponse(Call<ProviderInformation> call, Response<ProviderInformation> response) {
                if (response.code() == 200){
                    mutableLiveData.setValue(response.body());
                }else {
                    if (response.code() == 500){
                        Toast.makeText(context, R.string.Server_problem, Toast.LENGTH_SHORT).show();
                    }else if (response.code() == 401){
                        Common.onCheckTokenAction(context);
                    }
                    context.startActivity(new Intent(context, Login_inActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            }

            @Override
            public void onFailure(Call<ProviderInformation> call, Throwable t) {
                if(t instanceof SocketTimeoutException) {
                    Toast.makeText(context, R.string.Unable_contact_server, Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, Login_inActivity.class));
                }

                else if (t instanceof UnknownHostException) {
                    Toast.makeText(context,R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, Login_inActivity.class));
                }

                else {
                    Toast.makeText(context,R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, Login_inActivity.class));
                }
            }

        });
        return mutableLiveData;
    }
}
