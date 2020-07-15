package com.dopave.diethub_vendor.UI.Fragments.Orders;

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

import com.dopave.diethub_vendor.Adapter.AdapterForDelegate;
import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.GetDeliveries.GetDeliveriesData;
import com.dopave.diethub_vendor.Models.Orders.Orders;
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

public class OrdersRepository {
    public static OrdersRepository repository;
    public static OrdersRepository getInstance(){
        if (repository == null)
            repository = new OrdersRepository();
        return repository;
    }

    public MutableLiveData<Orders> getAllOrders (String [] Status, OrdersViewModel viewModel,
                                                 final ProgressDialog dialog,
                                                 final Context context ,int limit, int skip){
        final MutableLiveData<Orders> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().
                getAllOrders(
                "Bearer "+Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",
                true,true,true,Status,limit,skip).enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response) {
                dialog.dismiss();
                if (response.code() == 200)
                    mutableLiveData.setValue(response.body());
                else {
                    try {
                        Log.i("EEEEEE",new JSONObject(response.errorBody().string()).getJSONArray("errors").getJSONObject(0).getString("message")+" "+response.code());
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t) {
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
//                        viewModel
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

}