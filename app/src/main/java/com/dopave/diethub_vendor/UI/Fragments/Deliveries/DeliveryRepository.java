package com.dopave.diethub_vendor.UI.Fragments.Deliveries;

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
import androidx.recyclerview.widget.RecyclerView;

import com.dopave.diethub_vendor.Adapter.AdapterForDelegate;
import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.GetDeliveries.GetDeliveriesData;
import com.dopave.diethub_vendor.Models.VehicleTypes.VehicleTypes;
import com.dopave.diethub_vendor.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryRepository {
    public static DeliveryRepository repository;
    public static DeliveryRepository getInstance(){
        if (repository == null)
            repository= new DeliveryRepository();
        return repository;
    }

    public MutableLiveData<GetDeliveriesData> getAllDeliveries(final ProgressDialog dialog,
                                                               final Context context,
                                                               final DeliveryViewModel viewModel,
                                                               final RecyclerView recyclerView,
                                                               final VehicleTypes vehicleTypes,
                                                               boolean hasTrip){  // to filter by has Trip
        final MutableLiveData<GetDeliveriesData> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().getAllDeliveries("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",
                true,true,true,hasTrip)
                .enqueue(new Callback<GetDeliveriesData>() {
                    @Override
                    public void onResponse(Call<GetDeliveriesData> call, Response<GetDeliveriesData> response) {
                        dialog.dismiss();
                        if (response.code() == 200){
                            mutableLiveData.setValue(response.body());
                        } else {
                            if (response.code() >= 500){
                                Toast.makeText(context, R.string.Server_problem, Toast.LENGTH_SHORT).show();
                            }else if (response.code() == 401){
                                Common.onCheckTokenAction(context);
                            }else {
                                try {
                                    String message = new JSONObject(response.errorBody()
                                            .string()).getString("message");
                                    Log.i("TTTTTTT", message + response.code());
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetDeliveriesData> call, Throwable t) {
                        dialog.dismiss();
                        final AlertDialog.Builder Adialog = new AlertDialog.Builder(context);
                        View view = LayoutInflater.from(context).inflate(R.layout.error_dialog, null);
                        TextView Title = view.findViewById(R.id.Title);
                        TextView Message = view.findViewById(R.id.Message);
                        Button button = view.findViewById(R.id.Again);
                        Adialog.setView(view);
                        final AlertDialog dialog1 = Adialog.create();
                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                                dialog.show();
                                viewModel.getAllDeliveries(dialog, context,viewModel,recyclerView,vehicleTypes).observe((LifecycleOwner) context, new Observer<GetDeliveriesData>() {
                                    @Override
                                    public void onChanged(GetDeliveriesData getDeliveriesData) {
                                        if (getDeliveriesData.getData().getDeliveryRows().size() != 0)
                                        {
                                            recyclerView.setAdapter(new AdapterForDelegate(
                                                    getDeliveriesData.getData().getDeliveryRows(),context,recyclerView,
                                                    viewModel,vehicleTypes));
                                        }
                                        else
                                            Toast.makeText(context, "there are't any deliveries yet", Toast.LENGTH_SHORT).show();
                                    }
                                });
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

    public MutableLiveData<GetDeliveriesData> getAllDeliveries(final ProgressDialog dialog,
                                                               final Context context,
                                                               final DeliveryViewModel viewModel,
                                                               final RecyclerView recyclerView,
                                                               final VehicleTypes vehicleTypes){ // to get All deliveries
        final MutableLiveData<GetDeliveriesData> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().getAllDeliveries("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",
                true,true,true)
                .enqueue(new Callback<GetDeliveriesData>() {
                    @Override
                    public void onResponse(Call<GetDeliveriesData> call, Response<GetDeliveriesData> response) {
                        dialog.dismiss();
                        if (response.code() == 200){
                            mutableLiveData.setValue(response.body());
                        } else {
                            if (response.code() >= 500){
                                Toast.makeText(context, R.string.Server_problem, Toast.LENGTH_SHORT).show();
                            }else if (response.code() == 401){
                                Common.onCheckTokenAction(context);
                            }else {
                                try {
                                    String message = new JSONObject(response.errorBody()
                                            .string()).getString("message");
                                    Log.i("TTTTTTT", message + response.code());
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetDeliveriesData> call, Throwable t) {
                        dialog.dismiss();
                        final AlertDialog.Builder Adialog = new AlertDialog.Builder(context);
                        View view = LayoutInflater.from(context).inflate(R.layout.error_dialog, null);
                        TextView Title = view.findViewById(R.id.Title);
                        TextView Message = view.findViewById(R.id.Message);
                        Button button = view.findViewById(R.id.Again);
                        Adialog.setView(view);
                        final AlertDialog dialog1 = Adialog.create();

                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                                dialog.show();
                                viewModel.getAllDeliveries(dialog, context,viewModel,recyclerView,vehicleTypes).observe((LifecycleOwner) context, new Observer<GetDeliveriesData>() {
                                    @Override
                                    public void onChanged(GetDeliveriesData getDeliveriesData) {
                                        if (getDeliveriesData.getData().getDeliveryRows().size() != 0)
                                        {
                                            recyclerView.setAdapter(new AdapterForDelegate(
                                                    getDeliveriesData.getData().getDeliveryRows(),context,recyclerView,
                                                    viewModel,vehicleTypes));
                                        }
                                        else
                                            Toast.makeText(context, "there are't any deliveries yet", Toast.LENGTH_SHORT).show();
                                    }
                                });
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

    public MutableLiveData<GetDeliveriesData> deleteDelivery(final String deliveryId,
                                                             final ProgressDialog dialog,
                                                             final Context context){
        final MutableLiveData<GetDeliveriesData> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().deleteDeliveryByProvider("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+""
                ,deliveryId).enqueue(new Callback<GetDeliveriesData>() {
            @Override
            public void onResponse(Call<GetDeliveriesData> call, Response<GetDeliveriesData> response) {
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
                            Log.i("TTTTTTT", new JSONObject(response.errorBody()
                                    .string()).getString("message") + response.code() + "dfsfsdfs");
                            Toast.makeText(context, new JSONObject(response.errorBody()
                                    .string()).getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetDeliveriesData> call, Throwable t) {
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
