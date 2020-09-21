package com.dopave.diethub_vendor.UI.Notifications;

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

import com.dopave.diethub_vendor.Adapter.AdapterForNotification;
import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.Notifications.NotificationData;
import com.dopave.diethub_vendor.Models.Settings.Settings;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.AboutUs.AboutUs_Activity;
import com.dopave.diethub_vendor.UI.Conditions.Conditions_Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsRepository {
    public static NotificationsRepository repository;
    public static NotificationsRepository getInstance(){
        if (repository == null)
            repository = new NotificationsRepository();
        return repository;
    }

    public MutableLiveData<NotificationData> getAllNotifies(final Context context,
                                                            final ProgressDialog dialog,
                                                            final NotificationsViewModel viewModel,
                                                            int type){
        if (type == 0)
            dialog.show();
        final MutableLiveData<NotificationData> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().getAllNotifications("Bearer "+
                Common.currentPosition.getData().getToken().getAccessToken()).enqueue(new Callback<NotificationData>() {
            @Override
            public void onResponse(Call<NotificationData> call, Response<NotificationData> response) {
                dialog.dismiss();
                if (response.code() == 200){
                   mutableLiveData.setValue(response.body());
                }else {

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
            public void onFailure(final Call<NotificationData> call, Throwable t) {
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
                        viewModel.getAllNotifies(context, dialog, viewModel,0)
                                .observe((LifecycleOwner) context, new Observer<NotificationData>() {
                            @Override
                            public void onChanged(NotificationData notificationData) {
                                ((Notification_Activity)context).onGetNotifiesData(notificationData,
                                        "normal");
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

    public MutableLiveData<Defualt> deleteSpecificNotify(final Context context,
                                                         final ProgressDialog dialog,
                                                         String NotifyId){
        dialog.show();
        final MutableLiveData<Defualt> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().deleteSpecificNotify("Bearer "+
                Common.currentPosition.getData().getToken().getAccessToken(),NotifyId).enqueue(new Callback<Defualt>() {
            @Override
            public void onResponse(final Call<Defualt> call, Response<Defualt> response) {
                if (response.code() == 200){
                    mutableLiveData.setValue(response.body());
                } else {
                    dialog.dismiss();
                    if (response.code() == 500){
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
            public void onFailure(Call<Defualt> call, Throwable t) {
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

    public MutableLiveData<Defualt> deleteAllNotifies(final Context context,
                                                                       final ProgressDialog dialog){
        dialog.show();
        final MutableLiveData<Defualt> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().deleteAllNotifies("Bearer "+
                Common.currentPosition.getData().getToken().getAccessToken())
                .enqueue(new Callback<Defualt>() {
            @Override
            public void onResponse(Call<Defualt> call, Response<Defualt> response) {
                if (response.code() == 200){
                    mutableLiveData.setValue(response.body());
                } else {
                    dialog.dismiss();
                    if (response.code() == 500){
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
            public void onFailure(Call<Defualt> call, Throwable t) {
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
