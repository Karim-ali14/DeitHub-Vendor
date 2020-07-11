package com.dopave.diethub_vendor.UI.Subscriptions;

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
import com.dopave.diethub_vendor.Models.Subscriptions.Subscriptions;
import com.dopave.diethub_vendor.Models.Subscriptions.UpdateStatus.UpdateSubscriptionStatus;
import com.dopave.diethub_vendor.Models.Years.Years;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.CreateVehicle.CreateVehicleActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriptionsRepository {
    public static SubscriptionsRepository repository;
    public static SubscriptionsRepository getInstance(){
        if (repository == null)
            repository = new SubscriptionsRepository();
        return repository;
    }

    public MutableLiveData<Subscriptions> getAllSubscriptions(final Context context,
                                                              final ProgressDialog dialog,
                                                              final SubscriptionsViewModel viewModel,
                                                              final int type,
                                                              final String status){
        final MutableLiveData<Subscriptions> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().getAllSubscriptionbyProviderId(
                "Bearer "+Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",
                true,true,status).enqueue(new Callback<Subscriptions>() {
            @Override
            public void onResponse(Call<Subscriptions> call, Response<Subscriptions> response) {
                dialog.dismiss();
                if (response.code() == 200)
                    mutableLiveData.setValue(response.body());
                else {
                    try {
                        String message = new JSONObject(response.errorBody().string())
                                .getString("message");
                        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();

                        Log.i("TTTTTT",new JSONObject(response.errorBody().string())
                                .getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Subscriptions> call, Throwable t) {
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
                    public void onClick(final View v) {
                        dialog1.dismiss();
                        dialog.show();
                        viewModel.getAllSubscriptions(context, dialog, viewModel,type,status).observe((LifecycleOwner) context,
                                new Observer<Subscriptions>() {
                            @Override
                            public void onChanged(Subscriptions subscriptions) {
                                ((SubscriptionsActivity)context).setListOfSubscription(type,
                                        subscriptions.getData().getRows(),viewModel,status);
                            }
                        });
                    }
                });
                if(t instanceof SocketTimeoutException) {
                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
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

    public MutableLiveData<Subscriptions> UpdateSubscriptionStatus(final Context context,
                                                                   final ProgressDialog dialog, String id,
                                                                   UpdateSubscriptionStatus updateSubscriptionStatus){
        final MutableLiveData<Subscriptions> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().UpdateSubscriptionStatus(
                "Bearer "+Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",id,
                updateSubscriptionStatus).enqueue(new Callback<Subscriptions>() {
            @Override
            public void onResponse(Call<Subscriptions> call, Response<Subscriptions> response) {
                if (200 == response.code()) {
                    mutableLiveData.setValue(response.body());
                }else {
                    dialog.dismiss();
                    try {
                        Toast.makeText(context, new JSONObject(response.errorBody().string())
                                .getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Subscriptions> call, Throwable t) {
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
