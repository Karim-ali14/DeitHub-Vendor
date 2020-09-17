package com.dopave.diethub_vendor.UI.CreateDelivery;

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
import com.dopave.diethub_vendor.Models.Cities.Cities;
import com.dopave.diethub_vendor.Models.CreateDelivery.Request.CreateDeliveryRequest;
import com.dopave.diethub_vendor.Models.CreateDelivery.Response.CreateDeliveryResponse;
import com.dopave.diethub_vendor.Models.GetDeliveries.GetDeliveriesData;
import com.dopave.diethub_vendor.Models.UpdateDeliveryRequest.UpdateDeliveryRequest;
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

public class CreateDeliveryRepository {
    public static CreateDeliveryRepository repository;
    public static CreateDeliveryRepository getInstance(){
        if (repository == null)
            repository = new CreateDeliveryRepository();
        return repository;
    }

    public MutableLiveData<Cities> getCities(final Context context, final ProgressDialog dialog,
                                             final CreateDeliveryViewModel viewModel){
        final MutableLiveData<Cities> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().getAllCities().enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Call<Cities> call, Response<Cities> response) {
                dialog.dismiss();
                if (response.code() == 200){
                    mutableLiveData.setValue(response.body());
                }else {
                    if (response.code() == 500){
                        Toast.makeText(context, R.string.Server_problem, Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<Cities> call, Throwable t) {
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
                        viewModel.getCities(context,dialog,viewModel).observe((LifecycleOwner) context, new Observer<Cities>() {
                            @Override
                            public void onChanged(Cities cities) {
                                ((CreateDeliveryActivity)context).onGetCity(cities);
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

    public MutableLiveData<CreateDeliveryResponse> createDelivery (final String Auth,
                                                                   final CreateDeliveryRequest requestBody,
                                                                   final String id,
                                                                   final Context context,
                                                                   final ProgressDialog dialog){
        final MutableLiveData<CreateDeliveryResponse> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().createDeliveryByProvider(Auth, requestBody, id)
                .enqueue(new Callback<CreateDeliveryResponse>() {
            @Override
            public void onResponse(Call<CreateDeliveryResponse> call,
                                   Response<CreateDeliveryResponse> response) {
                dialog.dismiss();
                if (response.code() == 201)
                    mutableLiveData.setValue(response.body());
                else {
                    try {
                        if (response.code() == 409) {
                            JSONArray errors = new JSONObject(response.errorBody().string()).getJSONArray("errors");
                            if (errors.getJSONObject(0).getJSONObject("context").getString("key").equals("mobilePhone")){
                                Toast.makeText(context, R.string.phone_number_existing, Toast.LENGTH_SHORT).show();
                            }else if (errors.getJSONObject(0).getJSONObject("context").getString("key").equals("email")){
                                Toast.makeText(context, R.string.email_existing, Toast.LENGTH_SHORT).show();
                            }else
                                Toast.makeText(context, errors.getJSONObject(0).getString("message"), Toast.LENGTH_SHORT).show();
                            Log.i("TTTTTT", errors + "");
                        }else if (response.code() == 422)
                            Toast.makeText(context, R.string.data_input_incorrect, Toast.LENGTH_SHORT).show();
                        else if (response.code() == 500){
                            Toast.makeText(context, R.string.Server_problem, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, new JSONObject(response.errorBody().string())
                                    .getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateDeliveryResponse> call, Throwable t) {
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

    public MutableLiveData<GetDeliveriesData> updateDelivery(final UpdateDeliveryRequest updateDeliveryRequest
            , final String deliveryId, final ProgressDialog dialog, final Context context){
        final MutableLiveData<GetDeliveriesData> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().updateDeliveryByProvider("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken()
                ,updateDeliveryRequest,
                Common.currentPosition.getData().getProvider().getId()+""
                ,deliveryId).enqueue(new Callback<GetDeliveriesData>() {
            @Override
            public void onResponse(Call<GetDeliveriesData> call, Response<GetDeliveriesData> response) {
                dialog.dismiss();
                if (response.code() == 200){
                    mutableLiveData.setValue(response.body());
                }else {
                    try {
                        if (response.code() == 409) {
                            JSONArray errors = new JSONObject(response.errorBody().string()).getJSONArray("errors");
                            if (errors.getJSONObject(0).getJSONObject("context").getString("key").equals("mobilePhone")){
                                Toast.makeText(context, R.string.phone_number_existing, Toast.LENGTH_SHORT).show();
                            }else if (errors.getJSONObject(0).getJSONObject("context").getString("key").equals("email")){
                                Toast.makeText(context, R.string.email_existing, Toast.LENGTH_SHORT).show();
                            }else
                                Toast.makeText(context, errors.getJSONObject(0).getString("message"), Toast.LENGTH_SHORT).show();
                            Log.i("TTTTTT", errors + "");
                        }else if (response.code() == 422)
                            Toast.makeText(context, R.string.data_input_incorrect, Toast.LENGTH_SHORT).show();
                        else if (response.code() == 500){
                            Toast.makeText(context, R.string.Server_problem, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, new JSONObject(response.errorBody().string())
                                    .getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
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
