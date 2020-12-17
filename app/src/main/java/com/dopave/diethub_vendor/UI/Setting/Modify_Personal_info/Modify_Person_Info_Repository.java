package com.dopave.diethub_vendor.UI.Setting.Modify_Personal_info;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.ProviderInfo.ProviderInformation;
import com.dopave.diethub_vendor.Models.ProviderInfo.Request.ProviderInfoRequest;
import com.dopave.diethub_vendor.Models.SignIn.SignIn;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.Setting.Modify_Images.Modify_ImagesActivity;

import org.json.JSONArray;
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

    public MutableLiveData<ProviderInformation> getProviderInfo(final Context context,
                                                         final ProgressDialog dialog,
                                                         final Modify_Person_info_viewModel viewModel,
                                                         final String type){
        dialog.show();
        final MutableLiveData<ProviderInformation> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().getProviderInfo("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"")
                .enqueue(new Callback<ProviderInformation>() {
                    @Override
                    public void onResponse(Call<ProviderInformation> call, Response<ProviderInformation> response) {
                        if (response.code() == 200){
                            mutableLiveData.setValue(response.body());
                        }else {
                            dialog.dismiss();
                            if (response.code() >= 500){
                                Toast.makeText(context, R.string.Server_problem, Toast.LENGTH_SHORT).show();
                            }else if (response.code() == 401){
                                Common.onCheckTokenAction(context);
                            }else {
                                try {
                                    String message = new JSONObject(response.errorBody().string())
                                            .getString("message");
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProviderInformation> call, final Throwable t) {
                        dialog.dismiss();
                        final AlertDialog.Builder Adialog = new AlertDialog.Builder(context);
                        View view = LayoutInflater.from(context).inflate(R.layout.error_dialog, null);
                        TextView Title = view.findViewById(R.id.Title);
                        TextView Message = view.findViewById(R.id.Message);
                        Button button = view.findViewById(R.id.Again);
                        Adialog.setView(view);
                        final AlertDialog dialog1 = Adialog.create();
                        dialog1.setCanceledOnTouchOutside(false);
                        dialog1.setCancelable(false);
                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                                dialog.show();
                                if (type.equals("forImage")) {
                                    viewModel.getProviderInfo(context, dialog, viewModel,"forImage")
                                            .observe((LifecycleOwner) context, new Observer<ProviderInformation>() {
                                        @Override
                                        public void onChanged(ProviderInformation providerInfo) {
                                            ((Modify_ImagesActivity)context).onGetMainImage(providerInfo);
                                        }
                                    });
                                }else if (type.equals("forPersonalInfo")){
                                    viewModel.getProviderInfo(context, dialog, viewModel,"forPersonalInfo")
                                            .observe((LifecycleOwner) context, new Observer<ProviderInformation>() {
                                                @Override
                                                public void onChanged(ProviderInformation providerInfo) {
                                                    ((Modify_personal_infoActivity)context).onGetProviderInfo(providerInfo);
                                                }
                                            });
                                }
                            }
                        });
                        if(t instanceof SocketTimeoutException) {
                            Title.setText(R.string.Unable_contact_server);
                            Message.setText(R.string.Error_downloading_data);
                            dialog1.show();
                        }

                        else if (t instanceof UnknownHostException) {
                            Title.setText(context.getResources().getString(R.string.no_internet_connection));
                            Message.setText(R.string.Make_sure_you_online);
                            dialog1.show();
                        }else {
                            Title.setText(context.getResources().getString(R.string.no_internet_connection));
                            Message.setText(R.string.Error_downloading_data);
                            dialog1.show();
                        }
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
                    if (response.code() >= 500){
                        Toast.makeText(context, R.string.Server_problem, Toast.LENGTH_SHORT).show();
                    }else if (response.code() == 422) {
                        try {
                            JSONArray errors = new JSONObject(response.errorBody().string()).getJSONArray("errors");
                            if (errors.getJSONObject(0).getJSONObject("context").getString("key").equals("address")){
                                Toast.makeText(context, context.getString(R.string.way_enter_address), Toast.LENGTH_LONG).show();
                            }else if (errors.getJSONObject(0).getJSONObject("context").getString("key").equals("addressEn")){
                                Toast.makeText(context, context.getString(R.string.way_enter_addressEn), Toast.LENGTH_LONG).show();
                            }else
                                Toast.makeText(context, errors.getJSONObject(0).getJSONObject("context").getString("key"), Toast.LENGTH_SHORT).show();
                            Log.i("TTTTTT", errors + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }else {
                        try {
                            String message = new JSONObject(response.errorBody().string())
                                    .getString("message");
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            Log.i("GGGGGGG", new JSONObject(response.errorBody().string())
                                    .getJSONArray("errors").getJSONObject(0).getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
