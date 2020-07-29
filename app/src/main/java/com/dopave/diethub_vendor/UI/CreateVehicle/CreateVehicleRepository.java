package com.dopave.diethub_vendor.UI.CreateVehicle;

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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.dopave.diethub_vendor.Adapter.AdapterForDelegate;
import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.CreateVehicle.Request.CreateVehicleRequest;
import com.dopave.diethub_vendor.Models.CreateVehicle.Request.Image;
import com.dopave.diethub_vendor.Models.CreateVehicle.Response.CreateVehicleRespons;
import com.dopave.diethub_vendor.Models.GetDeliveries.GetDeliveriesData;
import com.dopave.diethub_vendor.Models.GetVehicles.Data;
import com.dopave.diethub_vendor.Models.GetVehicles.GetVehicleData;
import com.dopave.diethub_vendor.Models.UpdateVehicle.DrivingLicenceImage;
import com.dopave.diethub_vendor.Models.UpdateVehicle.UpdateVehicle;
import com.dopave.diethub_vendor.Models.UpdateVehicle.VehicleLicenceImage;
import com.dopave.diethub_vendor.Models.VehicleTypes.VehicleTypes;
import com.dopave.diethub_vendor.Models.Years.Years;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.Fragments.Deliveries.DeliveryViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateVehicleRepository {
    public static CreateVehicleRepository repository;
    public static CreateVehicleRepository getInstance(){
        if (repository == null)
            repository = new CreateVehicleRepository();
        return repository;
    }

    public MutableLiveData<VehicleTypes> getAllVehicleTypes(final Context context,
                                                            final ProgressDialog dialog,
                                                            final CreateVehicleViewModel viewModel,
                                                            final String type,
                                                            final RecyclerView recyclerView){
        final MutableLiveData<VehicleTypes> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().getAllVehicleTypes().enqueue(new Callback<VehicleTypes>() {
            @Override
            public void onResponse(Call<VehicleTypes> call, Response<VehicleTypes> response) {
                if (response.code()==200){
                    mutableLiveData.setValue(response.body());
                }else {
                    dialog.dismiss();
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
            public void onFailure(Call<VehicleTypes> call, Throwable t) {
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
                        viewModel.getAllVehicleTypes(context,dialog,viewModel,type,recyclerView).observe((LifecycleOwner) context, new Observer<VehicleTypes>() {
                            @Override
                            public void onChanged(final VehicleTypes vehicleTypes) {
                                if (type.equals("CreateVehicleActivity")) {
                                    CreateVehicleActivity activity = (CreateVehicleActivity) context;
                                    activity.onGetVehicleType(vehicleTypes);
                                }
                                else if (type.equals("DeliveryFragment")){
                                    final DeliveryViewModel deliveryViewModel = ViewModelProviders.of(
                                            (FragmentActivity)
                                                    context).get(DeliveryViewModel.class);
                                                    deliveryViewModel.getAllDeliveries
                                                            (dialog,context,deliveryViewModel,
                                                                    recyclerView,vehicleTypes)
                                                            .observe((LifecycleOwner) context,
                                                    new Observer<GetDeliveriesData>() {
                                                        @Override
                                                        public void onChanged(GetDeliveriesData getDeliveriesData) {
                                                            if (getDeliveriesData.getData().getDeliveryRows().size() != 0)
                                                            {
                                                                recyclerView.setAdapter(new AdapterForDelegate(
                                                                        getDeliveriesData.getData().getDeliveryRows(),context,recyclerView,
                                                                        deliveryViewModel,vehicleTypes));
                                                            }
                                                            else
                                                                Toast.makeText(context, "there are't any deliveries yet", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                }
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

    public MutableLiveData<Years> getAllYears(final Context context, final ProgressDialog dialog,
                                              final CreateVehicleViewModel viewModel){
        final MutableLiveData<Years> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().getAllYears().enqueue(new Callback<Years>() {
            @Override
            public void onResponse(Call<Years> call, Response<Years> response) {
                dialog.dismiss();
                if (response.code() == 200){
                    mutableLiveData.setValue(response.body());
                }else {
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
            public void onFailure(final Call<Years> call, Throwable t) {
                dialog.dismiss();
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
                        viewModel.getAllYears(context,dialog,viewModel).observe((LifecycleOwner) context, new Observer<Years>() {
                            @Override
                            public void onChanged(Years years) {
                                CreateVehicleActivity activity = (CreateVehicleActivity) context;
                                activity.onGetYears(years);
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

    public MutableLiveData<CreateVehicleRespons> createVehicle(String Auth, String id,
                                                               final String deliveryId,
                                                               CreateVehicleRequest createVehicleRequest,
                                                               final Context context,
                                                               final ProgressDialog dialog){
        final MutableLiveData<CreateVehicleRespons> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().createVehicle(Auth, id, deliveryId, createVehicleRequest)
                .enqueue(new Callback<CreateVehicleRespons>() {
            @Override
            public void onResponse(Call<CreateVehicleRespons> call, Response<CreateVehicleRespons> response) {
                dialog.dismiss();
                if (response.code() == 201)
                    mutableLiveData.setValue(response.body());
                else {
                    try {
                        if (response.code() == 422)
                            Toast.makeText(context, R.string.data_input_incorrect, Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<CreateVehicleRespons> call, Throwable t) {
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

    public MutableLiveData<GetVehicleData> getVehicleData(final String deliveryId, final Context context,
                                                          final ProgressDialog dialog,
                                                          final CreateVehicleViewModel viewModel){
        final MutableLiveData<GetVehicleData> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().getVehicleByDeliveryId("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId().toString(),deliveryId,
                true,true,true)
                .enqueue(new Callback<GetVehicleData>() {
                    @Override
                    public void onResponse(Call<GetVehicleData> call, Response<GetVehicleData> response) {
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
                    public void onFailure(Call<GetVehicleData> call, Throwable t) {
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
                                viewModel.getVehicleData(deliveryId,context,dialog,viewModel)
                                        .observe((LifecycleOwner) context, new Observer<GetVehicleData>() {
                                    @Override
                                    public void onChanged(GetVehicleData getVehicleData) {
                                        CreateVehicleActivity activity = (CreateVehicleActivity) context;
                                        activity.VehicleData = getVehicleData;
                                        activity.setData();
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

    public MutableLiveData<Data> updateVehicle(String vehicleId, String Number, String vehicleModel,
                                               int selectedYear, int vehicleTypes,
                                               String drivingLicenceImage,
                                               String vehicleLicenceImage, List<Image> list,
                                               final Context context, final ProgressDialog dialog){
        final MutableLiveData<Data> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().updateVehicle("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",
                vehicleId,new UpdateVehicle(Number,vehicleModel,
                        selectedYear,vehicleTypes,new DrivingLicenceImage(drivingLicenceImage),
                        new VehicleLicenceImage(vehicleLicenceImage),list)).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                dialog.dismiss();
                if (response.code() == 200)
                    mutableLiveData.setValue(response.body());
                else {
                    try {
                        if (response.code() == 422)
                            Toast.makeText(context, R.string.data_input_incorrect, Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<Data> call, Throwable t) {
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
