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
import androidx.lifecycle.MutableLiveData;
import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Orders.Orders;
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

public class OrdersRepository {
    public static OrdersRepository repository;
    public static OrdersRepository getInstance(){
        if (repository == null)
            repository = new OrdersRepository();
        return repository;
    }

    public MutableLiveData<Orders> getAllOrders (final String [] Status, final OrdersViewModel viewModel,
                                                 final ProgressDialog dialog,
                                                 final Context context,
                                                 final int limit, final int skip, final int typeRequest){
        final MutableLiveData<Orders> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().
                getAllOrders(
                "Bearer "+Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",
                true,true,true,true,Status,limit,skip).enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response) {
                dialog.dismiss();
                if (response.code() == 200)
                    mutableLiveData.setValue(response.body());
                else {
                    if (response.code() == 500){
                        Toast.makeText(context, R.string.Server_problem, Toast.LENGTH_SHORT).show();
                    }else if (response.code() == 401){
                        Common.onCheckTokenAction(context);
                    }else {
                        try {
                            String message = new JSONObject(response.errorBody().string())
                                    .getString("message");
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                            Log.i("TTTTTT", new JSONObject(response.errorBody().string())
                                    .getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(final Call<Orders> call, Throwable t) {
                dialog.dismiss();
                final AlertDialog.Builder Adialog = new AlertDialog.Builder(context);
                final View view = LayoutInflater.from(context).inflate(R.layout.error_dialog, null);
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
                        if (typeRequest == 0){
                            OrderFragment orderFragment = (OrderFragment)
                                    ((HomeActivity)context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                            orderFragment.getOrders(Status,0,limit,skip,typeRequest);
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

}
